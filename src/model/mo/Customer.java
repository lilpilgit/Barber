package model.mo;

import java.util.ArrayList;

public class Customer {
    private User user;
    private Integer numBookedReservations;
    private Integer numOrderedProduct;
    private Boolean isBlocked;

    /* M : N */
    private ArrayList<Product> products;
    private ArrayList<Wishlist> wishlists;
    private ArrayList<Cart> carts;
    private ArrayList<Booking> bookings;

    public Customer(User user, Integer numBookedReservations, Integer numOrderedProduct, Boolean isBlocked, ArrayList<Product> products, ArrayList<Wishlist> wishlists, ArrayList<Cart> carts, ArrayList<Booking> bookings) {
        this.user = user;
        this.numBookedReservations = numBookedReservations;
        this.numOrderedProduct = numOrderedProduct;
        this.isBlocked = isBlocked;
        this.products = products;
        this.wishlists = wishlists;
        this.carts = carts;
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "user=" + user +
                ", numBookedReservations=" + numBookedReservations +
                ", numOrderedProduct=" + numOrderedProduct +
                ", isBlocked=" + isBlocked +
                ", products=" + products +
                ", wishlists=" + wishlists +
                ", carts=" + carts +
                ", bookings=" + bookings +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(ArrayList<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }
}
