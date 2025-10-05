package com.fileshare.service;

import com.fileshare.model.FileEntity;
import com.fileshare.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

   
public FileEntity storeFile(MultipartFile file) throws IOException {
    String originalFilename = file.getOriginalFilename();
    String storedFileName = UUID.randomUUID().toString() + "_" + originalFilename;

    // 📁 Define the upload directory path
    Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

    // 🧱 Create the uploads folder if it doesn't exist
    if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
    }

    // 📄 Save file physically
    Path filePath = uploadPath.resolve(storedFileName);
    file.transferTo(filePath.toFile());

    // 🗃️ Save file metadata to the database
    FileEntity fileEntity = new FileEntity();
    fileEntity.setOriginalFileName(originalFilename);
    fileEntity.setStoredFileName(storedFileName);
    fileEntity.setUploadTime(LocalDateTime.now());
    fileEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

    fileEntity.setDownloadCount(0);              // ✅ Start from 0
    fileEntity.setMaxDownloads(3);               // ✅ Set limit to 3

    return fileRepository.save(fileEntity);
}

public FileEntity getFileById(Long id) {
    return fileRepository.findById(id).orElse(null);
}


public FileEntity saveFile(FileEntity fileEntity) {
    return fileRepository.save(fileEntity);
}

public void deleteFileById(Long id) {
    fileRepository.deleteById(id);
}

}
