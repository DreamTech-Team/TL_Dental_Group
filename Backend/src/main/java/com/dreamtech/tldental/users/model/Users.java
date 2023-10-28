package com.dreamtech.tldental.users.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity(name = "Users")
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "student_email_unique", columnNames = "email"))
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "roles", nullable = false)
    private String Roles;

    @Column(name = "fullname", nullable = false)
    private String fullname;   
    
    @Column(name = "phonenumber", length = 10, nullable = false)
    private String phonenumber;

    @Column(name = "address")
    private String address;

    @ToString.Exclude
    @Column(name = "password", nullable = false, columnDefinition = "text")
    private String password;

    @Column(name = "changed", nullable = false)
    private boolean changed = false;

    @Column(name = "create_at")
    private ZonedDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }
}
