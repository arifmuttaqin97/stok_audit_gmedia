package id.net.gmedia.stokaudit.login;

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

import id.net.gmedia.stokaudit.ApiResponse;
import id.net.gmedia.stokaudit.R;
import id.net.gmedia.stokaudit.customer.MainActivity;
import id.net.gmedia.stokaudit.retrofit.RetrofitBuilder;
import id.net.gmedia.stokaudit.retrofit.RetrofitService;
import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final String LOGIN = "log_login";
    private EditText username;
    private EditText password;
    private Button btnLogin;
    private String tmpUser;
    private String tmpPass;
    private SharedPreferences mLogin;

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
                                if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
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
                                        Toast.makeText(LoginActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                        Log.d(LOGIN, responseLogin);
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                                    Log.d(LOGIN, response.body().getMetadata().get("message"));
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
