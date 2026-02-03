package com.Rothana.hotel_booking_system.fileupload;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class FileUploadController {
    private final FileUploadService fileUploadService;


    @PostMapping
    public ResponseEntity<Map> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        Map data =   this.fileUploadService.upload(file);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
