package vn.edu.imic.rxjavaticketapp.application;

import android.app.Application;

import vn.edu.imic.rxjavaticketapp.di.component.AppComponent;
import vn.edu.imic.rxjavaticketapp.di.component.DaggerAppComponent;
import vn.edu.imic.rxjavaticketapp.di.module.ApplicationModule;

public class MyApplication extends Application{
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        appComponent.injectApplication(this);
    }

    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
