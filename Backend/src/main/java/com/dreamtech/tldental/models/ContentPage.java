package com.dreamtech.tldental.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="ContentPage")
public class ContentPage {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(length = 1000, unique = false)
    private String title;

    @Lob
    @Column(columnDefinition="TEXT")
    private String content;

    @Column(columnDefinition="TEXT")
    private String image;

    @Column(unique = true)
    private String slug;

    @Column(length = 200, unique = false)
    private String type;

    public ContentPage(String id, String title, String content, String image, String slug, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.slug = slug;
        this.type = type;
    }
}