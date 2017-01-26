package com.shop440.Models;

import java.io.Serializable;

/**
 * Created by SMILECS on 1/6/17.
 */

public class StoreModel implements Serializable {
    public String Owner, Slug, Name, Description, Specialisation, Phone, Address, CitySlug, City, State, Country, Coordinates, Logo, Price, Category, Tags;
    public String OwnerSlug, Image, Placeholder, Purchases, Likes, ProductsNumber;

    public String getPurchases() {
        return Purchases;
    }

    public void setPurchases(String purchases) {
        Purchases = purchases;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getProductsNumber() {
        return ProductsNumber;
    }

    public void setProductsNumber(String productsNumber) {
        ProductsNumber = productsNumber;
    }

    public String getPlaceholder() {
        return Placeholder;
    }

    public void setPlaceholder(String placeholder) {
        Placeholder = placeholder;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getOwnerSlug() {
        return OwnerSlug;
    }

    public void setOwnerSlug(String ownerSlug) {
        OwnerSlug = ownerSlug;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getSlug() {
        return Slug;
    }

    public void setSlug(String slug) {
        Slug = slug;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        Specialisation = specialisation;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCitySlug() {
        return CitySlug;
    }

    public void setCitySlug(String citySlug) {
        CitySlug = citySlug;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(String coordinates) {
        Coordinates = coordinates;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    @Override
    public String toString() {
        return getOwner();
    }
}
