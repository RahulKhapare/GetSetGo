package com.getsetgoapp.Model;

import com.adoisstudio.helper.JsonList;

public class CourseModuleModel {

    String id;
    String module_name;
    JsonList videos;
    boolean clickFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public JsonList getVideos() {
        return videos;
    }

    public void setVideos(JsonList videos) {
        this.videos = videos;
    }

    public boolean isClickFlag() {
        return clickFlag;
    }

    public void setClickFlag(boolean clickFlag) {
        this.clickFlag = clickFlag;
    }
}
