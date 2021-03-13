package com.getsetgoapp.Model;

import com.adoisstudio.helper.Json;

public class MyPointsModel {

    String amount;
    String create_date_text;
    String income_type;
    Json json;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreate_date_text() {
        return create_date_text;
    }

    public void setCreate_date_text(String create_date_text) {
        this.create_date_text = create_date_text;
    }

    public String getIncome_type() {
        return income_type;
    }

    public void setIncome_type(String income_type) {
        this.income_type = income_type;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }
}
