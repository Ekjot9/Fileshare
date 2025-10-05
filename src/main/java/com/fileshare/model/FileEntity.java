package com.fileshare.model;
import com.fileshare.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;
    private String storedFileName;
    

    private LocalDateTime uploadTime;

    private LocalDateTime expiryTime;
    @Column(nullable = false)
    private int downloadCount = 0;

    @Column(nullable = false)
    private int maxDownloads = 3; // 🔁 You can make this configurable later


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User uploadedBy; 

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getOriginalFileName() {
    return originalFileName;
}

public void setOriginalFileName(String originalFileName) {
    this.originalFileName = originalFileName;
}

public String getStoredFileName() {
    return storedFileName;
}

public void setStoredFileName(String storedFileName) {
    this.storedFileName = storedFileName;
}

public LocalDateTime getUploadTime() {
    return uploadTime;
}

public void setUploadTime(LocalDateTime uploadTime) {
    this.uploadTime = uploadTime;
}

public User getUploadedBy() {
    return uploadedBy;
}

public void setUploadedBy(User uploadedBy) {
    this.uploadedBy = uploadedBy;
}

public LocalDateTime getExpiryTime() {
    return expiryTime;
}

public void setExpiryTime(LocalDateTime expiryTime) {
    this.expiryTime = expiryTime;
}
public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getMaxDownloads() {
        return maxDownloads;
    }

    public void setMaxDownloads(int maxDownloads) {
        this.maxDownloads = maxDownloads;
    }


}
