package com.example.ssanusi.advert.interfaces;

import com.example.ssanusi.advert.activities.ForgotPassword;
import com.example.ssanusi.advert.model.ChangePasswordRequest;
import com.example.ssanusi.advert.model.ChangePasswordResponse;
import com.example.ssanusi.advert.model.ForgotPasswordRequest;
import com.example.ssanusi.advert.model.ForgotPasswordResponse;
import com.example.ssanusi.advert.model.LoginRequest;
import com.example.ssanusi.advert.model.LoginResponse;
import com.example.ssanusi.advert.model.RegistrationRequest;
import com.example.ssanusi.advert.model.RegistrationResponse;
import com.example.ssanusi.advert.model.UploadResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {
     //The base URL the app is hosted
     String BASE_URL = "http://192.168.10.94:7800/api/";

     //This is the endpoint to register
     @POST("register")
     Call<RegistrationResponse> signUpMethod (@Body RegistrationRequest registrationRequest);

     //This is the endpoint to login
     @POST("login")
     Call<LoginResponse> loginMethod (@Body LoginRequest loginRequest);

     //This is the endpoint to change password
     @POST("change-password")
     Call<ChangePasswordResponse> changePasswordMethod (@Header("Authorization") String token,
                                                        @Body ChangePasswordRequest changePasswordRequest);

     //This is the endpoint to rest password
     @POST("reset-password")
     Call<ForgotPasswordResponse> forgotPasswordMethod (@Body ForgotPasswordRequest forgotPasswordRequest);

     @Multipart
     @POST("register-company")
     Call<UploadResponse> sendFiles(@Header("Authorization") String token,
                                    @Part("name") RequestBody name,
                                    @Part("rc") RequestBody rc,
                                    @Part("category") RequestBody category,
                                    @Part("phone") RequestBody phone,
                                    @Part("address") RequestBody address,
                                    @Part("email") RequestBody email,
                                    @Part("description") RequestBody description,
                                    @Part MultipartBody.Part file);
}
