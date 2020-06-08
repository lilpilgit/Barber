package model.mo;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String address;
    private String phone;
    private String password;
    private LocalDate birthDate;
    private String fiscalCode;
    private char type;                  /* A C E */
    private Boolean blocked;
    private Boolean deleted;

    /* 1:1 Admin & N:1 Customers */
    private Structure structure;
    /* 1:1 Booking */
    private Booking booking;
    /* N:M Cart */
    private ArrayList<ExtendedProduct> cart;
    /* N:M Wishlist */
    private ArrayList<Product> wishlist;
    /* 1:N Order */
    private ArrayList<Order> orders;

//    public User(){
//        this.structure = new Structure();
//        this.booking = new Booking();
//        this.cart = new ArrayList<>();
//        this.wishlist = new ArrayList<>();
//        this.orders = new ArrayList<>();
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", type=" + type +
                ", blocked=" + blocked +
                ", deleted=" + deleted +
                ", structure=" + structure +
                ", booking=" + booking +
                ", cart=" + cart +
                ", wishlist=" + wishlist +
                ", orders=" + orders +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public Boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public ArrayList<ExtendedProduct> getCart() {
        return cart;
    }

    public void setCart(ArrayList<ExtendedProduct> cart) {
        this.cart = cart;
    }

    public ArrayList<Product> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<Product> wishlist) {
        this.wishlist = wishlist;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}