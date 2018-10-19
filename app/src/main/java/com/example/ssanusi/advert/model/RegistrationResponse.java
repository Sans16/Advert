package com.example.ssanusi.advert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {
    @Expose @SerializedName("status") boolean status;
    @Expose @SerializedName("message") String message;
    @Expose @SerializedName("data") Data data;

    private class Data {
        private RegistrationRequest registrationRequest;
        private String updated_at;
        private String created_at;
        public int id;
        public RegistrationRequest getRegistrationRequest() {return registrationRequest;}
        public void setRegistrationRequest(RegistrationRequest registrationRequest) {
            this.registrationRequest = registrationRequest;}
        public String getUpdated_at() {return updated_at;}
        public void setUpdated_at(String updated_at) {this.updated_at = updated_at;}
        public String getCreated_at() {return created_at;}
        public void setCreated_at(String created_at) {this.created_at = created_at; }
        public int getId() {return id;}
        public void setId(int id) {this.id = id;}
    }
}
