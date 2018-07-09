package vn.edu.imic.rxjavaticketapp.di.component;

import dagger.Component;
import vn.edu.imic.rxjavaticketapp.di.module.ActivityModule;
import vn.edu.imic.rxjavaticketapp.di.scope.ActivityScope;
import vn.edu.imic.rxjavaticketapp.di.scope.ApplicationScope;
import vn.edu.imic.rxjavaticketapp.ui.maindagger.MainActivityDagger;

@ActivityScope
@Component(modules = ActivityModule.class,dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(MainActivityDagger activityDagger);
}
