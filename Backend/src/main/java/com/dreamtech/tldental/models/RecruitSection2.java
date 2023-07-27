package com.dreamtech.tldental.models;

import java.util.Optional;

public class RecruitSection2 {
    ContentPage mainItem;
    Optional<ContentPage[]> subItem;
    Optional<String[]> deletedSubItem;

    public RecruitSection2(ContentPage mainItem, Optional<ContentPage[]> subItem, Optional<String[]> deletedSubItem) {
        this.mainItem = mainItem;
        this.subItem = subItem;
        this.deletedSubItem = deletedSubItem;
    }

    public ContentPage getMainItem() {
        return mainItem;
    }

    public void setMainItem(ContentPage mainItem) {
        this.mainItem = mainItem;   
    }

    public Optional<ContentPage[]> getSubItem() {
        return subItem;
    }

    public void setSubItem(Optional<ContentPage[]> subItem) {
        this.subItem = subItem;
    }

    public Optional<String[]> getDeletedSubItem() {
        return deletedSubItem;
    }

    public void setDeletedSubItem(Optional<String[]> deletedSubItem) {
        this.deletedSubItem = deletedSubItem;
    }

}
