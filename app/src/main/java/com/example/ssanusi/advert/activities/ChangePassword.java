package com.example.ssanusi.advert.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    @BindView(R.id.defaultPass) TextInputEditText defaultPass;
    @BindView(R.id.newPass) TextInputEditText newPass;
    @BindView(R.id.rNewPass) TextInputEditText rNewPass;
    @BindView(R.id.checkbox) CheckBox checkBox;

    private Unbinder unbinder;
    private String defaultPassword, newPassword, confirmPassword;
    private API api;

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

    public void assignment (){
        defaultPassword = defaultPass.getText().toString().trim();
        newPassword = newPass.getText().toString().trim();
        confirmPassword = rNewPass.getText().toString().trim();
    }

    public boolean validation(){
        assignment();

        if (TextUtils.isEmpty(defaultPassword)){
            defaultPass.setError("The field cannot be empty");
            defaultPass.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(newPassword)){
            newPass.setError("The field cannot be empty");
            newPass.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            rNewPass.setError("The field cannot be empty");
            rNewPass.requestFocus();
            return false;
        }

        if (!TextUtils.equals(newPassword,confirmPassword)){
            newPass.setError("Password does not match");
            rNewPass.setError("Password does not match");
            newPass.requestFocus();
            return false;
        }
        return true;
    }

    @OnClick(R.id.confirm_password_button)
    public void changePassword(){
        if (!validation()) return;
        assignment();
        changePasswordApi(defaultPassword,newPassword,confirmPassword);
    }

    @OnCheckedChanged(R.id.checkbox)
    public void checkBoxCheck(){
        boolean isChecked = checkBox.isChecked();
        if (!isChecked){
            Toast.makeText(getApplicationContext(),"Kindly check the terms and conditions",
                            Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void changePasswordApi(String defaultPassword, String password, String confirmPassword){
       String token = AppPreference.getUserToken();
       api.changePasswordMethod(token,new ChangePasswordRequest(defaultPassword,password,confirmPassword))
               .enqueue(new Callback<ChangePasswordResponse>() {
                   @Override
                   public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                       if (response.isSuccessful()){
                           String message = response.body().getMessage();
                           Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                           Intent i = new Intent(ChangePassword.this, MainActivity.class);
                           startActivity(i);
                           Log.i("TAG","It is reaching the endpoint");
                       }else if (response.code() == 400 || response.code() == 401){
                           Gson gson = new GsonBuilder().create();
                           try {
                               ChangePasswordResponse error = gson.fromJson(response.errorBody().string(), ChangePasswordResponse.class);
                               Toast.makeText(getBaseContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                           } catch (Exception ex) {
                               ex.printStackTrace();
                           }
                           Log.i("TAG","It is reaching the endpoint but an error occured");
                       }
                   }
                   @Override
                   public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                       if (call.isCanceled()) {
                           Log.e("TAG", "request was cancelled");
                       } else {
                           Log.e("TAG", "other larger issue, i.e. no network connection?");
                           Toast.makeText(getBaseContext(), "It failed to reach server",Toast.LENGTH_SHORT).show();
                       }

                       Log.i("TAG","It is not reaching the endpoint");
                   }
               });
    }

}
