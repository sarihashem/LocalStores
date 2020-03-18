package com.sarihunter.localstores.classes;

public class Items {
    private String id;
    private String name;
    private String description;
    private String owner;
    private String ownerID;
    private String datePublished;
    private String photoUrl;
    private String barcode;
    private int rate;
    private double price;
    private String category;
    private String tag;
    private int quantity;

    public Items() {

    }

    public Items(String id, String name, String description, String owner, String ownerID, String datePublished, String photoUrl, String barcode, int rate, double price, String category, String tag, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.ownerID = ownerID;
        this.datePublished = datePublished;
        this.photoUrl = photoUrl;
        this.barcode = barcode;
        this.rate = rate;
        this.price = price;
        this.category = category;
        this.tag = tag;
        this.quantity = quantity;
    }

    public Items(String id, String name, String description, String owner, String ownerID, String datePublished, String photoUrl, String barcode, double price, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.ownerID = ownerID;
        this.datePublished = datePublished;
        this.photoUrl = photoUrl;
        this.barcode = barcode;
        this.price = price;
        this.category = category;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Items(String id, String name, String description, String owner, String datePublished, String photoUrl, String barcode, int rate, double price, String category, String tag, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.datePublished = datePublished;
        this.photoUrl = photoUrl;
        this.barcode = barcode;
        this.rate = rate;
        this.price = price;
        this.category = category;
        this.tag = tag;
        this.quantity = quantity;
    }

    public Items(String id, String name, String description, String owner, String datePublished, String photoUrl, String barcode, double price, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.datePublished = datePublished;
        this.photoUrl = photoUrl;
        this.barcode = barcode;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
