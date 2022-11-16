package com.example.sunriseapp;

import android.widget.EditText;

public class MemberInfo {
  private  String name;
  private  String phoneNumber;
  private  String birthDate;
  private  String address;
  public MemberInfo(String name,String phoneNumber,String birthDate,String address){
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.birthDate = birthDate;
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public String getAddress() {
    return address;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
