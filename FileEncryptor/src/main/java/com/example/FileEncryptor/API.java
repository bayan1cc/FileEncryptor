package com.example.FileEncryptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class API {
    @Autowired
    private Upload upload;
    
    @Autowired
    private Download download;

    @GetMapping("/encryption")
    public ResponseEntity<?> Encryption() {
        if (upload.getFile() == null) {
            return ResponseEntity.badRequest().body("No file has been uploaded yet");
        }

        Encryption encryption = new Encryption();
        byte[] encryptedData = encryption.EncryptionProcess(upload.getFile(), upload.getKey());

        return ResponseEntity.ok("File encrypted successfully");
    }

    @GetMapping("/decryption")
    public ResponseEntity<?> Decryption(){
        if (upload.getFile() == null) {
            return ResponseEntity.badRequest().body("No file has been uploaded yet");
        }

        Decryption decryption = new Decryption();
        byte[] decryptedData = decryption.DecryptionProcess(upload.getFile(), upload.getKey());

        return ResponseEntity.ok("File decrypted successfully");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("password") String password) throws IOException {
        upload.UploadProcess(file, password);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download() {
        if (upload.getFile() == null) {
            throw new IllegalArgumentException("No file has been uploaded yet");
        }

        String fileName = "downloaded_file";
        if (upload.getTypeOfUploadedFile() != null) {
            fileName = "downloaded_file." + upload.getTypeOfUploadedFile();
        }

        Encryption encryption = new Encryption();
        byte[] encryptedData = encryption.EncryptionProcess(upload.getFile(), upload.getKey());
        
        return download.downloadFile(encryptedData, fileName);
    }
    
    @GetMapping("/download/encrypted")
    public ResponseEntity<Resource> downloadEncrypted(@RequestParam(defaultValue = "encrypted_file") String fileName) {
        if (upload.getFile() == null) {
            throw new IllegalArgumentException("No file has been uploaded yet");
        }

        Encryption encryption = new Encryption();
        byte[] encryptedData = encryption.EncryptionProcess(upload.getFile(), upload.getKey());

        return download.downloadFile(encryptedData, fileName);
    }
    
    @GetMapping("/download/decrypted")
    public ResponseEntity<Resource> downloadDecrypted(@RequestParam(defaultValue = "decrypted_file") String fileName) {
        if (upload.getFile() == null) {
            throw new IllegalArgumentException("No file has been uploaded yet");
        }

        Decryption decryption = new Decryption();
        byte[] decryptedData = decryption.DecryptionProcess(upload.getFile(), upload.getKey());

        return download.downloadFile(decryptedData, fileName);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File too large! Maximum size is 1GB.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exc) {
        return ResponseEntity.badRequest().body(exc.getMessage());
    }
}