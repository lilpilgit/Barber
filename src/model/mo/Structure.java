package model.mo;

import java.time.Instant;
import java.util.ArrayList;

public class Structure {

    private Long id;
    private String address;
    private Instant openingTime;
    private Instant closingTime;
    private Instant slot;        /* This is necessary for scanning the booking appointments */
                        /*TODO:controllare per quale cazzo di motivo se uso Time sia qui che nel DB che nell'html va in eccezione*/
    private String name;
    private String phone;

    /* 1:1 with ADMIN */
    private User admin;

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
                ", admin=" + admin +
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

    public Instant getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Instant openingTime) {
        this.openingTime = openingTime;
    }

    public Instant getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Instant closingTime) {
        this.closingTime = closingTime;
    }

    public Instant getSlot() {
        return slot;
    }

    public void setSlot(Instant slot) {
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

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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


