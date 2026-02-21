
package com.Rothana.hotel_booking_system.features.Payments;

import com.Rothana.hotel_booking_system.features.Payments.dto.PaypalCaptureRequest;
import com.Rothana.hotel_booking_system.features.Payments.dto.PaypalCaptureResponse;
import com.Rothana.hotel_booking_system.features.Payments.dto.PaypalCreateOrderResponse;

public interface PaymentService {
    PaypalCreateOrderResponse createPaypalOrder(Integer bookingId);
    PaypalCaptureResponse capturePaypalOrder(PaypalCaptureRequest request);
}
