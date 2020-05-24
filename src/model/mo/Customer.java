package model.mo;

import java.util.ArrayList;

public class Customer {
    private User user;
    private Long id;
    private Integer numBookedReservations;
    private Integer numOrderedProduct;
    private Boolean isBlocked;

    /* M : N */
    private ArrayList<Product> orders;
    private ArrayList<Product> wishlist;
    private ArrayList<Product> cart;
    /* 1 : N */
    private ArrayList<Booking> bookings;

    @Override
    public String toString() {
        return "Customer{" +
                "user=" + user +
                ", id=" + id +
                ", numBookedReservations=" + numBookedReservations +
                ", numOrderedProduct=" + numOrderedProduct +
                ", isBlocked=" + isBlocked +
                ", orders=" + orders +
                ", wishlist=" + wishlist +
                ", cart=" + cart +
                ", bookings=" + bookings +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumBookedReservations() {
        return numBookedReservations;
    }

    public void setNumBookedReservations(Integer numBookedReservations) {
        this.numBookedReservations = numBookedReservations;
    }

    public Integer getNumOrderedProduct() {
        return numOrderedProduct;
    }

    public void setNumOrderedProduct(Integer numOrderedProduct) {
        this.numOrderedProduct = numOrderedProduct;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public ArrayList<Product> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Product> orders) {
        this.orders = orders;
    }

    public ArrayList<Product> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<Product> wishlist) {
        this.wishlist = wishlist;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    public void setCart(ArrayList<Product> cart) {
        this.cart = cart;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }
}
