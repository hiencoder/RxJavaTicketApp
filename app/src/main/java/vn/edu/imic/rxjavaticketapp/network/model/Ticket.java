package vn.edu.imic.rxjavaticketapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MyPC on 14/04/2018.
 */

public class Ticket {
    @SerializedName("from")
    String from;

    @SerializedName("to")
    String to;

    @SerializedName("flight_number")
    String flightNumber;

    @SerializedName("departure")
    String departure;

    @SerializedName("arrival")
    String arrival;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("stops")
    private int stops;

    @SerializedName("duration")
    private String duration;

    //Ticket sẽ phụ thuộc vào Price và Airline
    private Price price;

    @SerializedName("airline")
    private Airline airline;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getStops() {
        return stops;
    }

    public Price getPrice() {
        return price;
    }

    public Airline getAirline() {
        return airline;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Ticket)) {
            return false;
        }

        return flightNumber.equalsIgnoreCase(((Ticket) obj).getFlightNumber());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.flightNumber != null ? this.flightNumber.hashCode() : 0);
        return hash;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
