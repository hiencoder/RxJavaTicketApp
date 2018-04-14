package vn.edu.imic.rxjavaticketapp.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import vn.edu.imic.rxjavaticketapp.R;
import vn.edu.imic.rxjavaticketapp.callback.TicketListener;
import vn.edu.imic.rxjavaticketapp.network.ApiClient;
import vn.edu.imic.rxjavaticketapp.network.ApiService;
import vn.edu.imic.rxjavaticketapp.network.model.Price;
import vn.edu.imic.rxjavaticketapp.network.model.Ticket;

public class MainActivity extends AppCompatActivity implements TicketListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String from = "DEL";
    private static final String to = "HYD";

    //Khai báo đối tượng CompositeDisposable để dispose subscription
    //Sử dụng nhiều Observable thì cần CompositeObservable
    private CompositeDisposable disposable = new CompositeDisposable();
    //Đối tượng Unbinder để unbind view(ButterKnife)
    private Unbinder unbinder;

    //Đối tượng để call apiservice
    private ApiService apiService;
    private TicketAdapter ticketAdapter;
    private List<Ticket> tickets = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView rvTicket;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(from + " > " + to);

        //Tạo đối tượng apiService
        apiService = ApiClient.getClient().create(ApiService.class);

        ticketAdapter = new TicketAdapter(this,tickets,this);

        //Tạo đối tượng layoutmanager
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        rvTicket.setLayoutManager(mLayoutManager);
        rvTicket.addItemDecoration(new GridSpacingItemDecoration(1,dpToPx(5),true));
        rvTicket.setItemAnimator(new DefaultItemAnimator());
        rvTicket.setAdapter(ticketAdapter);

        final ConnectableObservable<List<Ticket>> ticketObservable = getTickets(from,to).replay();

        //Subscribe
        disposable.add(
                ticketObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Ticket>>() {
                    @Override
                    public void onNext(List<Ticket> tickets) {
                        //Refresh danh sách ticket
                        tickets.clear();
                        tickets.addAll(tickets);
                        ticketAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                }));

        /*Lấy giá vé riêng lẻ
        * Flatmap đầu tiên chuyển đổi single List<Ticket> thành đa phát xạ
        * Flatmap thứ 2 tạo HTTP call mỗi lần phát ra ticket*/
        disposable.add(
                ticketObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                        /*Chuyển đổi từ phát danh sách ticket thành single ticket*/
                .flatMap(new Function<List<Ticket>, ObservableSource<Ticket>>() {
                    @Override
                    public ObservableSource<Ticket> apply(List<Ticket> tickets) throws Exception {
                        return Observable.fromIterable(tickets);
                    }
                })
                        /*Nạp đối tượng Price vào ticket */
                .flatMap(new Function<Ticket, ObservableSource<Ticket>>() {
                    @Override
                    public ObservableSource<Ticket> apply(Ticket ticket) throws Exception {
                        return getPriceObservable(ticket);
                    }
                })
                .subscribeWith(new DisposableObserver<Ticket>() {
                    @Override
                    public void onNext(Ticket ticket) {
                        int position = tickets.indexOf(ticket);
                        if (position == -1){
                            //ticket không được tìm thấy trong danh sách
                            //Không thực thi gì
                            return;
                        }
                        tickets.set(position,ticket);
                        ticketAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                })
        );

        //Gọi phương thức kết nối để nạp toàn bộ ticket
        ticketObservable.connect();
    }

    /**
     *Tạo retrofit để lấy về price của 1 ticket
     * Lấy Price HTTP call trả về đối tượng Price
     * Set đối tượng price cho ticket
     * @param ticket
     * @return
     */
    private Observable<Ticket> getPriceObservable(final Ticket ticket) {
        return apiService.getPrice(ticket.getFlightNumber(),
                ticket.getFrom(),
                ticket.getTo())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Price, Ticket>() {
                    @Override
                    public Ticket apply(Price price) throws Exception {
                        ticket.setPrice(price);
                        return ticket;
                    }
                });
    }

    /**
     * Show ra error
     * @param e
     */
    private void showError(Throwable e) {
        
    }

    /**
     * Lấy về tất cả ticket đầu tiên
     * Observable phát danh sách ticket một lần
     * Tất cả item sẽ được thêm vào recyclerview
     * @param from tham số địa điểm bắt đầu đi
     * @param to tham số địa điểm đến
     * @return trả về Observable lấy toàn bộ danh sách ticket
     */
    private Observable<List<Ticket>> getTickets(String from, String to) {
        return apiService.searchTicket(from,to)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onClick(Ticket ticket) {

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
