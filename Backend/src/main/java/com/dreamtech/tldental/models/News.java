package com.dreamtech.tldental.models;

import com.dreamtech.tldental.utils.Utils;
import jakarta.persistence.*;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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

    @Column(name = "detail_mobile", length = 5000)
    private String detailMobile;

    private int highlight;

    @Column(name = "create_at")
    private ZonedDateTime createAt;

    @ManyToMany
    @JoinTable(
            name = "news_tags",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    private Set<Tags> tags = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        this.slug = Utils.generateSlug(title);
    }

    @PreUpdate
    protected void preUpdate() {
        this.slug = Utils.generateSlug(title);
    }

    public News() {
    }

    public News(String title, String img, String summary, String detail, String detailMobile, int highlight) {
        this.title = title;
        this.img = img;
        this.summary = summary;
        this.detail = detail;
        this.detailMobile = detailMobile;
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
                ", detailMobile='" + detailMobile + '\'' +
                ", highlight=" + highlight +
                ", tags=" + tags +
                ", createAt=" + createAt +
                '}';
    }

    public Set<Tags> getTags() {
        return tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetailMobile() {
        return detailMobile;
    }

    public void setDetailMobile(String detailMobile) {
        this.detailMobile = detailMobile;
    }

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }
}
