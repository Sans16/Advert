package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @Expose @SerializedName("status") String status;
    @Expose @SerializedName("message") String message;
    @Expose @SerializedName("token") String token;
    @Expose @SerializedName("data") RegistrationRequest data;

    public LoginResponse(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }
    public RegistrationRequest getData() {return data;}
    public void setData(RegistrationRequest data) {this.data = data;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
}
