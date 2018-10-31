package com.example.ssanusi.advert.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ssanusi.advert.MainActivity;
import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.API;
import com.example.ssanusi.advert.model.ChangePasswordRequest;
import com.example.ssanusi.advert.model.ChangePasswordResponse;
import com.example.ssanusi.advert.retrofit.RetrofitClass;
import com.example.ssanusi.advert.utilities.AppPreference;
import com.example.ssanusi.advert.utilities.NetworkUtil;
import com.example.ssanusi.advert.utilities.TokenConstructApi;
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

public class ChangePassword extends AppCompatActivity {
    @BindView(R.id.defaultPass)
    TextInputEditText defaultPass;
    @BindView(R.id.newPass)
    TextInputEditText newPass;
    @BindView(R.id.rNewPass)
    TextInputEditText rNewPass;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.default_password_layout)
    TextInputLayout defaultPassTL;
    @BindView(R.id.new_password_layout)
    TextInputLayout newPassTL;
    @BindView(R.id.re_new_password_layout)
    TextInputLayout rNewPassTL;

    private Unbinder unbinder;
    private String defaultPassword, newPassword, confirmPassword;
    private API api;
    private boolean isChecked;
    private Call<ChangePasswordResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        unbinder = ButterKnife.bind(this);
        api = RetrofitClass.initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onPause() {
        if (call != null) {
            call.cancel();
            call = null;
        }
        super.onPause();
    }

    public void assignment() {
        defaultPassword = defaultPass.getText().toString().trim();
        newPassword = newPass.getText().toString().trim();
        confirmPassword = rNewPass.getText().toString().trim();
    }

    public boolean validation() {
        assignment();

        if (TextUtils.isEmpty(defaultPassword)) {
            defaultPassTL.setError("The field cannot be empty");
            defaultPassTL.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(newPassword)) {
            newPassTL.setError("The field cannot be empty");
            newPassTL.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            rNewPassTL.setError("The field cannot be empty");
            rNewPassTL.requestFocus();
            return false;
        }

        if (!TextUtils.equals(newPassword, confirmPassword)) {
            newPassTL.setError("Password does not match");
            rNewPassTL.setError("Password does not match");
            newPassTL.requestFocus();
            return false;
        }

        if (!isChecked) {
            Toast.makeText(getApplicationContext(), "Kindly check the terms and conditions",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.confirm_password_button)
    public void changePassword() {
        if (!validation()) return;
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Snackbar.make(findViewById(android.R.id.content), "Please check your connection", Snackbar.LENGTH_SHORT).show();
            return;
        }
        // TODO progress dialog
        assignment();
        String token = AppPreference.getUserToken();
        call = api.changePasswordMethod(token, new ChangePasswordRequest(defaultPassword, newPassword, confirmPassword));
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ChangePassword.this, MainActivity.class);
                    startActivity(i);
                    Log.i("TAG", "It is reaching the endpoint");
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    try {
                        ChangePasswordResponse error = gson.fromJson(response.errorBody().string(), ChangePasswordResponse.class);
                        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.i("TAG", "error 400");
                } else if (response.code() == 401) {
                    Gson gson = new GsonBuilder().create();
                    try {
                        ChangePasswordResponse error = gson.fromJson(response.errorBody().string(), ChangePasswordResponse.class);
                        Toast.makeText(getBaseContext(), error.getError(), Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.i("TAG", "error 401");
                } else if (response.code() == 500) {
                    Toast.makeText(getBaseContext(), "unable to perform operation, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.e("TAG", "request was cancelled");
                } else {
                    Log.e("TAG", "other larger issue, i.e. no network connection?");
                    Toast.makeText(getBaseContext(), "It failed to reach server", Toast.LENGTH_SHORT).show();
                }

                Log.i("TAG", "It is not reaching the endpoint");
            }
        });
    }

    @OnCheckedChanged(R.id.checkbox)
    public void checkBoxCheck() {
        isChecked = checkBox.isChecked();
        if (!isChecked) {
            Toast.makeText(getApplicationContext(), "Kindly check the terms and conditions",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * This is to clear the error message displayed on the text input layout
     *
     * @param view
     */
    @OnTouch({R.id.defaultPass, R.id.newPass, R.id.rNewPass})
    public boolean clearError(View view) {
        switch (view.getId()){
            case  R.id.defaultPass:
                defaultPassTL.setError(null);
                break;
            case  R.id.newPass:
                newPassTL.setError(null);
                break;
            case  R.id.rNewPass:
                rNewPassTL.setError(null);
                break;
        }
        return false;
    }
}
