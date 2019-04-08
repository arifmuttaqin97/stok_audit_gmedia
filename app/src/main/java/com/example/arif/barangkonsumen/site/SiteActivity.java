package com.example.arif.barangkonsumen.site;

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

public class SiteActivity extends AppCompatActivity {

    TextView testSite;
    ListView listSite;

    SiteResponseData site;
    ArrayList<SiteResponseData> arrayCustomerSite;

    SiteAdapter siteAdapter;

    HashMap<String, String> hashCustomerSite;

    Integer startIndex = 0;
    Integer count = 15;

    String SITE = "log_site";

    Map<String, String> headerMap = new HashMap<>();

    SharedPreferences mLogin;

    ProgressBar progressBar;

    String customer;
    String nama_customer;

    SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        testSite = findViewById(R.id.textSite);
        listSite = findViewById(R.id.listSite);
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
        headerMap.put("Timestamp", "11017201850101101");
        headerMap.put("Signature", "FWCcb1F1hAq+Q4/J3gJt4v6pgM5L2oKbW/KmWywfUDE=");

        customer = getIntent().getStringExtra("Customer");
        nama_customer = getIntent().getStringExtra("Nama_Customer");

        testSite.setText(nama_customer);
    }

    void getSite(final String search, final String params) {
        arrayCustomerSite = new ArrayList<>();
        hashCustomerSite = new HashMap<>();

        hashCustomerSite.put("start", "0");
        hashCustomerSite.put("count", "10");
        hashCustomerSite.put("search", search);
        hashCustomerSite.put("customer_id", params);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.customerSite(headerMap, hashCustomerSite);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    String responseSite = response.body().getResponse().toString() + response.body().getMetadata().toString();
                    if (response.body().getResponse() instanceof List) {
                        List list = (List) response.body().getResponse();

                        for (Object object : list) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                            site = new SiteResponseData(
                                    (String) linkedTreeMap.get("id_site"),
                                    (String) linkedTreeMap.get("service_id"),
                                    (String) linkedTreeMap.get("nama_site"),
                                    (String) linkedTreeMap.get("alamat_site"),
                                    (String) linkedTreeMap.get("kota_site"),
                                    (String) linkedTreeMap.get("telp_site")
                            );
                            arrayCustomerSite.add(site);
                        }
                        listSite.setAdapter(null);
                        siteAdapter = new SiteAdapter(SiteActivity.this, arrayCustomerSite);
                        listSite.setAdapter(siteAdapter);
                        listSite.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                if (view.getLastVisiblePosition() == totalItemCount - 1 && listSite.getCount() > (count - 1)) {
                                    startIndex += count;
                                    getMore(search, params);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SiteActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d(SITE, responseSite);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(SITE, t.getLocalizedMessage());
            }
        });
    }

    void getMore(String searchMore, String params) {
        arrayCustomerSite = new ArrayList<>();
        hashCustomerSite = new HashMap<>();

        hashCustomerSite.put("start", startIndex.toString());
        hashCustomerSite.put("count", count.toString());
        hashCustomerSite.put("search", searchMore);
        hashCustomerSite.put("customer_id", params);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.customerSite(headerMap, hashCustomerSite);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    String responseSite = response.body().getResponse().toString() + response.body().getMetadata().toString();
                    if (response.body().getResponse() instanceof List) {
                        List list = (List) response.body().getResponse();

                        for (Object object : list) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                            site = new SiteResponseData(
                                    (String) linkedTreeMap.get("id_site"),
                                    (String) linkedTreeMap.get("service_id"),
                                    (String) linkedTreeMap.get("nama_site"),
                                    (String) linkedTreeMap.get("alamat_site"),
                                    (String) linkedTreeMap.get("kota_site"),
                                    (String) linkedTreeMap.get("telp_site")
                            );
                            arrayCustomerSite.add(site);
                        }

                        if (siteAdapter != null) {
                            siteAdapter.addMore(arrayCustomerSite);
                        } else {
                            Log.d(SITE, responseSite);
                            Toast.makeText(SiteActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SiteActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d(SITE, responseSite);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(SITE, t.getLocalizedMessage());
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
                getSite(s, customer);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getSite(s, customer);
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
        getSite("", customer);
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

    //    void getData(){
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("start","0");
//            jsonObject.put("count","10");
//            jsonObject.put("search","");
//            jsonObject.put("customer_id","03.0019.0810");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ApiVolley apiVolley = new ApiVolley(SiteActivity.this, jsonObject, "POST", RetrofitBuilder.GET_SITE, new ApiVolley.VolleyCallback() {
//            @Override
//            public void onSuccess(String result) {
//                try {
//                    JSONObject response = new JSONObject(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(String result) {
//
//            }
//        });
//    }
}
