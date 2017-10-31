package com.shop440.Models;

import java.io.Serializable;

/**
 * Created by SMILECS on 1/24/17.
 */

public class CategoryModel implements Serializable {
    public String Name, Slug;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSlug() {
        return Slug;
    }

    public void setSlug(String slug) {
        Slug = slug;
    }

    @Override
    public String toString() {
        return getName();
    }
}