package com.Rothana.hotel_booking_system.fileupload;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {


    @Autowired
    private Cloudinary cloudinary;


    @Override
    public Map upload(MultipartFile file) throws IOException {

        try {
            Map data =   this.cloudinary.uploader().upload(file.getBytes() , Map.of());
            return  data;
        }catch (IOException e){
            throw new RemoteException("Image upload failed");
        }
    }
}