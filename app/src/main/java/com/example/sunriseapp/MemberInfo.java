package com.example.sunriseapp;

import android.widget.EditText;

public class MemberInfo {
  private  String name;
  private  String phoneNumber;
  private  String birthDate;
  private  String address;
  private  String photoUri;
  public MemberInfo(String name,String phoneNumber,String birthDate,String address,String photoUri){
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.birthDate = birthDate;
    this.address = address;
    this.photoUri = photoUri;

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
  public String getPhotoUri() {
    return photoUri;
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

  public void setPhotoUri(String photoUri) {
    this.photoUri = photoUri;
  }
}
