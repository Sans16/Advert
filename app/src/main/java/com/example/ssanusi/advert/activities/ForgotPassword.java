package com.example.ssanusi.advert.activities;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.API;
import com.example.ssanusi.advert.model.ForgotPasswordRequest;
import com.example.ssanusi.advert.model.ForgotPasswordResponse;
import com.example.ssanusi.advert.retrofit.RetrofitClass;
import com.example.ssanusi.advert.utilities.AppPreference;
import com.example.ssanusi.advert.utilities.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    @BindView(R.id.emailFPEt) EditText emailFPEt;
    @BindView(R.id.emailFPTL) TextInputLayout emailFPTL;
    @BindView(R.id.forgotPasswordBtn) Button forgotPasswordBtn;
    @BindView(R.id.backBtnFP) ImageView backBtnFP;
    Call<ForgotPasswordResponse> call;

    private Unbinder unbinder;
    private API api;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        unbinder = ButterKnife.bind(this);
        api = RetrofitClass.initialize();
    }

    @Override
    protected void onPause() {
        if (call != null){
            call.cancel();
            call = null;
        }
        super.onPause();
    }

    @OnClick(R.id.backBtnFP)
    public void gotoReg(){
        onBackPressed();
    }

    @OnClick(R.id.forgotPasswordBtn)
    public void requestPassword(){
        if(!validate()) return;
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Snackbar.make(findViewById(android.R.id.content),"Please check your connection",Snackbar.LENGTH_SHORT).show();
            return;
        }
        Log.i("TAG","clicked");
        email = emailFPEt.getText().toString().trim();
        call = api.forgotPasswordMethod(new ForgotPasswordRequest(email));
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful()){
                    Log.i("TAG","it is successful");
                    Toast.makeText(getBaseContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
                else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ForgotPasswordResponse error;
                    try {
                        error = gson.fromJson(response.errorBody().string(), ForgotPasswordResponse.class);
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                        Log.i("TAG","it is successful but an error occurred");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (response.code() == 500){
                    Toast.makeText(getBaseContext(), "unable to perform operation, try again",Toast.LENGTH_SHORT).show();
                    Log.i("TAG","Internal server error");
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.e("TAG", "request was cancelled");
                } else {
                    Log.e("TAG", "other larger issue, i.e. no network connection?");
                    Toast.makeText(getBaseContext(), "unable to perform operation, try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public boolean validate(){
         email = emailFPEt.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailFPTL.setError("Invalid email");
            emailFPEt.requestFocus();
        }
        return true;
    }

    @OnTextChanged(R.id.emailFPEt)
    public void checkField(){
        email = emailFPEt.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            forgotPasswordBtn.setEnabled(false);
        }else{
            forgotPasswordBtn.setEnabled(true);
        }
    }

    @OnTouch(R.id.emailFPEt)
    public boolean clearError(View view){
        if (view.getId() == R.id.emailFPEt){
            emailFPTL.setError(null);
        }
        return false;
    }

    public void changePassword(String email){

    }
}
