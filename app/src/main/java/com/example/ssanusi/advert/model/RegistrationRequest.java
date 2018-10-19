package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @Expose @SerializedName("firstname") String firstname;
    @Expose @SerializedName("lastname") String lastname;
    @Expose @SerializedName("email") String email;
    @Expose @SerializedName("phonenumber") String phonenumber;

    public RegistrationRequest(String firstname, String lastname, String email, String phonenumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public String getFirstname() {return firstname;}
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public String getLastname() {return lastname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPhonenumber() {return phonenumber;}
    public void setPhonenumber(String phonenumber) {this.phonenumber = phonenumber;}
}
