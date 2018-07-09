package vn.edu.imic.rxjavaticketapp.ui.maindagger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.edu.imic.rxjavaticketapp.R;
import vn.edu.imic.rxjavaticketapp.callback.TicketListener;
import vn.edu.imic.rxjavaticketapp.network.model.Ticket;

/**
 * Created by MyPC on 14/04/2018.
 */

public class TicketAdapterDagger extends RecyclerView.Adapter<TicketAdapterDagger.TicketHolder>{
    private List<Ticket> tickets;
    private TicketListener ticketListener;

    public TicketAdapterDagger(TicketListener ticketListener) {
        this.tickets = new ArrayList<>();
        this.ticketListener = ticketListener;
    }

    @Override
    public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item,
                parent,false);
        return new TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketHolder holder, int position) {
        final Ticket ticket = tickets.get(position);
        holder.bind(ticket);
                    /*Sự kiện click cho itemview*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketListener.onClick(ticket);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tickets != null ? tickets.size() : 0;
    }

    public void setData(List<Ticket> list){
        tickets.addAll(list);
        notifyDataSetChanged();
    }
    public class TicketHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.airline_name)
        TextView tvAirlineName;

        @BindView(R.id.logo)
        ImageView imgLogo;

        @BindView(R.id.number_of_stops)
        TextView stops;

        @BindView(R.id.number_of_seats)
        TextView seats;

        @BindView(R.id.departure)
        TextView departures;

        @BindView(R.id.arrival)
        TextView arrival;

        @BindView(R.id.duration)
        TextView duration;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.loader)
        SpinKitView loader;

        View view;
        public TicketHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,view);

        }

        public void bind(Ticket ticket){
            Glide.with(view.getContext())
                    .load(ticket.getAirline().getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgLogo);
            tvAirlineName.setText(ticket.getAirline().getName());
            stops.setText(ticket.getStops() + " Stops");
            departures.setText(ticket.getDeparture() + " Dep");
            arrival.setText(ticket.getArrival() + " Dest");
            duration.setText(ticket.getFlightNumber());
            duration.append(", " + ticket.getDuration());

            if (!TextUtils.isEmpty(ticket.getInstructions())){
                duration.append(", " + ticket.getInstructions());
            }

            if (ticket.getPrice() != null){
                price.setText("₹" + String.format("%.0f",
                        ticket.getPrice().getPrice()));
                seats.setText(ticket.getPrice().getSeats() + " Seats");
                loader.setVisibility(View.INVISIBLE);
            }else{
                loader.setVisibility(View.VISIBLE);
            }
        }
    }
}
