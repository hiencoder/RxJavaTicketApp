package vn.edu.imic.rxjavaticketapp.ui.base;

public interface BaseView {
    void showLoading();

    void  hideLoading();

    void showError(String message);

    void loadSuccess();
}
