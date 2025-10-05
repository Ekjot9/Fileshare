package com.fileshare.service;

import com.fileshare.model.FileEntity;
import com.fileshare.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleanupService {
     @Autowired
    private FileRepository fileRepository;

    @Scheduled(fixedRate = 15000) // Runs every 60 seconds
    public void deleteExpiredFiles() {
        System.out.println("Cleanup task is running...");

        List<FileEntity> expiredFiles = fileRepository.findAll().stream()
            .filter(file -> file.getExpiryTime() != null && file.getExpiryTime().isBefore(LocalDateTime.now()))
            .toList();

            

        for (FileEntity file : expiredFiles) {
            File f = new File("uploads/" + file.getStoredFileName());
            if (f.exists()) f.delete();  // Delete file from storage
            fileRepository.delete(file); // Delete from DB
            System.out.println("Deleted expired file: " + file.getOriginalFileName() + " (ID: " + file.getId() + ")");

        }
    }
}
