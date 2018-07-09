package vn.edu.imic.rxjavaticketapp.di.module;

import android.app.Application;
import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.imic.rxjavaticketapp.di.qualifier.AppContext;
import vn.edu.imic.rxjavaticketapp.di.scope.ApplicationScope;
import vn.edu.imic.rxjavaticketapp.network.ApiService;
import vn.edu.imic.rxjavaticketapp.util.Const;

@Module
public class ApplicationModule {
    /*Cung cấp các thành phần sử dụng trong application*/
    private Application mApplication;

    public ApplicationModule(Application application) {
        this.mApplication = application;
    }

    //provide context
    @Provides
    @ApplicationScope
    @AppContext
    public Context provideContext(){
        return mApplication;
    }

    //Provide application
    @Provides
    @ApplicationScope
    public Application provideApplication(){
        return mApplication;
    }
    //provide ApiService
    @Provides
    @ApplicationScope
    public ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

    //Provide Retrofit
    @Provides
    @ApplicationScope
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    //Provide OkHttpClient
    @Provides
    @ApplicationScope
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor){
        return new OkHttpClient.Builder()
                .readTimeout(Const.REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Const.REQUEST_TIMEOUT,TimeUnit.MILLISECONDS)
                .writeTimeout(Const.REQUEST_TIMEOUT,TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    //https://github.com/MindorksOpenSource/android-mvp-architecture
    //Provide HttpLogginInterceptor
    @Provides
    @ApplicationScope
    public HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
