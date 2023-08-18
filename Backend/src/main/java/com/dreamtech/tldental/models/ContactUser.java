package com.dreamtech.tldental.models;

import com.dreamtech.tldental.utils.Utils;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name="ContactUser")
public class ContactUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 100)
    private String fullname;

    @Column(length = 150)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 500, nullable = true)
    private String content;

    @Column(nullable = false)
    private boolean isContacted;

    @Column(name = "create_at")
    private ZonedDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public ContactUser() {
    }

    public ContactUser(String fullname, String email, String phone, String content, boolean isContacted) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.content = content;
        this.isContacted = isContacted;
    }

    @Override
    public String toString() {
        return "ContactUser{" +
                "id='" + id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                ", isContacted=" + isContacted +
                ", createAt=" + createAt +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isContacted() {
        return isContacted;
    }

    public void setContacted(boolean contacted) {
        isContacted = contacted;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }
}
