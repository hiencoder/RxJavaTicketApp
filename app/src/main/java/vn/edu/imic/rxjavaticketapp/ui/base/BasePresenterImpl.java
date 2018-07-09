package vn.edu.imic.rxjavaticketapp.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import vn.edu.imic.rxjavaticketapp.network.ApiService;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V>{
    private static final String TAG = BasePresenterImpl.class.getSimpleName();
    private CompositeDisposable compositeDisposable;
    private V mMvpView;
    private ApiService apiService;
    public BasePresenterImpl(CompositeDisposable compositeDisposable, ApiService apiService) {
        this.compositeDisposable = compositeDisposable;
        this.apiService = apiService;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
        mMvpView = null;
    }

    public BaseView getBaseView(){
        return mMvpView;
    }

    public boolean isViewAttached(){
        return mMvpView != null;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
