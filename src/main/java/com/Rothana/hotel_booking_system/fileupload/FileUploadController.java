package com.Rothana.hotel_booking_system.fileupload;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{fileName}")
    void deleteFileName(@PathVariable String fileName) {
        fileUploadService.deleteByFileName(fileName);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/multiple")
    List<FileUploadResponse> uploadMultiple(@RequestBody List<MultipartFile> files) {
        return fileUploadService.uploadMultiple(files);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    FileUploadResponse upload(@RequestBody MultipartFile file) {
        return fileUploadService.upload(file);
    }
}
