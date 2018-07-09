package vn.edu.imic.rxjavaticketapp.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import butterknife.Unbinder;
import vn.edu.imic.rxjavaticketapp.R;
import vn.edu.imic.rxjavaticketapp.application.MyApplication;
import vn.edu.imic.rxjavaticketapp.di.component.ActivityComponent;
import vn.edu.imic.rxjavaticketapp.di.component.DaggerActivityComponent;
import vn.edu.imic.rxjavaticketapp.di.module.ActivityModule;
import vn.edu.imic.rxjavaticketapp.util.CommonUtils;

public class BaseActivity extends AppCompatActivity implements BaseView {
    private ActivityComponent activityComponent;
    private Unbinder mUnbinder;
    private ProgressDialog mProgressDialog;
    protected Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(((MyApplication)getApplication()).getAppComponent())
                .build();

    }

    protected void setupToolbar(){

    }

    protected void setUpUnbinder(Unbinder unbinder){
        mUnbinder = unbinder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }



    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showError(String message) {
        if (message != null){
            showSnackBar(message);
        }
    }

    @Override
    public void loadSuccess() {

    }

    public void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),message,Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView tvMessage = sbView.findViewById(android.support.design.R.id.snackbar_text);
        tvMessage.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
