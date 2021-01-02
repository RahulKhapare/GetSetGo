package com.getsetgo.Model;

import android.text.TextUtils;

import java.util.Locale;

public class CourseListModel {
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCourse_name() {
    return course_name;
  }

  public void setCourse_name(String course_name) {
    this.course_name = course_name;
  }

  private String id;
  private String course_name;



}
