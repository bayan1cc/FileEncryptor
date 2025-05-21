package com.example.FileEncryptor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class Upload {
    private byte[] file;
    private byte[] key;
    private String typeOfUploadedFile;

    public void UploadProcess(MultipartFile file, String password) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (file.getSize() > 1000_000_000) { // 1GB limit example
            throw new IllegalArgumentException("File too large");
        }

        String originalFilename = file.getOriginalFilename();

        // Extract file extension (type)
        if (originalFilename != null && originalFilename.contains(".")) {
            this.typeOfUploadedFile = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } else {
            // Default type if extension can't be determined
            this.typeOfUploadedFile = "bin";
        }

        this.file = file.getBytes();
        this.key = password.getBytes();
    }

    public byte[] getFile() {
        return file;
    }

    public byte[] getKey() {
        return key;
    }
    
    public String getTypeOfUploadedFile() {
        return typeOfUploadedFile;
    }
}