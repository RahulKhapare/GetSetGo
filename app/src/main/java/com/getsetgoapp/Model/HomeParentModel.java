package com.getsetgoapp.Model;

import com.adoisstudio.helper.JsonList;

public class HomeParentModel {

    String category_name;
    String category_slug;
    JsonList courses;

    public String getCategory_slug() {
        return category_slug;
    }

    public void setCategory_slug(String category_slug) {
        this.category_slug = category_slug;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public JsonList getCourses() {
        return courses;
    }

    public void setCourses(JsonList courses) {
        this.courses = courses;
    }
}
