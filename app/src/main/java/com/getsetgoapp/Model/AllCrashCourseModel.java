package com.getsetgoapp.Model;

import com.adoisstudio.helper.JsonList;

public class AllCrashCourseModel {

    String text;
    JsonList crash_courses;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JsonList getCrash_courses() {
        return crash_courses;
    }

    public void setCrash_courses(JsonList crash_courses) {
        this.crash_courses = crash_courses;
    }
}
