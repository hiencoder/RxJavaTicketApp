package vn.edu.imic.rxjavaticketapp.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.imic.rxjavaticketapp.app.Const;

/**
 * Created by MyPC on 14/04/2018.
 */

public class ApiClient {
    private static Retrofit retrofit = null;
    private static int REQUEST_TIMEOUT = 60;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient(){
        if (okHttpClient == null){
            initOkHttpClient();
        }

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /*Init okhttpclient*/
    private static void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT,TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(httpLoggingInterceptor);

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept","application/json")
                        .addHeader("Request-Type","Android")
                        .addHeader("Content-Type","application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        builder.build();
    }

    /*Xóa bỏ apiclient*/
    public static void resetApiClient(){
        retrofit = null;
        okHttpClient = null;
    }
}
