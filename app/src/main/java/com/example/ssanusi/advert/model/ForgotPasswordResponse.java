package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponse {
    @Expose @SerializedName("status") String status;
    @Expose @SerializedName("message") String message;

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}


}
