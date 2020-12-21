package com.getsetgo.Model;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;

public class CountryCode {

  private String code;
  private String name;
  private String dialCode;




  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
    if (TextUtils.isEmpty(name)) {
      name = new Locale("", code).getDisplayName();
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDialCode() {
    return dialCode;
  }

  public void setDialCode(String dialCode) {
    this.dialCode = dialCode;
  }


}
