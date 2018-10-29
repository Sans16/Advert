package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest {
    @Expose @SerializedName("old_password") String oldPassword;
    @Expose @SerializedName("password") String Password;
    @Expose @SerializedName("password_confirmation") String PasswordConfirmation;

    public ChangePasswordRequest(String oldPassword, String password, String passwordConfirmation) {
        this.oldPassword = oldPassword;
        Password = password;
        PasswordConfirmation = passwordConfirmation;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasswordConfirmation() {
        return PasswordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        PasswordConfirmation = passwordConfirmation;
    }
}
