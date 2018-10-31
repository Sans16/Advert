package com.example.ssanusi.advert.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ssanusi.advert.MainActivity;
import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.API;
import com.example.ssanusi.advert.model.ChangePasswordResponse;
import com.example.ssanusi.advert.model.RegistrationRequest;
import com.example.ssanusi.advert.model.RegistrationResponse;
import com.example.ssanusi.advert.retrofit.RetrofitClass;
import com.example.ssanusi.advert.utilities.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

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

public class Registration extends AppCompatActivity {
    @BindView(R.id.fnRegEt)
    EditText fn;
    @BindView(R.id.lnRegEt)
    EditText ln;
    @BindView(R.id.emailRegEt)
    EditText email;
    @BindView(R.id.pnRegEt)
    EditText pn;
    @BindView(R.id.regBtn)
    Button regBtn;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.pnRegTL)
    TextInputLayout pnRegTL;
    @BindView(R.id.emailRegTL)
    TextInputLayout emailRegTL;

    private String firstName, lastName, emailAdd, phone;
    private API api;
    private Unbinder unbinder;
    private Call<RegistrationResponse>call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        unbinder = ButterKnife.bind(this);
        assignment();
        api = RetrofitClass.initialize();
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validation()) return;
                if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    Snackbar.make(findViewById(android.R.id.content),"Please check your connection",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                sendDetails(firstName, lastName, emailAdd, phone);
            }
        });
    }

    /**
     * This is to cancel retrofit call when user moves away from the application
     */
    @Override
    protected void onPause() {
        if (call!=null){
            call.cancel();
            call = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void assignment() {
        firstName = fn.getText().toString().trim();
        lastName = ln.getText().toString().trim();
        emailAdd = email.getText().toString().trim();
        phone = pn.getText().toString().trim();
    }

    public void sendDetails(String fn, String ln, String email, String pn) {
        // TODO progress dialog for the sign in progress
        call = api.signUpMethod(new RegistrationRequest(fn, ln, email, pn));
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                Log.i("Tag", "it is reaching the endpoint");
                if (response.isSuccessful()){
                    showDialog();
                }
                else if (response.code() == 400){
                    Gson gson = new GsonBuilder().create();
                    try {
                        ChangePasswordResponse error = gson.fromJson(response.errorBody().string(), ChangePasswordResponse.class);
                        Toast.makeText(getBaseContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.i("TAG","error 400");
                }
                else if (response.code() == 500){
                    Toast.makeText(getBaseContext(), "unable to perform operation, try again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Log.i("Tag", "it is not reaching the endpoint" + t.getMessage());
                if (call.isCanceled()) {
                    Log.e("TAG", "request was cancelled");
                } else {
                    Log.e("TAG", "other larger issue, i.e. no network connection?");
                    Toast.makeText(getBaseContext(), "unable to perform operation, try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This is the dialog to take user to hie/her mail
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation").setMessage("Check your mail for your password")
                .setPositiveButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (emailAdd.contains("@gmail.com")) {
                            //TODO set an intent to go to gmail using a webview
                        }
                        if (emailAdd.contains("@yahoo.com")) {
                            // TODO set an intent to go to yahoo using a webview
                        } else {
                            /*
                            Since yahoo and gmail have been solved for others should go to gmail as
                            default
                             */
                            //TODO set an intent to go to gmail using a webview
                        }
//                        Intent intent = new Intent(Registration.this, MainActivity.class);
//                        startActivity(intent);
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

    public boolean validation() {
        assignment();
        if (TextUtils.isEmpty(firstName)) {
            fn.setError("The Field is required");
            fn.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            ln.setError("The Field is required");
            ln.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(emailAdd) || !Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()) {
            emailRegTL.setError("Invalid email address");
            email.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || !isNumberValid("234", phone)) {
            pnRegTL.setError("Phone number not valid");
            pn.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isNumberValid(String countryCode, String contact) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        boolean isValid = false;
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(contact, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    /**
     * This is the method that enable and disable the register button
     */
    @OnTextChanged({R.id.fnRegEt, R.id.lnRegEt, R.id.emailRegEt, R.id.pnRegEt})
    public void checkFields() {
        String check_first = fn.getText().toString().trim();
        String check_last = ln.getText().toString().trim();
        String check_email = email.getText().toString().trim();
        String check_mobile = pn.getText().toString().trim();

        if (TextUtils.isEmpty(check_first) || TextUtils.isEmpty(check_last)
                || TextUtils.isEmpty(check_email) || TextUtils.isEmpty(check_mobile)) {
            //continueBtn.setTextColor(getResources().getColor(R.color.btn_inactive_text_color));
            //continueBtn.setBackground(getResources().getDrawable(R.drawable.btn_inactive));
            regBtn.setEnabled(false);
        } else {
            regBtn.setTextColor(Color.WHITE);
            regBtn.setBackground(getResources().getDrawable(R.drawable.dark_black));
            regBtn.setEnabled(true);
        }
    }

    @OnClick(R.id.loginBtn)
    public void goToSignInPage() {
        Intent intent = new Intent(Registration.this, SignIn.class);
        startActivity(intent);
    }

    @OnTouch({R.id.emailRegEt, R.id.pnRegEt})
    public boolean clearError(View view) {
        switch (view.getId()) {
            case R.id.emailRegEt:
                emailRegTL.setError(null);
                break;
            case R.id.pnRegEt:
                pnRegTL.setError(null);
                break;
            default:
                break;
        }
        return false;
    }
}
