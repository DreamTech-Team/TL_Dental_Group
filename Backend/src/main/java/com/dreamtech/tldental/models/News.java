package com.dreamtech.tldental.models;

import jakarta.persistence.*;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Table(name="News")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200, unique = true)
    private String title;

    @Column(nullable = false)
    private String img;

    @Column(unique = true)
    private String slug;

    @Column(length = 300)
    private String summary;

    @Column(length = 10000)
    private String detail;

    private boolean highlight;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        generateSlug();
    }

    @PreUpdate
    protected void generateSlug() {
        String slug = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        this.slug = slug.toLowerCase(Locale.getDefault())
                .replaceAll("\\s+", "-");
    }

    public News() {
    }

    public News(String title, String summary, String detail, boolean highlight) {
        this.title = title;
        this.summary = summary;
        this.detail = detail;
        this.highlight = highlight;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getSlug() {
        return slug;
    }

    public String getSummary() {
        return summary;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", slug='" + slug + '\'' +
                ", summary='" + summary + '\'' +
                ", detail='" + detail + '\'' +
                ", highlight=" + highlight +
                ", createAt=" + createAt +
                '}';
    }
}
