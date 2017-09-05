package com.shop440.Models;

/**
 * Created by mmumene on 03/09/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Analytics {

    @SerializedName("Stores")
    @Expose
    private Integer stores;
    @SerializedName("Purchases")
    @Expose
    private Integer purchases;
    @SerializedName("Likes")
    @Expose
    private Integer likes;

    public Integer getStores() {
        return stores;
    }

    public void setStores(Integer stores) {
        this.stores = stores;
    }

    public Integer getPurchases() {
        return purchases;
    }

    public void setPurchases(Integer purchases) {
        this.purchases = purchases;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

}