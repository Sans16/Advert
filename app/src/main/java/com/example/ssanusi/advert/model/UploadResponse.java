package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadResponse {
    @SerializedName("status")  boolean status;
    @SerializedName("message") List<String> message;
    @Expose @SerializedName("error") String error;

    public UploadResponse(List<String> message, boolean status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status; }
    public void setStatus(boolean status) {
        this.status = status; }
}
