package vn.edu.imic.rxjavaticketapp.ui.maindagger;

import vn.edu.imic.rxjavaticketapp.ui.base.BasePresenter;

public interface MainPresenter<V extends MainView> extends BasePresenter<V>{
    void loadData(String from, String to);
}
