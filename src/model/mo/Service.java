package model.mo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Service {
    private Long id;
    private String name;
    private BigDecimal price;
    private Instant duration;

    /* N:M */
    private ArrayList<Booking> bookings;

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", booking=" + bookings +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getDuration() {
        return duration;
    }

    public void setDuration(Instant duration) {
        this.duration = duration;
    }

    public ArrayList<Booking> getBooking() {
        return bookings;
    }

    public void setBooking(ArrayList<Booking> booking) {
        this.bookings = booking;
    }
}
