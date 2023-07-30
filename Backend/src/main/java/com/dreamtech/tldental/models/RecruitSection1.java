package com.dreamtech.tldental.models;

public class RecruitSection1 {
    ContentPage subItem1;
    ContentPage subItem2;
    ContentPage image1;
    ContentPage image2;

    public RecruitSection1() {
    }

    public RecruitSection1(ContentPage subItem1, ContentPage subItem2, ContentPage image1, ContentPage image2) {
        this.subItem1 = subItem1;
        this.subItem2 = subItem2;
        this.image1 = image1;
        this.image2 = image2;
    }

    public ContentPage getSubItem1() {
        return subItem1;
    }

    public void setSubItem1(ContentPage subItem1) {
        this.subItem1 = subItem1;
    }

    public ContentPage getSubItem2() {
        return subItem2;
    }

    public void setSubItem2(ContentPage subItem2) {
        this.subItem2 = subItem2;
    }

    public ContentPage getImage1() {
        return image1;
    }

    public void setImage1(ContentPage image1) {
        this.image1 = image1;
    }

    public ContentPage getImage2() {
        return image2;
    }

    public void setImage2(ContentPage image2) {
        this.image2 = image2;
    }
}
