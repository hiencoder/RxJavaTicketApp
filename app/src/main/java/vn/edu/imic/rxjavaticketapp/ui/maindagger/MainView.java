package vn.edu.imic.rxjavaticketapp.ui.maindagger;

import java.util.List;

import vn.edu.imic.rxjavaticketapp.network.model.Ticket;
import vn.edu.imic.rxjavaticketapp.ui.base.BaseView;

public interface MainView extends BaseView{
    void loadData(List<Ticket> tickets);

}
