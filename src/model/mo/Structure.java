package model.mo;

import java.sql.Time;
import java.util.ArrayList;

public class Structure {

    private Long id;
    private String address;
    private Time openingTime;
    private Time closingTime;
    private Time slot;        /* This is necessary for scanning the booking appointments */
    private String name;
    private String phone;

    /* 1:N with EMPLOYEE */
    private ArrayList<User> employees;
    /* 1:N with BOOKING */
    private ArrayList<Booking> bookings;
    /* 1:N with PRODUCT */
    private ArrayList<Product> products;

    @Override
    public String toString() {
        return "Structure{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", slot=" + slot +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", employees=" + employees +
                ", bookings=" + bookings +
                ", products=" + products +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }

    public Time getSlot() {
        return slot;
    }

    public void setSlot(Time slot) {
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<User> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<User> employees) {
        this.employees = employees;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}


