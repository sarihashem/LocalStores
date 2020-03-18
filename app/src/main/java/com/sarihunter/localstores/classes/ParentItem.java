package com.sarihunter.localstores.classes;

import java.util.List;

public class ParentItem {

    List<Items> items;
    String title;

    public ParentItem(List<Items> items, String title) {
        this.items = items;
        this.title = title;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
