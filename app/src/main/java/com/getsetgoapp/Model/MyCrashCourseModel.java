package com.getsetgoapp.Model;

import com.adoisstudio.helper.JsonList;

public class MyCrashCourseModel {

   String id;
   String instructor_id;
   String name;
   String slug;
   String program_date;
   String program_end_date;
   String program_time;
   String program_end_time;
   String session;
   String price;
   String mlm_price;
   String category_name;
   String skill_level;
   String language;
   String share_url;
   int is_purchased;
   JsonList instructors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstructor_id() {
        return instructor_id;
    }

    public void setInstructor_id(String instructor_id) {
        this.instructor_id = instructor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getProgram_date() {
        return program_date;
    }

    public void setProgram_date(String program_date) {
        this.program_date = program_date;
    }

    public String getProgram_end_date() {
        return program_end_date;
    }

    public void setProgram_end_date(String program_end_date) {
        this.program_end_date = program_end_date;
    }

    public String getProgram_time() {
        return program_time;
    }

    public void setProgram_time(String program_time) {
        this.program_time = program_time;
    }

    public String getProgram_end_time() {
        return program_end_time;
    }

    public void setProgram_end_time(String program_end_time) {
        this.program_end_time = program_end_time;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMlm_price() {
        return mlm_price;
    }

    public void setMlm_price(String mlm_price) {
        this.mlm_price = mlm_price;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSkill_level() {
        return skill_level;
    }

    public void setSkill_level(String skill_level) {
        this.skill_level = skill_level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getIs_purchased() {
        return is_purchased;
    }

    public void setIs_purchased(int is_purchased) {
        this.is_purchased = is_purchased;
    }

    public JsonList getInstructors() {
        return instructors;
    }

    public void setInstructors(JsonList instructors) {
        this.instructors = instructors;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
