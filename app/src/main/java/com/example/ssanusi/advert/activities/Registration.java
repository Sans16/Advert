package com.example.ssanusi.advert.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.ssanusi.advert.MainActivity;
import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.API;
import com.example.ssanusi.advert.model.RegistrationRequest;
import com.example.ssanusi.advert.model.RegistrationResponse;
import com.example.ssanusi.advert.retrofit.RetrofitClass;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {
    private EditText fn,ln,email,pn;
    String firstName, lastName, emailAdd, phone;
    Button regBtn;
    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        assignment();
        api = RetrofitClass.initialize();
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validation()) return;
                sendDetails(firstName,lastName,emailAdd,phone);
            }
        });
    }

    public void init (){
        fn = findViewById(R.id.fnRegEt);
        ln = findViewById(R.id.lnRegEt);
        email = findViewById(R.id.emailRegEt);
        pn = findViewById(R.id.pnRegEt);
        regBtn = findViewById(R.id.regBtn);
    }

    public void assignment (){
        init();
        firstName = fn.getText().toString().trim();
        lastName = ln.getText().toString().trim();
        emailAdd = email.getText().toString().trim();
        phone = pn.getText().toString().trim();
    }

    public void sendDetails (String fn, String ln, String email , String pn){

        api.signUpMethod(new RegistrationRequest(fn,ln,email,pn)).enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                Log.i("Tag","it is reaching the endpoint");
               showDialog();
            }
            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Log.i("Tag","it is not reaching the endpoint"+t.getMessage());
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation").setMessage("Check your mail for your password")
                .setPositiveButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.show();
    }

    public boolean validation (){
        assignment();
        if (TextUtils.isEmpty(firstName))
        {
            fn.setError("The Field is required");
            fn.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(lastName)){
            ln.setError("The Field is required");
            ln.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(emailAdd) || !Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()){
            email.setError("Invalid email address");
            email.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || !isNumberValid("234",phone)){
            pn.setError("Phone number not valid");
            pn.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isNumberValid(String countryCode, String contact){
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        boolean isValid = false;
        try{
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(contact,isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return isValid;
    }
}
