package com.getsetgoapp.adapterview;

import com.adoisstudio.helper.JsonList;

public class CourseChildModel {

    String video_id;
    String video_title;
    String duration;
    String is_completed;
    JsonList video_urls;
    JsonList additional_links;
    JsonList additional_files;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(String is_completed) {
        this.is_completed = is_completed;
    }

    public JsonList getVideo_urls() {
        return video_urls;
    }

    public void setVideo_urls(JsonList video_urls) {
        this.video_urls = video_urls;
    }

    public JsonList getAdditional_links() {
        return additional_links;
    }

    public void setAdditional_links(JsonList additional_links) {
        this.additional_links = additional_links;
    }

    public JsonList getAdditional_files() {
        return additional_files;
    }

    public void setAdditional_files(JsonList additional_files) {
        this.additional_files = additional_files;
    }
}
