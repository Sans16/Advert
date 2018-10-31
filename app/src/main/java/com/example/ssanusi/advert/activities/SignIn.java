package com.example.ssanusi.advert.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssanusi.advert.MainActivity;
import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.API;
import com.example.ssanusi.advert.model.LoginRequest;
import com.example.ssanusi.advert.model.LoginResponse;
import com.example.ssanusi.advert.model.RegistrationRequest;
import com.example.ssanusi.advert.retrofit.RetrofitClass;
import com.example.ssanusi.advert.utilities.AppPreference;
import com.example.ssanusi.advert.utilities.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.emailSIEt)EditText emailSIET;
    @BindView(R.id.passwordSIEt)EditText passwordSIET;
    @BindView(R.id.checkBoxSI)CheckBox checkBoxSI;
    @BindView(R.id.forgotPasswordSI)TextView forgotPasswordSI;
    @BindView(R.id.regBtnSI)TextView regBtnSI;
    @BindView(R.id.signInBtn)Button signInBtn;
    @BindView(R.id.emailSITL)TextInputLayout emailSITL;
    @BindView(R.id.passwordSITL)TextInputLayout passwordSITL;
    private Unbinder unbinder;
    private API api;
    private Call<LoginResponse>call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        unbinder = ButterKnife.bind(this);
        api = RetrofitClass.initialize();
        forgotPasswordSI.setOnClickListener(this);
        regBtnSI.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * This is created to stop the retrofit call on pause
     */
    @Override
    protected void onPause() {
        if (call!=null){
            call.cancel();
            call = null;
        }
        super.onPause();
    }

    /**
     * This is to enable and disable the sign in button
     */

    @OnTextChanged({R.id.emailSIEt,R.id.passwordSIEt})
    public void checkEmptyFields(){
        String check_email = emailSIET.getText().toString().trim();
        String check_password = passwordSIET.getText().toString().trim();
        if (TextUtils.isEmpty(check_email)|| TextUtils.isEmpty(check_password)){
            signInBtn.setEnabled(false);
        }else{
            signInBtn.setEnabled(true);
            signInBtn.setBackgroundColor(Color.BLACK);
            signInBtn.setTextColor(Color.WHITE);
            //regBtn.setBackground(getResources().getDrawable(R.drawable.dark_black_sign_in));
        }
    }

    /**
     * This is to keep the user logged in
     */
    @OnCheckedChanged(R.id.checkBoxSI)
    public void checkChanged(){
        boolean isCheck = checkBoxSI.isChecked();
        if (isCheck){
            AppPreference.setIsLoggedIn(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgotPasswordSI:
                Intent intent = new Intent(SignIn.this,ForgotPassword.class);
                startActivity(intent);
                break;

            case R.id.regBtnSI:
                intent = new Intent(SignIn.this,Registration.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * This is to clear the error set before
     * @return status : clear or not clear
     */
    @OnTouch({R.id.emailSIEt,R.id.passwordSIEt})
    public boolean clearError(View view){
       switch (view.getId()){
            case R.id.emailSIEt:
                emailSITL.setError(null);
                break;

           case R.id.passwordSIEt:
               passwordSITL.setError(null);
               break;
       }
        return  false;
    }

    @OnClick(R.id.signInBtn)
    public void signInBtnClicked(){
        if (!validate()) return;
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Snackbar.make(findViewById(android.R.id.content),"Please check your connection",Snackbar.LENGTH_SHORT).show();
            return;
        }
        // TODO Progress Dialog to show progress
        String email = emailSIET.getText().toString().trim();
        String password = passwordSIET.getText().toString().trim();
        call = api.loginMethod(new LoginRequest(email,password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    String token = response.body().getToken();
                    RegistrationRequest userDetails = response.body().getData();
                    AppPreference.setUserToken(token);
                    Log.i("TAG",token);
                    AppPreference.setUserDetails(userDetails);
                    Toast.makeText(getApplicationContext(),"login successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        LoginResponse error = gson.fromJson(response.errorBody().string(), LoginResponse.class);
                        Toast.makeText(getBaseContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.i("TAG",error.getMessage()+" ");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.i("TAG","It is reaching the end point but not successful");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i("TAG","It is not reaching the endpoint");
                if (call.isCanceled()) {
                    Log.e("TAG", "request was cancelled");
                } else {
                    Log.e("TAG", "other larger issue, i.e. no network connection?");
                    Toast.makeText(getBaseContext(), "unable to perform operation, try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validate(){
        String email;
        email = emailSIET.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailSITL.setError("Invalid Email");
            emailSIET.requestFocus();
            return false;
        }
        return true;
    }
}
