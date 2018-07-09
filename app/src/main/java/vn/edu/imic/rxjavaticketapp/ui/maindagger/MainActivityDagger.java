package vn.edu.imic.rxjavaticketapp.ui.maindagger;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.edu.imic.rxjavaticketapp.R;
import vn.edu.imic.rxjavaticketapp.callback.TicketListener;
import vn.edu.imic.rxjavaticketapp.network.model.Ticket;
import vn.edu.imic.rxjavaticketapp.ui.base.BaseActivity;

public class MainActivityDagger extends BaseActivity implements TicketListener, MainView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView rvTicket;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout clMain;

    private String from = "DEL";
    private String to = "HYD";

    //Inject presenter
    @Inject
    MainPresenterImpl<MainView> mainViewMainPresenter;

    //Inject adapter
    @Inject
    TicketAdapterDagger ticketAdapterDagger;

    @Inject
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dagger);
        setUpUnbinder(ButterKnife.bind(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(from + " > " + to);

        mainViewMainPresenter.onAttach(this);

        rvTicket.setLayoutManager(layoutManager);
        rvTicket.setAdapter(ticketAdapterDagger);
        rvTicket.setHasFixedSize(true);
        rvTicket.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(Ticket ticket) {

    }

    @Override
    public void loadData(List<Ticket> tickets) {
        ticketAdapterDagger.setData(tickets);
    }
}
