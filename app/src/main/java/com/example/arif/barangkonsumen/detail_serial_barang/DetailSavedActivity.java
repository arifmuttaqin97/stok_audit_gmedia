package com.example.arif.barangkonsumen.detail_serial_barang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arif.barangkonsumen.ApiResponse;
import com.example.arif.barangkonsumen.R;
import com.example.arif.barangkonsumen.retrofit.RetrofitBuilder;
import com.example.arif.barangkonsumen.retrofit.RetrofitService;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSavedActivity extends AppCompatActivity {

    private final Map<String, String> headerMap = new HashMap<>();
    private final Integer count = 15;
    private final String SERIAL = "log_serial";
    private TextView namaBarang;
    private ListView listSerial;
    private ProgressBar progressBar;
    private SharedPreferences mLogin;
    private DetailSerialResponseData serial;
    private ArrayList<DetailSerialResponseData> arraySerial;
    private HashMap<String, String> hashSerial;
    private DetailSerialAdapter serialAdapter;
    private Integer startIndex = 0;
    private String id_lokasi;
    private String id_barang;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_saved);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        namaBarang = findViewById(R.id.testNamaBarang);
        listSerial = findViewById(R.id.listSerial);
        progressBar = findViewById(R.id.loading);

        namaBarang.setText(getIntent().getStringExtra("nama_barang"));
        id_lokasi = getIntent().getStringExtra("id_lokasi");
        id_barang = getIntent().getStringExtra("id_barang");

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
    }

    private void getSerial() {
        arraySerial = new ArrayList<>();
        hashSerial = new HashMap<>();

        hashSerial.put("start", "0");
        hashSerial.put("count", "15");
        hashSerial.put("id_lokasi", id_lokasi);
        hashSerial.put("id_barang", id_barang);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailSerial(headerMap, hashSerial);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    String responseSerial = response.body().getResponse().toString() + response.body().getMetadata().toString();
                    if (response.body().getResponse() instanceof List) {
                        List list = (List) response.body().getResponse();

                        for (Object object : list) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                            serial = new DetailSerialResponseData(
                                    (String) linkedTreeMap.get("id_serial"),
                                    (String) linkedTreeMap.get("id_barang"),
                                    (String) linkedTreeMap.get("id_lokasi"),
                                    (String) linkedTreeMap.get("nama_barang"),
                                    (String) linkedTreeMap.get("serial")
                            );
                            arraySerial.add(serial);
                        }
                        listSerial.setAdapter(null);
                        serialAdapter = new DetailSerialAdapter(DetailSavedActivity.this, arraySerial);
                        listSerial.setAdapter(serialAdapter);
                        listSerial.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                if (view.getLastVisiblePosition() == totalItemCount - 1 && listSerial.getCount() > (count - 1)) {
                                    startIndex += count;
                                    getMore();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(DetailSavedActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d(SERIAL, responseSerial);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(SERIAL, t.getLocalizedMessage());
            }
        });
    }

    private void getMore() {
        arraySerial = new ArrayList<>();
        hashSerial = new HashMap<>();

        hashSerial.put("start", startIndex.toString());
        hashSerial.put("count", count.toString());
        hashSerial.put("id_lokasi", id_lokasi);
        hashSerial.put("id_barang", id_barang);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailSerial(headerMap, hashSerial);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    String responseSerial = response.body().getResponse().toString() + response.body().getMetadata().toString();
                    if (response.body().getResponse() instanceof List) {
                        List list = (List) response.body().getResponse();

                        for (Object object : list) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                            serial = new DetailSerialResponseData(
                                    (String) linkedTreeMap.get("id_serial"),
                                    (String) linkedTreeMap.get("id_barang"),
                                    (String) linkedTreeMap.get("id_lokasi"),
                                    (String) linkedTreeMap.get("nama_barang"),
                                    (String) linkedTreeMap.get("serial")
                            );
                            arraySerial.add(serial);
                        }

                        if (serialAdapter != null) {
                            serialAdapter.addMore(arraySerial);
                        } else {
                            Log.d(SERIAL, responseSerial);
                            Toast.makeText(DetailSavedActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DetailSavedActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d(SERIAL, responseSerial);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(SERIAL, t.getLocalizedMessage());
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

    @Override
    protected void onResume() {
        getSerial();
        super.onResume();
    }
}
