package com.getsetgoapp.Model;

import com.adoisstudio.helper.Json;

public class MyPointsModel {

    String points;
    String create_date_text;
    String action_type;
    String description;
    String points_type;
    String username;
    String parent_username;
    String courses;
    Json json;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCreate_date_text() {
        return create_date_text;
    }

    public void setCreate_date_text(String create_date_text) {
        this.create_date_text = create_date_text;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoints_type() {
        return points_type;
    }

    public void setPoints_type(String points_type) {
        this.points_type = points_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParent_username() {
        return parent_username;
    }

    public void setParent_username(String parent_username) {
        this.parent_username = parent_username;
    }

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }
}
