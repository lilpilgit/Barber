package model.mo;

import java.util.ArrayList;

public class Structure {

    private Long id;
    private String address;
    private String openingTime;
    private String closingTime;
    private String slot;        /* This is necessary for scanning the booking appointments */
    private String name;
    private String phone;

    /* 1:1 with ADMIN */
    private Admin admin;

    /* 1:N with EMPLOYEE */
    private ArrayList<Employee> employees;
    /* 1:N with BOOKING */
    private ArrayList<Booking> bookings;
    /* 1:N with PRODUCT */
    private ArrayList<Product> products;


    @Override
    public String toString() {
        return "Structure{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", slot='" + slot + '\'' +
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

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
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


