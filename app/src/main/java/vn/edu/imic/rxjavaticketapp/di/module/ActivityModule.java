package vn.edu.imic.rxjavaticketapp.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import vn.edu.imic.rxjavaticketapp.callback.TicketListener;
import vn.edu.imic.rxjavaticketapp.di.qualifier.ActivityContext;
import vn.edu.imic.rxjavaticketapp.di.scope.ActivityScope;
import vn.edu.imic.rxjavaticketapp.ui.maindagger.MainActivityDagger;
import vn.edu.imic.rxjavaticketapp.ui.maindagger.MainPresenterImpl;
import vn.edu.imic.rxjavaticketapp.ui.maindagger.MainView;
import vn.edu.imic.rxjavaticketapp.ui.maindagger.TicketAdapterDagger;

@Module
public class ActivityModule {
    /*Cung cấp các thành phần cho MainActivity*/
    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    //Provide activity
    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext(){
        return mActivity;
    }

    @Provides
    @ActivityScope
    public AppCompatActivity provideAppCompatActivity(){
        return mActivity;
    }

    //Provide adapter
    @Provides
    @ActivityScope
    public TicketAdapterDagger provideTickerAdapter(TicketListener ticketListener){
        return new TicketAdapterDagger(ticketListener);
    }

    //Provide TicketListener
    @Provides
    @ActivityScope
    public TicketListener provideTicketListener(MainActivityDagger mainActivityDagger){
        return mainActivityDagger;
    }

    //Provide CompositeDisposable
    @Provides
    @ActivityScope
    public CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }

    //Provide PresenterImpl
    @Provides
    @ActivityScope
    public MainPresenterImpl<MainView> provideMainPresenterImpl(MainPresenterImpl<MainView> presenter){
        return presenter;
    }

    //Provide layoutmanager
    @Provides
    @ActivityScope
    public LinearLayoutManager provideLayoutManager(AppCompatActivity appCompatActivity){
        return new LinearLayoutManager(appCompatActivity);
    }
}
