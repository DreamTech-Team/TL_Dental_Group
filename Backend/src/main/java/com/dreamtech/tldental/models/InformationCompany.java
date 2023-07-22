package com.dreamtech.tldental.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="InformationCompany")
public class InformationCompany {
    @Id
    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = true, length = 1000)
    private String content;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        createAt = LocalDateTime.now();
    }

    public InformationCompany() {
    }

    public InformationCompany(String title, String content, String type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

    @Override
    public String toString() {
        return "InformationCompany{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
