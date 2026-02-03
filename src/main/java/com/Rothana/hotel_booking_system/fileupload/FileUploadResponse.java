package com.Rothana.hotel_booking_system.fileupload;

import lombok.Builder;

@Builder
public record FileUploadResponse(
        String name,
        String url,
        String contentType,
        Long size
) {
}
