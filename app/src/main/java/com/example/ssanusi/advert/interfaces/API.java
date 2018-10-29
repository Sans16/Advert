package com.example.ssanusi.advert.interfaces;

import com.example.ssanusi.advert.model.ChangePasswordRequest;
import com.example.ssanusi.advert.model.ChangePasswordResponse;
import com.example.ssanusi.advert.model.LoginRequest;
import com.example.ssanusi.advert.model.LoginResponse;
import com.example.ssanusi.advert.model.RegistrationRequest;
import com.example.ssanusi.advert.model.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface API {
     String BASE_URL = "http://192.168.10.94:7800/api/";
     @POST("register")
     Call<RegistrationResponse> signUpMethod (@Body RegistrationRequest registrationRequest);

     @POST("login")
     Call<LoginResponse> loginMethod (@Body LoginRequest loginRequest);

     @POST("Change-Password")
     Call<ChangePasswordResponse> changePasswordMethod (@Header("Authorization") String token,
                                                        @Body ChangePasswordRequest changePasswordRequest);


}
