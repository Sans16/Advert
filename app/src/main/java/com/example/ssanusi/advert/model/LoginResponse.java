package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @Expose @SerializedName("status") String status;
    @Expose @SerializedName("message") String message;
    @Expose @SerializedName("token") String token;

    public LoginResponse(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
}
