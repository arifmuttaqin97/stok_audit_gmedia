package com.example.arif.barangkonsumen.customer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import com.example.arif.barangkonsumen.login.LoginActivity;
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

public class MainActivity extends AppCompatActivity {

    private final Integer count = 15;
    private final String CUSTOMER = "log_customer";
    private final Map<String, String> headerMap = new HashMap<>();
    private ListView listMain;
    private HashMap<String, String> hashCustomer;
    private CustomerResponseData customer;
    private ArrayList<CustomerResponseData> arrayCustomer;
    private CustomerAdapter customerAdapter;
    private Integer startIndex = 0;
    private SharedPreferences mLogin;

    private SharedPreferences.Editor editor;

    private ProgressBar progressBar;

    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listMain = findViewById(R.id.listMain);
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
    }

    private void getCustomer(final String search) {
        arrayCustomer = new ArrayList<>();
        hashCustomer = new HashMap<>();

        hashCustomer.put("start", "0");
        hashCustomer.put("count", "15");
        hashCustomer.put("search", search);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.customer(headerMap, hashCustomer);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    String responseCustomer = response.body().getResponse().toString() + response.body().getMetadata().toString();
                    if (response.body().getResponse() instanceof List) {
                        List list = (List) response.body().getResponse();

                        for (Object object : list) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                            customer = new CustomerResponseData(
                                    (String) linkedTreeMap.get("customer_id"),
                                    (String) linkedTreeMap.get("customer_name"),
                                    (String) linkedTreeMap.get("alamat")
                            );
                            arrayCustomer.add(customer);
                        }
                        listMain.setAdapter(null);
                        customerAdapter = new CustomerAdapter(MainActivity.this, arrayCustomer);
                        listMain.setAdapter(customerAdapter);
                        listMain.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                if (view.getLastVisiblePosition() == totalItemCount - 1 && listMain.getCount() > (count - 1)) {
                                    startIndex += count;
                                    getMore(search);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d(CUSTOMER, responseCustomer);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(CUSTOMER, t.getLocalizedMessage());
            }
        });
    }

    private void getMore(String searchMore) {
        arrayCustomer = new ArrayList<>();
        hashCustomer = new HashMap<>();

        hashCustomer.put("start", startIndex.toString());
        hashCustomer.put("count", count.toString());
        hashCustomer.put("search", searchMore);

        progressBar.setVisibility(View.VISIBLE);

        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.customer(headerMap, hashCustomer);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    String responseCustomer = response.body().getResponse().toString() + response.body().getMetadata().toString();
                    if (response.body().getResponse() instanceof List) {
                        List list = (List) response.body().getResponse();

                        for (Object object : list) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

                            customer = new CustomerResponseData(
                                    (String) linkedTreeMap.get("customer_id"),
                                    (String) linkedTreeMap.get("customer_name"),
                                    (String) linkedTreeMap.get("alamat")
                            );
                            arrayCustomer.add(customer);
                        }

                        if (customerAdapter != null) {
                            customerAdapter.addMore(arrayCustomer);
                        } else {
                            Log.d(CUSTOMER, responseCustomer);
                            Toast.makeText(MainActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d(CUSTOMER, responseCustomer);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(CUSTOMER, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_with_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getCustomer(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getCustomer(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                editor = mLogin.edit();
                editor.remove("username");
                editor.apply();

                Toast.makeText(this, "Anda telah logout dari aplikasi ini", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        getCustomer("");
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
