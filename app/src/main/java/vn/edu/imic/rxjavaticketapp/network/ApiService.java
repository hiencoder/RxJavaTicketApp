package vn.edu.imic.rxjavaticketapp.network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.edu.imic.rxjavaticketapp.network.model.Price;
import vn.edu.imic.rxjavaticketapp.network.model.Ticket;

/**
 * Created by MyPC on 14/04/2018.
 */

public interface ApiService {
    /*Lay ra danh sach ticket theo dia chi di va den*/
    @GET("airline-tickets.php")
    Single<List<Ticket>> searchTicket(@Query("from") String from,
                                      @Query("to") String to);

    /*Lấy ra thông tin giá vé theo đường bay*/
    @GET("airline-tickets-price.php")
    Single<Price> getPrice(@Query("flight_number") String flightNumber,
                           @Query("from") String from ,
                           @Query("to") String to);
}
