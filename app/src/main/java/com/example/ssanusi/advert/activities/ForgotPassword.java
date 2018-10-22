package com.example.ssanusi.advert.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.API;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import butterknife.Unbinder;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.emailFPEt) EditText emailFPEt;
    @BindView(R.id.emailFPTL) TextInputLayout emailFPTL;
    @BindView(R.id.forgotPasswordBtn) Button forgotPasswordBtn;
    @BindView(R.id.backBtnFP) ImageView backBtnFP;

    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        unbinder = ButterKnife.bind(this);

    }

    @OnClick(R.id.backBtnFP)
    public void gotoReg(){
        onBackPressed();
    }

    @OnClick(R.id.forgotPasswordBtn)
    public void requestPassword(){
        if(!validate()) return;


        // TODO Check for internet connection
        // TODO connect to the server to check for password
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public boolean validate(){
        String email = emailFPEt.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailFPTL.setError("Invalid email");
            emailFPEt.requestFocus();
        }
        return true;
    }

    @OnTextChanged(R.id.emailFPEt)
    public void checkField(){
        String email = emailFPEt.getText().toString().trim();
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
}
