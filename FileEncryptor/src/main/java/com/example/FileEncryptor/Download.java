package com.example.FileEncryptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Download {
    
    @Autowired
    private Upload upload;
    
    /**
     * Prepares a file for download
     * 
     * @param data The byte array of the file to download
     * @param fileName The name to give the downloaded file
     * @return ResponseEntity containing the file as a resource for download
     */
    public ResponseEntity<Resource> downloadFile(byte[] data, String fileName) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("No data available for download");
        }
        
        // Create a resource from the data
        ByteArrayResource resource = new ByteArrayResource(data);
        
        // Set up response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        
        // Return the response entity with the file
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}