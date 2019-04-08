package com.example.arif.barangkonsumen.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arif.barangkonsumen.ApiResponse;
import com.example.arif.barangkonsumen.R;
import com.example.arif.barangkonsumen.customer.MainActivity;
import com.example.arif.barangkonsumen.retrofit.RetrofitBuilder;
import com.example.arif.barangkonsumen.retrofit.RetrofitService;
import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnLogin;

    String tmpUser, tmpPass;

    SharedPreferences mLogin;

    String LOGIN = "log_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.userLogin);
        password = findViewById(R.id.passLogin);
        btnLogin = findViewById(R.id.btnLogin);

        final HashMap<String, String> loginHash = new HashMap<>();

//        Inisialisasi Shared Preference
        mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = mLogin.edit();

        if (mLogin.contains("username")) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpUser = username.getText().toString();
                tmpPass = password.getText().toString();

                if (tmpUser.equals("") || tmpPass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Silahkan masukkan username dan password", Toast.LENGTH_SHORT).show();
                } else {
                    loginHash.put("username", tmpUser);
                    loginHash.put("password", tmpPass);

                    RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
                    Call<ApiResponse> call = retrofitService.login(loginHash);
                    call.enqueue(new Callback<ApiResponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                            if (response.body() != null) {
                                String responseLogin = response.body().getResponse().toString() + response.body().getMetadata().toString();
                                if (response.body().getResponse() instanceof LinkedTreeMap) {
                                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) response.body().getResponse();
                                    editor.putString("username", (String) linkedTreeMap.get("username"));
                                    editor.putString("nik", (String) linkedTreeMap.get("nik"));
                                    editor.putString("nama", (String) linkedTreeMap.get("nama"));
                                    editor.apply();

                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Kombinasi antara username dan password salah", Toast.LENGTH_SHORT).show();
                                    Log.d(LOGIN, responseLogin);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                            Log.d(LOGIN, t.getLocalizedMessage());
                        }
                    });
                }
            }
        });
    }
}
