package com.Rothana.hotel_booking_system.fileupload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileUploadService {
    public Map upload(MultipartFile file) throws IOException;
}
