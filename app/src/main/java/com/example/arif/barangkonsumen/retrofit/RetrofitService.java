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
            "Content-Type: application/json"
    })
    @POST("authentication/index")
    Call<ApiResponse> login(@Body HashMap<String, String> params);

    @POST("customer/index")
    Call<ApiResponse> customer(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("customer/list_site")
    Call<ApiResponse> customerSite(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("barang/detail_barang")
    Call<ApiResponse> detailBarang(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("barang/index")
    Call<ApiResponse> listBarang(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @Multipart
    @POST("barang/upload_foto")
    Call<ApiResponse> uploadFoto(@HeaderMap Map<String, String> headers,
                                 @Part MultipartBody.Part photo, @Part("id_barang") RequestBody id_barang);

    @POST("barang/tambah_barang")
    Call<ApiResponse> tambahBarang(@HeaderMap Map<String, String> headers, @Body HashMap<String, Object> params);

    @POST("barang/detail_serial_barang")
    Call<ApiResponse> detailSerial(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);

    @POST("barang/detail_gambar_serial_barang")
    Call<ApiResponse> detailGambar(@HeaderMap Map<String, String> headers, @Body HashMap<String, String> params);
}
