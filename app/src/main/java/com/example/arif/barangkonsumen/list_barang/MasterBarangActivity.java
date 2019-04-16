package com.example.arif.barangkonsumen.list_barang;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

public class MasterBarangActivity extends AppCompatActivity {

    private final Integer count = 15;
    private final String MASTER = "log_master";
    private final Map<String, String> headerMap = new HashMap<>();
    private ListView listMaster;
    private ListBarangResponseData listBarang;
    private ArrayList<ListBarangResponseData> arrayListBarang;
    private ListBarangAdapter listBarangAdapter;
    private HashMap<String, String> hashListBarang;
    private Integer startIndex = 0;
    private String id_lokasi = "";
    private String nama_site = "";
    private SharedPreferences mLogin;

    private ProgressBar progressBar;

    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_barang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listMaster = findViewById(R.id.listMaster);
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

        id_lokasi = getIntent().getStringExtra("id_lokasi");
        nama_site = getIntent().getStringExtra("nama_site");
    }

    private void getMaster(final String search) {
        arrayListBarang = new ArrayList<>();
        hashListBarang = new HashMap<>();

        hashListBarang.put("start", "0");
        hashListBarang.put("count", "15");
        hashListBarang.put("search", search);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.listBarang(headerMap, hashListBarang);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseMaster = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();

                            for (Object object : list) {
                                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                                listBarang = new ListBarangResponseData(
                                        (String) linkedTreeMap.get("id_header"),
                                        (String) linkedTreeMap.get("nama_barang"),
                                        (String) linkedTreeMap.get("merk"),
                                        (String) linkedTreeMap.get("type"),
                                        (String) linkedTreeMap.get("kode"),
                                        (String) linkedTreeMap.get("id_ukuran"),
                                        (String) linkedTreeMap.get("qty"),
                                        (String) linkedTreeMap.get("qty_small"),
                                        (String) linkedTreeMap.get("min_stock"),
                                        (String) linkedTreeMap.get("max_stock"),
                                        (String) linkedTreeMap.get("harga_jual"),
                                        (String) linkedTreeMap.get("id_aktif"),
                                        (String) linkedTreeMap.get("gambar"),
                                        (String) linkedTreeMap.get("keterangan"),
                                        (String) linkedTreeMap.get("user_input"),
                                        (String) linkedTreeMap.get("tanggal_input"),
                                        (String) linkedTreeMap.get("user_update"),
                                        (String) linkedTreeMap.get("tanggal_update"),
                                        (String) linkedTreeMap.get("flag"),
                                        (String) linkedTreeMap.get("id_kategori"),
                                        (String) linkedTreeMap.get("id_tipe"),
                                        (String) linkedTreeMap.get("id_satuan_kecil")
                                );
                                arrayListBarang.add(listBarang);
                            }
                            listMaster.setAdapter(null);
                            listBarangAdapter = new ListBarangAdapter(MasterBarangActivity.this, arrayListBarang, id_lokasi, nama_site);
                            listMaster.setAdapter(listBarangAdapter);
                            listMaster.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {

                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                    if (view.getLastVisiblePosition() == totalItemCount - 1 && listMaster.getCount() > (count - 1)) {
                                        startIndex += count;
                                        getMore(search);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MasterBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            Log.d(MASTER, responseMaster);
                        }
                    } else {
                        Toast.makeText(MasterBarangActivity.this, "Terjadi kesalahan : " + response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                        Log.d(MASTER, response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(MASTER, t.getLocalizedMessage());
            }
        });
    }

    private void getMore(String search) {
        arrayListBarang = new ArrayList<>();
        hashListBarang = new HashMap<>();

        hashListBarang.put("start", startIndex.toString());
        hashListBarang.put("count", count.toString());
        hashListBarang.put("search", search);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.listBarang(headerMap, hashListBarang);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseMaster = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();

                            for (Object object : list) {
                                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                                listBarang = new ListBarangResponseData(
                                        (String) linkedTreeMap.get("id_header"),
                                        (String) linkedTreeMap.get("nama_barang"),
                                        (String) linkedTreeMap.get("merk"),
                                        (String) linkedTreeMap.get("type"),
                                        (String) linkedTreeMap.get("kode"),
                                        (String) linkedTreeMap.get("id_ukuran"),
                                        (String) linkedTreeMap.get("qty"),
                                        (String) linkedTreeMap.get("qty_small"),
                                        (String) linkedTreeMap.get("min_stock"),
                                        (String) linkedTreeMap.get("max_stock"),
                                        (String) linkedTreeMap.get("harga_jual"),
                                        (String) linkedTreeMap.get("id_aktif"),
                                        (String) linkedTreeMap.get("gambar"),
                                        (String) linkedTreeMap.get("keterangan"),
                                        (String) linkedTreeMap.get("user_input"),
                                        (String) linkedTreeMap.get("tanggal_input"),
                                        (String) linkedTreeMap.get("user_update"),
                                        (String) linkedTreeMap.get("tanggal_update"),
                                        (String) linkedTreeMap.get("flag"),
                                        (String) linkedTreeMap.get("id_kategori"),
                                        (String) linkedTreeMap.get("id_tipe"),
                                        (String) linkedTreeMap.get("id_satuan_kecil")
                                );
                                arrayListBarang.add(listBarang);
                            }
                            if (listBarangAdapter != null) {
                                listBarangAdapter.addMore(arrayListBarang);
                            } else {
                                Toast.makeText(MasterBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                Log.d(MASTER, responseMaster);
                            }
                        } else {
                            Toast.makeText(MasterBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            Log.d(MASTER, responseMaster);
                        }
                    } else {
                        Toast.makeText(MasterBarangActivity.this, "Terjadi kesalahan : " + response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                        Log.d(MASTER, response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(MASTER, t.getLocalizedMessage());
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
                getMaster(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getMaster(s);
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
        getMaster("");
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
