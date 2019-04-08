package com.example.arif.barangkonsumen.retrofit;

import com.example.arif.barangkonsumen.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitService {

    @Headers({
            "Client-Service: gmedia-stok-audit",
            "Auth-Key: gmedia",
            "Content-Type: application/json",
            "Timestamp: 11017201850101101",
            "Signature: FWCcb1F1hAq+Q4/J3gJt4v6pgM5L2oKbW/KmWywfUDE="
    })
    @POST("Authentication/index")
    Call<ApiResponse> login(@Body HashMap<String, String> params);

    @POST("Customer/index")
    Call<ApiResponse> customer(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("Customer/list_site")
    Call<ApiResponse> customerSite(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("Barang/detail_barang")
    Call<ApiResponse> detailBarang(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("Barang/index")
    Call<ApiResponse> listBarang(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @Multipart
    @POST("Barang/upload_foto")
    Call<ApiResponse> uploadFoto(@HeaderMap Map<String, String> headers,
                                 @Part MultipartBody.Part photo, @Part("id_barang") RequestBody id_barang);

    @POST("Barang/tambah_barang")
    Call<ApiResponse> tambahBarang(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> params);

    @POST("Barang/detail_serial_barang")
    Call<ApiResponse> detailSerial(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("Barang/detail_gambar_serial_barang")
    Call<ApiResponse> detailGambar(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);
}