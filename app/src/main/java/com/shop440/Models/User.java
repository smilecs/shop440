package com.shop440.Models;

import java.io.Serializable;

/**
 * Created by SMILECS on 12/27/16.
 */

public class User implements Serializable{
    public String phone;
    public String image;
    public String fullname;
    public String passcode;
    public String Likes;
    public String Stores;
    public String Purchases;

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getStores() {
        return Stores;
    }

    public void setStores(String stores) {
        Stores = stores;
    }

    public String getPurchases() {
        return Purchases;
    }

    public void setPurchases(String purchases) {
        Purchases = purchases;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return fullname;
    }
}
