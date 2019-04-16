package com.example.arif.barangkonsumen.detail_gambar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arif.barangkonsumen.ApiResponse;
import com.example.arif.barangkonsumen.R;
import com.example.arif.barangkonsumen.retrofit.RetrofitBuilder;
import com.example.arif.barangkonsumen.retrofit.RetrofitService;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGambarActivity extends AppCompatActivity {

    private final Map<String, String> headerMap = new HashMap<>();
    private final HashMap<String, String> hashMap = new HashMap<>();
    private final String GAMBAR = "log_detailGambar";
    private TextView detailGambar;
    private RecyclerView rvGambar;
    private SharedPreferences mLogin;
    private String id_serial;
    private DetailGambarAdapter detailGambarAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gambar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        detailGambar = findViewById(R.id.testDetailGambar);
        rvGambar = findViewById(R.id.rvGambar);

        mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (mLogin.contains("username") && mLogin.contains("nik")) {
            headerMap.put("User-Name", Objects.requireNonNull(mLogin.getString("username", "akbar")));
            headerMap.put("User-Id", Objects.requireNonNull(mLogin.getString("nik", "03.121.2017")));
        } else {
            headerMap.put("User-Name", "akbar");
            headerMap.put("User-Id", "03.121.2017");
        }

        headerMap.put("Client-Service", "gmedia-stok-audit");
        headerMap.put("Auth-Key", "gmedia");
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Timestamp", "11017201850101101");
        headerMap.put("Signature", "FWCcb1F1hAq+Q4/J3gJt4v6pgM5L2oKbW/KmWywfUDE=");

        id_serial = getIntent().getStringExtra("id_serial");

        hashMap.put("id_serial", id_serial);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGambar.setLayoutManager(layoutManager);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailGambar(headerMap, hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null) {
                    String responseDetailGambar = response.body().getResponse().toString() + response.body().getMetadata().toString();
                    if (response.body().getResponse() instanceof LinkedTreeMap) {
                        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) response.body().getResponse();
                        detailGambar.setText((String) linkedTreeMap.get("nama_barang"));

                        ArrayList arrayList = (ArrayList) linkedTreeMap.get("data_image");
                        assert arrayList != null;

                        ArrayList<String> arrayList1 = new ArrayList<>();

                        for (Object object : arrayList) {
                            LinkedTreeMap linkedTreeMap1 = (LinkedTreeMap) object;
                            String tmpGambar = (String) linkedTreeMap1.get("image");
                            arrayList1.add(tmpGambar);
                        }
                        detailGambarAdapter = new DetailGambarAdapter(arrayList1);
                        rvGambar.setAdapter(detailGambarAdapter);
                    } else {
                        Toast.makeText(DetailGambarActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d(GAMBAR, responseDetailGambar);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(GAMBAR, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
