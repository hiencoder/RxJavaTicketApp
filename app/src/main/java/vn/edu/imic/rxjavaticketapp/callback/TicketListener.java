package vn.edu.imic.rxjavaticketapp.callback;

import vn.edu.imic.rxjavaticketapp.network.model.Ticket;

/**
 * Created by MyPC on 14/04/2018.
 */

public interface TicketListener {
    void onClick(Ticket ticket);
}
