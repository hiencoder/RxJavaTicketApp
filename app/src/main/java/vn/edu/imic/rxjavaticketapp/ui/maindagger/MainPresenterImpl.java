package vn.edu.imic.rxjavaticketapp.ui.maindagger;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import vn.edu.imic.rxjavaticketapp.network.ApiService;
import vn.edu.imic.rxjavaticketapp.network.model.Price;
import vn.edu.imic.rxjavaticketapp.network.model.Ticket;
import vn.edu.imic.rxjavaticketapp.ui.base.BasePresenterImpl;
import vn.edu.imic.rxjavaticketapp.util.Logutil;

public class MainPresenterImpl<V extends MainView> extends BasePresenterImpl<V> implements MainPresenter<V>{
    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    @Inject
    public MainPresenterImpl(CompositeDisposable compositeDisposable, ApiService apiService) {
        super(compositeDisposable,apiService);
    }

    @Override
    public void loadData(String from, String to) {
        //Fetcha data
        final ConnectableObservable<List<Ticket>> ticketObservable = getTickets(from,to).replay();
        getCompositeDisposable().add(ticketObservable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<List<Ticket>>() {
            @Override
            public void onNext(List<Ticket> tickets) {
                ((MainView) getBaseView()).loadData(tickets);
            }

            @Override
            public void onError(Throwable e) {
                getBaseView().showError(e.getMessage());
            }

            @Override
            public void onComplete() {
                getBaseView().hideLoading();
            }
        }));

        getCompositeDisposable().add(ticketObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(new Function<List<Ticket>, ObservableSource<Ticket>>() {
            @Override
            public ObservableSource<Ticket> apply(List<Ticket> tickets) throws Exception {
                return Observable.fromIterable(tickets);
            }
        })
        .flatMap(new Function<Ticket, ObservableSource<Ticket>>() {
            @Override
            public ObservableSource<Ticket> apply(Ticket ticket) throws Exception {
                return getObservablePriceTicket(ticket);
            }
        })
        .subscribeWith(new DisposableObserver<Ticket>() {
            @Override
            public void onNext(Ticket ticket) {
                Logutil.d(TAG,ticket.getPrice().getCurrency());
            }

            @Override
            public void onError(Throwable e) {
                getBaseView().showError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }));

        ticketObservable.connect();
    }

    /**
     *
     * @param ticket
     * @return
     */
    private Observable<Ticket> getObservablePriceTicket(final Ticket ticket) {
        return getApiService().getPrice(ticket.getFlightNumber(),
                ticket.getFrom(),
                ticket.getFrom())
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

    private Observable<List<Ticket>> getTickets(String from, String to) {
        return getApiService().searchTicket(from,to)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
