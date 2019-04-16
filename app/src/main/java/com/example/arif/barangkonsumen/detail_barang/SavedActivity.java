package com.example.arif.barangkonsumen.detail_barang;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arif.barangkonsumen.ApiResponse;
import com.example.arif.barangkonsumen.R;
import com.example.arif.barangkonsumen.list_barang.MasterBarangActivity;
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

public class SavedActivity extends AppCompatActivity {

    private final Integer count = 15;
    private final String SAVE = "log_save";
    private final Map<String, String> headerMap = new HashMap<>();
    private TextView testSaved;
    private ListView listSaved;
    private FloatingActionButton btnAdd;
    private DetailBarangResponseData detailBarang;
    private ArrayList<DetailBarangResponseData> arrayDetailBarang;
    private DetailBarangAdapter detailBarangAdapter;
    private HashMap<String, String> hashDetailBarang;
    private Integer startIndex = 0;
    private SharedPreferences mLogin;

    private ProgressBar progressBar;

    private String site;
    private String nama_site;

    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        testSaved = findViewById(R.id.textSaved);
        listSaved = findViewById(R.id.listSaved);
        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.loading);

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
//        headerMap.put("Timestamp", "11017201850101101");
//        headerMap.put("Signature", "FWCcb1F1hAq+Q4/J3gJt4v6pgM5L2oKbW/KmWywfUDE=");

        site = getIntent().getStringExtra("Site");
        nama_site = getIntent().getStringExtra("Nama_Site");
        testSaved.setText(nama_site);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SavedActivity.this, MasterBarangActivity.class);
                i.putExtra("id_lokasi", site);
                i.putExtra("nama_site", nama_site);
                startActivity(i);
            }
        });
    }

    private void getDetailBarang(final String search, final String params) {
        arrayDetailBarang = new ArrayList<>();
        hashDetailBarang = new HashMap<>();

        hashDetailBarang.put("start", "0");
        hashDetailBarang.put("count", "15");
        hashDetailBarang.put("search", search);
        hashDetailBarang.put("id_lokasi", params);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailBarang(headerMap, hashDetailBarang);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseSave = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();

                            for (Object object : list) {
                                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                                detailBarang = new DetailBarangResponseData(
                                        (String) linkedTreeMap.get("id_barang"),
                                        (String) linkedTreeMap.get("id_lokasi"),
                                        (String) linkedTreeMap.get("nama_barang"),
                                        (String) linkedTreeMap.get("total_serial")
                                );
                                arrayDetailBarang.add(detailBarang);
                            }
                            listSaved.setAdapter(null);
                            detailBarangAdapter = new DetailBarangAdapter(SavedActivity.this, arrayDetailBarang);
                            listSaved.setAdapter(detailBarangAdapter);
                            listSaved.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {

                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                    if (view.getLastVisiblePosition() == totalItemCount - 1 && listSaved.getCount() > (count - 1)) {
                                        startIndex += count;
                                        getMore(search, params);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SavedActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            Log.d(SAVE, responseSave);
                        }
                    } else {
                        Toast.makeText(SavedActivity.this, "Terjadi kesalahan : " + response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                        Log.d(SAVE, response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(SAVE, t.getLocalizedMessage());
            }
        });
    }

    private void getMore(String search, String params) {
        arrayDetailBarang = new ArrayList<>();
        hashDetailBarang = new HashMap<>();

        hashDetailBarang.put("start", startIndex.toString());
        hashDetailBarang.put("count", count.toString());
        hashDetailBarang.put("search", search);
        hashDetailBarang.put("id_lokasi", params);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailBarang(headerMap, hashDetailBarang);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseSave = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();

                            for (Object object : list) {
                                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                                detailBarang = new DetailBarangResponseData(
                                        (String) linkedTreeMap.get("id_barang"),
                                        (String) linkedTreeMap.get("id_lokasi"),
                                        (String) linkedTreeMap.get("nama_barang"),
                                        (String) linkedTreeMap.get("total_serial")
                                );
                                arrayDetailBarang.add(detailBarang);
                            }

                            if (detailBarangAdapter != null) {
                                detailBarangAdapter.addMore(arrayDetailBarang);
                            } else {
                                Log.d(SAVE, responseSave);
                                Toast.makeText(SavedActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(SavedActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            Log.d(SAVE, responseSave);
                        }
                    } else {
                        Toast.makeText(SavedActivity.this, "Terjadi kesalahan : " + response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                        Log.d(SAVE, response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(SAVE, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getDetailBarang(s, site);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getDetailBarang(s, site);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        getDetailBarang("", site);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
