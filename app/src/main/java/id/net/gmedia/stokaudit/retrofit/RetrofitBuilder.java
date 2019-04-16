package id.net.gmedia.stokaudit.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static final String BASE_URL = "https://erpsmg.gmedia.id/inventory_v2/api/";
//    private static final String BASE_URL = "http://gmedia.bz/sam/api/";
//    public static final String GET_SITE = BASE_URL + "Customer/list_site";

    public static Retrofit getApi() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // Add your other interceptors …

        // Add logging as last interceptor
        httpClient.addInterceptor(logging);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }
}
