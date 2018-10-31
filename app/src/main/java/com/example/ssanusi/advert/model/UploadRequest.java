package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadRequest {
    @Expose @SerializedName("name") String name;
    @Expose @SerializedName("rc") String rc;
    @Expose @SerializedName("category") String category;
    @Expose @SerializedName("phone") String phone;
    @Expose @SerializedName("address") String address;
    @Expose @SerializedName("email") String email;
    @Expose @SerializedName("description") String description;

    public UploadRequest(String name, String rc, String category, String phone, String address, String email, String description) {
        this.name = name;
        this.rc = rc;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
