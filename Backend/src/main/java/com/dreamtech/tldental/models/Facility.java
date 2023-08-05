package com.dreamtech.tldental.models;
import jakarta.persistence.*;

// create a mysql entity model for facility with address, phonenumber, hotline, image, map_link
@Entity
@Table(name="Facility")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 10000)
    private String address;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false, length = 11)
    private String hotline;

    @Column(nullable = false, length = 500)
    private String image;

    @Column(nullable = false, length = 500)
    private String mapLink;

    public Facility() {
    }

    public Facility(String id, String address, String phoneNumber, String hotline, String image, String mapLink) {
        this.id = id;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.hotline = hotline;
        this.image = image;
        this.mapLink = mapLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }
}