package model.mo;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Customer> customers;
    private ArrayList<Product> products;

    public Cart(ArrayList<Customer> customers, ArrayList<Product> products) {
        this.customers = customers;
        this.products = products;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "customers=" + customers +
                ", products=" + products +
                '}';
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
