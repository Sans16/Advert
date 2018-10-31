package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest {
    @Expose @SerializedName("email") String email;
    public ForgotPasswordRequest(String email) {
        this.email = email;
    }
}
