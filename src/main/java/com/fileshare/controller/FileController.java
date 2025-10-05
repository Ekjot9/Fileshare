
package com.fileshare.controller;

import com.fileshare.model.FileEntity;
import com.fileshare.service.FileService;
import com.fileshare.service.QRCodeService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private QRCodeService qrCodeService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            FileEntity savedFile = fileService.storeFile(file);

            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                    .replacePath(null)
                    .build()
                    .toUriString();

            String downloadUrl = baseUrl + "/file/download/" + savedFile.getId();
            String qrCodeBase64 = qrCodeService.generateQrCodeAsBase64(downloadUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("fileId", savedFile.getId());
            response.put("originalFileName", savedFile.getOriginalFileName());
            response.put("expiryTime", savedFile.getExpiryTime());
            response.put("downloadUrl", downloadUrl);
            response.put("qrCodeImage", "data:image/png;base64," + qrCodeBase64);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

   @GetMapping("/download/{id}")
public ResponseEntity<?> downloadFile(@PathVariable Long id) {
    FileEntity fileEntity = fileService.getFileById(id);

    if (fileEntity == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
    }

    if (fileEntity.getExpiryTime() != null && fileEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
        return ResponseEntity.status(HttpStatus.GONE).body("File has expired.");
    }

    if (fileEntity.getDownloadCount() >= fileEntity.getMaxDownloads()) {
        return ResponseEntity.status(HttpStatus.GONE).body("File download limit exceeded.");
    }

    // ✅ Increment count before file read attempt (ensures count is saved even if file is downloaded but not saved)
    fileEntity.setDownloadCount(fileEntity.getDownloadCount() + 1);
    fileService.saveFile(fileEntity);

    File file = new File("uploads/" + fileEntity.getStoredFileName());
    if (!file.exists()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File missing on server.");
    }

    try {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getOriginalFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileBytes);
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file.");
    }
}


    @GetMapping(value = "/qr/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getFileQrCode(@PathVariable Long id, HttpServletRequest request) {
        FileEntity file = fileService.getFileById(id);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        String downloadUrl = baseUrl + "/file/download/" + id;

        try {
            BufferedImage qrImage = qrCodeService.generateQRCodeImage(downloadUrl, 300, 300);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(baos.toByteArray());

        } catch (IOException | WriterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    @DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteFile(@PathVariable Long id) {
    System.out.println("🔍 DELETE called for file ID: " + id);
    FileEntity file = fileService.getFileById(id);

    if (file == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
    }

    File physicalFile = new File("uploads/" + file.getStoredFileName());
    if (physicalFile.exists()) {
        physicalFile.delete(); // delete from disk
    }

    fileService.deleteFileById(id); // delete from database

    return ResponseEntity.ok("File deleted successfully.");
}
}