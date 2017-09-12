
package com.shop440.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable{

    @SerializedName("Path")
    @Expose
    private String path;
    @SerializedName("Height")
    @Expose
    private Integer height;
    @SerializedName("Width")
    @Expose
    private Integer width;
    @SerializedName("Placeholder")
    @Expose
    private String placeholder;
    @SerializedName("Thumbnail")
    @Expose
    private String thumbnail;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getPlaceholder() {
        return placeholder.split("data:image/jpeg;base64,")[1];
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
