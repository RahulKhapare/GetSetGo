package com.getsetgo.Model;

import com.adoisstudio.helper.JsonList;

public class CourseModuleModel {

    String module_title;
    boolean clickFlag;

    public String getModule_title() {
        return module_title;
    }

    public void setModule_title(String module_title) {
        this.module_title = module_title;
    }

    public boolean isClickFlag() {
        return clickFlag;
    }

    public void setClickFlag(boolean clickFlag) {
        this.clickFlag = clickFlag;
    }
}
