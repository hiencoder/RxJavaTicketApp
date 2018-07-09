package vn.edu.imic.rxjavaticketapp.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import vn.edu.imic.rxjavaticketapp.application.MyApplication;
import vn.edu.imic.rxjavaticketapp.di.module.ApplicationModule;
import vn.edu.imic.rxjavaticketapp.di.qualifier.AppContext;
import vn.edu.imic.rxjavaticketapp.di.scope.ActivityScope;
import vn.edu.imic.rxjavaticketapp.di.scope.ApplicationScope;

@ActivityScope
@Component(modules = ApplicationModule.class)
public interface AppComponent {
    //    Các component luôn phải có scope
    void injectApplication(MyApplication myApplication);

    @AppContext
    Context getContext();

    Application application();
}
