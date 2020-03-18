package com.sarihunter.localstores.classes;

public class User {

    private String id;
    String userID;
    private String name;
    private String email;
    private String photoURL;
    private String coverImg;
    private String phone;
    private String address;
    private double earning;
    private int totalProductsNum;
    private int totalProductSelled;
    private int productsInStock;
    private double totalValue;

    public String getCoverImg() {
        return coverImg;
    }

    public User(String id, String userID, String name, String email, String photoURL, String coverImg, String phone, String address, double earning, int totalProductsNum, int totalProductSelled, int productsInStock, double totalValue) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.coverImg = coverImg;
        this.phone = phone;
        this.address = address;
        this.earning = earning;
        this.totalProductsNum = totalProductsNum;
        this.totalProductSelled = totalProductSelled;
        this.productsInStock = productsInStock;
        this.totalValue = totalValue;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public User() {
    }

    public User(String userID, String name, String email, String photoURL, String coverImg, String phone, String address, double earning, int totalProductsNum, int totalProductSelled, int productsInStock, double totalValue) {

        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.phone = phone;
        this.address = address;
        this.earning = earning;
        this.totalProductsNum = totalProductsNum;
        this.totalProductSelled = totalProductSelled;
        this.productsInStock = productsInStock;
        this.totalValue = totalValue;
        this.userID = userID;
        this.coverImg = coverImg;
    }

    public User(String userID, String name, String email, String photoURL, String phone, String address, double earning, int totalProductsNum, int totalProductSelled, int productsInStock, double totalValue) {

        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.phone = phone;
        this.address = address;
        this.earning = earning;
        this.totalProductsNum = totalProductsNum;
        this.totalProductSelled = totalProductSelled;
        this.productsInStock = productsInStock;
        this.totalValue = totalValue;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getEarning() {
        return earning;
    }

    public void setEarning(double earning) {
        this.earning = earning;
    }

    public int getTotalProductsNum() {
        return totalProductsNum;
    }

    public void setTotalProductsNum(int totalProductsNum) {
        this.totalProductsNum = totalProductsNum;
    }

    public int getTotalProductSelled() {
        return totalProductSelled;
    }

    public void setTotalProductSelled(int totalProductSelled) {
        this.totalProductSelled = totalProductSelled;
    }

    public int getProductsInStock() {
        return productsInStock;
    }

    public void setProductsInStock(int productsInStock) {
        this.productsInStock = productsInStock;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", earning=" + earning +
                ", totalProductsNum=" + totalProductsNum +
                ", totalProductSelled=" + totalProductSelled +
                ", productsInStock=" + productsInStock +
                ", totalValue=" + totalValue +
                '}';
    }
}
