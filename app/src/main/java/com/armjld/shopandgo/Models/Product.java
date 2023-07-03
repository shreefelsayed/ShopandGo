package com.armjld.shopandgo.Models;

public class Product {
    String id = "";
    String name = "";
    String imageLink = "";
    int price = 0;
    String category = "";

    public Product() {
    }

    public Product(String id, String name, String imageLink, int price, String category) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.price = price;
        this.category = category;
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
