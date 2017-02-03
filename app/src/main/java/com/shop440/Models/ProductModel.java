package com.shop440.Models;

import java.io.Serializable;

/**
 * Created by SMILECS on 1/24/17.
 */

public class ProductModel implements Serializable {
    public String Slug;
    public String Name;
    public String Description;
    public String Price;
    public String Category;
    public String Image;
    public String CitySlug;
    public String City;
    public String State;
    public String Country;
    public String Coordinates;
    public String OwnerSlug;
    public String Specialisation;
    public String Owner;
    public String OwnerLogo;

    public String getOwnerLogo() {
        return OwnerLogo;
    }

    public void setOwnerLogo(String ownerLogo) {
        OwnerLogo = ownerLogo;
    }

    public String Placeholder;

    public String getTagsText() {
        return TagsText;
    }

    public void setTagsText(String tagsText) {
        TagsText = tagsText;
    }

    public String TagsText;

    public String getPlaceholder() {
        return Placeholder;
    }

    public void setPlaceholder(String placeholder) {
        Placeholder = placeholder;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
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

    public String getOwnerSlug() {
        return OwnerSlug;
    }

    public void setOwnerSlug(String ownerSlug) {
        OwnerSlug = ownerSlug;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        Specialisation = specialisation;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String shop;
    public String[] Tags, RawImages;

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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String[] getTags() {
        return Tags;
    }

    public void setTags(String[] tags) {
        Tags = tags;
    }

    public String[] getRawImages() {
        return RawImages;
    }

    public void setRawImages(String[] rawImages) {
        RawImages = rawImages;
    }

    @Override
    public String toString() {
        return getName();
    }
}
