package id.net.gmedia.stokaudit.data_barang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.net.gmedia.stokaudit.ApiResponse;
import id.net.gmedia.stokaudit.R;
import id.net.gmedia.stokaudit.detail_barang.SavedActivity;
import id.net.gmedia.stokaudit.retrofit.RetrofitBuilder;
import id.net.gmedia.stokaudit.retrofit.RetrofitService;
import com.fxn.pix.Pix;
import com.google.gson.internal.LinkedTreeMap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataBarangActivity extends AppCompatActivity {

    private final ArrayList<String> arrayList = new ArrayList<>();
    private final ArrayList<HashMap<String, Integer>> idResponse = new ArrayList<>();
    private final Map<String, String> headerMap = new HashMap<>();
    private final HashMap<String, Object> hashTambahBarang = new HashMap<>();
    private final String DATA_BARANG = "log_dataBarang";
    private TextView testDataBarang;
    private TextView testBarcode;
    private Button btnSave;
    private RelativeLayout btnBarcode;
    private RelativeLayout btnPicture;
    private String barcodes = "";
    private RecyclerView rvGambar;
    private GambarAdapter gambarAdapter;
    private String kodeBarang = "";
    private SharedPreferences mLogin;

    //    maxSize = ukuran pixel maksimal
    private static Bitmap resizeBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        testDataBarang = findViewById(R.id.testDataBarang);
        btnBarcode = findViewById(R.id.btnBarcode);
        btnPicture = findViewById(R.id.btnPicture);
        btnSave = findViewById(R.id.btnSave);
        rvGambar = findViewById(R.id.rvGambar);
        testBarcode = findViewById(R.id.testBarcode);

        mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (mLogin.contains("username") && mLogin.contains("nik")) {
            headerMap.put("User-Name", Objects.requireNonNull(mLogin.getString("username", "")));
            headerMap.put("User-Id", Objects.requireNonNull(mLogin.getString("nik", "")));
        } else {
            headerMap.put("User-Name", "");
            headerMap.put("User-Id", "");
        }

        headerMap.put("Client-Service", "gmedia-stok-audit");
        headerMap.put("Auth-Key", "gmedia");
//        headerMap.put("Timestamp", "11017201850101101");
//        headerMap.put("Signature", "FWCcb1F1hAq+Q4/J3gJt4v6pgM5L2oKbW/KmWywfUDE=");

        String dataBarang = getIntent().getStringExtra("Master");
        testDataBarang.setText(dataBarang);

        kodeBarang = getIntent().getStringExtra("KodeMaster");

        final String id_lokasi = getIntent().getStringExtra("id_lokasi");
        final String nama_site = getIntent().getStringExtra("nama_site");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGambar.setLayoutManager(layoutManager);

        gambarAdapter = new GambarAdapter(arrayList);
        rvGambar.setAdapter(gambarAdapter);

        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new IntentIntegrator(AddActivity.this).initiateScan();
                IntentIntegrator integrator = new IntentIntegrator(DataBarangActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start(DataBarangActivity.this, 1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashTambahBarang.put("id_barang", kodeBarang);
                hashTambahBarang.put("id_lokasi", id_lokasi);
                hashTambahBarang.put("serial", barcodes);
                hashTambahBarang.put("data_image", idResponse);

                RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
                Call<ApiResponse> call = retrofitService.tambahBarang(headerMap, hashTambahBarang);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.body() != null) {
                            if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                                Toast.makeText(DataBarangActivity.this, "Barang sudah berhasil disimpan", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(DataBarangActivity.this, SavedActivity.class);
                                i.putExtra("Site", id_lokasi);
                                i.putExtra("Nama_Site", nama_site);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(DataBarangActivity.this, response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                                Log.d(DATA_BARANG, response.body().getMetadata().get("message"));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        Log.d(DATA_BARANG, t.getLocalizedMessage());
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                barcodes = result.getContents();
                testBarcode.setText(barcodes);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            assert data != null;
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            arrayList.add(returnValue.get(0));

            gambarAdapter = new GambarAdapter(arrayList);
            rvGambar.setAdapter(gambarAdapter);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(data.getStringArrayListExtra(Pix.IMAGE_RESULTS).get(0))));
                Bitmap resizeBitmaps = resizeBitmap(bitmap, 1024);

                Uri tempUri = getImageUri(getApplicationContext(), resizeBitmaps);
                File finalFile = new File(getRealPathFromURI(tempUri));

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), finalFile);
                RequestBody id_barang = RequestBody.create(MediaType.parse("text/plain"), kodeBarang);

                MultipartBody.Part body = MultipartBody.Part.createFormData("pic", finalFile.getName(), reqFile);

                RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
                Call<ApiResponse> call = retrofitService.uploadFoto(headerMap, body, id_barang);
                call.enqueue(new Callback<ApiResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.body() != null) {
                            if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                                if (response.body().getResponse() instanceof LinkedTreeMap) {
                                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) response.body().getResponse();
                                    HashMap<String, Integer> hashImage = new HashMap<>();

                                    Double angkaTemp = (Double) linkedTreeMap.get("id");

                                    assert angkaTemp != null;

                                    hashImage.put("id_image", angkaTemp.intValue());
                                    idResponse.add(hashImage);
                                } else {
                                    Toast.makeText(DataBarangActivity.this, response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                                    Log.d(DATA_BARANG, response.body().getMetadata().get("message"));
                                }
                            } else {
                                Toast.makeText(DataBarangActivity.this, response.body().getMetadata().get("message"), Toast.LENGTH_SHORT).show();
                                Log.d(DATA_BARANG, response.body().getMetadata().get("message"));
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        Log.d(DATA_BARANG, t.getLocalizedMessage());
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int idx = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        }
        assert cursor != null;
        return cursor.getString(idx);
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
