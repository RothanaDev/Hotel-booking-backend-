package com.Rothana.hotel_booking_system.features.Payments;

import com.Rothana.hotel_booking_system.entity.Booking;
import com.Rothana.hotel_booking_system.entity.Payment;
import com.Rothana.hotel_booking_system.entity.PaymentStatus;
import com.Rothana.hotel_booking_system.features.Payments.dto.PaypalCaptureRequest;
import com.Rothana.hotel_booking_system.features.Payments.dto.PaypalCaptureResponse;
import com.Rothana.hotel_booking_system.features.Payments.dto.PaypalCreateOrderResponse;
import com.Rothana.hotel_booking_system.features.booking.BookingRepository;
import com.Rothana.hotel_booking_system.features.telegram.TelegramNotifyService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PayPalHttpClient payPalHttpClient;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final TelegramNotifyService telegramNotifyService;

    @Value("${app.currency:USD}")
    private String currency;

    @Value("${app.paypal.return-url:http://localhost:3000/paypal/success}")
    private String returnUrl;

    @Value("${app.paypal.cancel-url:http://localhost:3000/paypal/cancel}")
    private String cancelUrl;

    @Transactional
    @Override
    public PaypalCreateOrderResponse createPaypalOrder(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking Not Found"));

        BigDecimal amount = booking.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid booking amount");
        }

        String cur = currency == null ? "USD" : currency.trim().toUpperCase();
        String amountString = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();

        // ---------- Build PayPal Order ----------
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        // Item breakdown (helps sandbox + avoids some PayPal validation/risk issues)
        Money money = new Money()
                .currencyCode(cur)
                .value(amountString);

        Item item = new Item()
                .name("Hotel booking #" + booking.getId())
                .description("Hotel booking payment")
                .quantity("1")
                .unitAmount(money);

        AmountBreakdown breakdown = new AmountBreakdown()
                .itemTotal(money);

        AmountWithBreakdown amountWithBreakdown = new AmountWithBreakdown()
                .currencyCode(cur)
                .value(amountString)
                .amountBreakdown(breakdown);

        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .referenceId("BOOKING_" + booking.getId())
                .description("Hotel booking payment")
                .amountWithBreakdown(amountWithBreakdown)
                .items(List.of(item));

        // Experience context
        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .brandName("Hotel Booking System")
                .userAction("PAY_NOW")
                // IMPORTANT: avoid shipping/billing issues
                .shippingPreference("NO_SHIPPING");

        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
        orderRequest.applicationContext(applicationContext);

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.requestBody(orderRequest);

        try {
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();

            String approvalUrl = order.links().stream()
                    .filter(l -> "approve".equalsIgnoreCase(l.rel()))
                    .findFirst()
                    .map(LinkDescription::href)
                    .orElse(null);

            if (approvalUrl == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "PayPal approval url not found");
            }

            // Save payment row
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(amount);
            payment.setCurrency(cur);
            payment.setStatus(PaymentStatus.CREATED);
            payment.setPaypalOrderId(order.id());

            paymentRepository.save(payment);

            return new PaypalCreateOrderResponse(order.id(), approvalUrl);

        } catch (HttpException e) {
            // Better debug
            String msg = "PayPal error (" + e.statusCode() + "): " + e.getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, msg);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create PayPal order: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public PaypalCaptureResponse capturePaypalOrder(PaypalCaptureRequest captureRequest) {

        String orderId = captureRequest.orderId();
        if (orderId == null || orderId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId is required");
        }

        Payment payment = paymentRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        // Idempotent: already completed
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            return new PaypalCaptureResponse(orderId, payment.getPaypalCaptureId(), "COMPLETED");
        }

        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());

        try {
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();

            String paypalStatus = order.status();

            String captureId = null;
            if (order.purchaseUnits() != null && !order.purchaseUnits().isEmpty()) {
                PurchaseUnit pu = order.purchaseUnits().get(0);
                if (pu.payments() != null && pu.payments().captures() != null && !pu.payments().captures().isEmpty()) {
                    captureId = pu.payments().captures().get(0).id();
                }
            }

            if ("COMPLETED".equalsIgnoreCase(paypalStatus)) {

                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setPaypalCaptureId(captureId);

                Booking booking = payment.getBooking();
                booking.setStatus("paid");
                bookingRepository.save(booking);

                telegramNotifyService.sendBookingNotification(booking);

            } else if ("PENDING".equalsIgnoreCase(paypalStatus)) {
                payment.setStatus(PaymentStatus.PENDING);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
            }

            paymentRepository.save(payment);

            return new PaypalCaptureResponse(orderId, captureId, paypalStatus);

        } catch (HttpException e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            String msg = "PayPal error (" + e.statusCode() + "): " + e.getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, msg);

        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to capture PayPal order: " + e.getMessage());
        }
    }
}

// @Transactional
// @Override
// public PaypalCaptureResponse capturePaypalOrder(PaypalCaptureRequest
// captureRequest) {
//
// String orderId = captureRequest.orderId();
// if (orderId == null || orderId.isBlank()) {
// throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId is
// required");
// }
//
// Payment payment = paymentRepository.findByPaypalOrderId(orderId)
// .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment
// not found"));
//
// // Idempotent
// if (payment.getStatus() == PaymentStatus.COMPLETED) {
// return new PaypalCaptureResponse(orderId, payment.getPaypalCaptureId(),
// "COMPLETED");
// }
//
// OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
// request.requestBody(new OrderRequest()); // required by SDK
//
// try {
// HttpResponse<Order> response = payPalHttpClient.execute(request);
// Order order = response.result();
//
// String paypalStatus = order.status();
//
// String captureId = null;
// if (order.purchaseUnits() != null && !order.purchaseUnits().isEmpty()) {
// PurchaseUnit pu = order.purchaseUnits().get(0);
// if (pu.payments() != null && pu.payments().captures() != null &&
// !pu.payments().captures().isEmpty()) {
// captureId = pu.payments().captures().get(0).id();
// }
// }
//
// if ("COMPLETED".equalsIgnoreCase(paypalStatus)) {
// payment.setStatus(PaymentStatus.COMPLETED);
// payment.setPaypalCaptureId(captureId);
//
// Booking booking = payment.getBooking();
// booking.setStatus("paid");
// bookingRepository.save(booking);
//
// } else if ("PENDING".equalsIgnoreCase(paypalStatus)) {
// payment.setStatus(PaymentStatus.PENDING);
// } else {
// payment.setStatus(PaymentStatus.FAILED);
// }
//
// paymentRepository.save(payment);
//
// return new PaypalCaptureResponse(orderId, captureId, paypalStatus);
//
// } catch (HttpException e) {
// payment.setStatus(PaymentStatus.FAILED);
// paymentRepository.save(payment);
//
// String msg = "PayPal error (" + e.statusCode() + "): " + e.getMessage();
// throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, msg);
//
// } catch (Exception e) {
// payment.setStatus(PaymentStatus.FAILED);
// paymentRepository.save(payment);
// throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed
// to capture PayPal order: " + e.getMessage());
// }
// }
// }
