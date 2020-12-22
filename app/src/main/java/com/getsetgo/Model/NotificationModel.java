package com.getsetgo.Model;

public class NotificationModel {

    private int viewType;

    public NotificationModel(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
