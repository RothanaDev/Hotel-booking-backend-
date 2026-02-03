package com.Rothana.hotel_booking_system.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {


    @Value("${room-upload.server-path}")
    private String serverPath;

    @Value("${room-upload.base-uri}")
    private String baseUri;

    @Override
    public void deleteByFileName(String fileName) {
        Path path = Paths.get(serverPath, fileName);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            }catch (IOException e)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR  ,"File upload failed to delete");
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "File not found");
        }
    }

    @Override
    public List<FileUploadResponse> uploadMultiple(List<MultipartFile> files) {
        List<FileUploadResponse> fileUploadResponses = new ArrayList<>();
        files.forEach(file ->{
            FileUploadResponse fileUploadResponse = upload(file);
            fileUploadResponses.add(fileUploadResponse);
        });
        return fileUploadResponses;
    }

    @Override
    public FileUploadResponse upload(MultipartFile file) {

        String extension = file.getContentType().split("/")[1];
        String fileName = String.format("%s.%s", UUID.randomUUID(), extension);
        Path path = Paths.get(serverPath + fileName);

        try {
            Files.copy(file.getInputStream() , path);
        }catch (IOException e){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR  ,"File upload failed");
        }

        return FileUploadResponse.builder()
                .name(fileName)
                .url(baseUri + fileName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();
    }
}