
package com.shop440.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Page implements Serializable{

    @SerializedName("Prev")
    @Expose
    private Boolean prev;
    @SerializedName("PrevVal")
    @Expose
    private Integer prevVal;
    @SerializedName("Next")
    @Expose
    private Boolean next;
    @SerializedName("NextVal")
    @Expose
    private Integer nextVal;
    @SerializedName("NextURL")
    @Expose
    private String nextURL;
    @SerializedName("Pages")
    @Expose
    private List<String> pages = null;
    @SerializedName("Total")
    @Expose
    private Integer total;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Skip")
    @Expose
    private Integer skip;

    public Boolean getPrev() {
        return prev;
    }

    public void setPrev(Boolean prev) {
        this.prev = prev;
    }

    public Integer getPrevVal() {
        return prevVal;
    }

    public void setPrevVal(Integer prevVal) {
        this.prevVal = prevVal;
    }

    public Boolean getNext() {
        return next;
    }

    public void setNext(Boolean next) {
        this.next = next;
    }

    public Integer getNextVal() {
        return nextVal;
    }

    public void setNextVal(Integer nextVal) {
        this.nextVal = nextVal;
    }

    public String getNextURL() {
        return nextURL;
    }

    public void setNextURL(String nextURL) {
        this.nextURL = nextURL;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

}
