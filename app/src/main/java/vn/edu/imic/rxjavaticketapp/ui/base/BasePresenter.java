package vn.edu.imic.rxjavaticketapp.ui.base;

public interface BasePresenter<V extends BaseView> {
    void onAttach(V mvpView);

    void onDetach();

}
