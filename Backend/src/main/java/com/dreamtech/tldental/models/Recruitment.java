package com.dreamtech.tldental.models;

import java.time.LocalDateTime;

import com.dreamtech.tldental.utils.Utils;

import jakarta.persistence.*;

@Entity
@Table(name="Recruitment")
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 10000, unique = false)
    private String title;

    @Column(columnDefinition = "TEXT", unique = false)
    private String position;

    @Column(columnDefinition = "TEXT", unique = false)
    private String location;

    @Column(columnDefinition = "TEXT", unique = false)
    private String department;

    @Column(columnDefinition = "TEXT", unique = false)
    private String working_form;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String benefit;

    @Column(columnDefinition = "TEXT")
    private String contact;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Column(unique = true)
    private String slug;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        this.slug = Utils.generateSlug(title);
    }

    @PreUpdate
    protected void preUpdate() {
        this.slug = Utils.generateSlug(title);
    }

    @Override
    public String toString() {
        return "Recruitment{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", position='" + position + '\'' +
                ", location='" + location + '\'' +
                ", department='" + department + '\'' +
                ", working_form='" + working_form + '\'' +
                ", description='" + description + '\'' +
                ", requirements='" + requirements + '\'' +
                ", benefit='" + benefit + '\'' +
                ", contact='" + contact + '\'' +
                ", treatment='" + treatment + '\'' +
                ", slug='" + slug + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public Recruitment() {
    }

    public Recruitment(String id, String title, String position, String location, String department,
            String working_form, String description, String requirements, String benefit, String contact,
            String treatment, String slug, LocalDateTime createAt) {
        this.id = id;
        this.title = title;
        this.position = position;
        this.location = location;
        this.department = department;
        this.working_form = working_form;
        this.description = description;
        this.requirements = requirements;
        this.benefit = benefit;
        this.contact = contact;
        this.treatment = treatment;
        this.slug = slug;
        this.createAt = createAt;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getWorking_form() {
        return working_form;
    }

    public void setWorking_form(String working_form) {
        this.working_form = working_form;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    
}