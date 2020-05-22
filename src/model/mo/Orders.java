package model.mo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Orders {
    private Long id;
    private LocalDate sellDate;
    private LocalDate orderDate;
    private String status;
    private BigDecimal totPrice;
    private Boolean isDeleted;

    /* M:N with product */
    private ArrayList<Product> products;
    /* M:N with customer */
    private ArrayList<Customer> customers;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", sellDate=" + sellDate +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", totPrice=" + totPrice +
                ", isDeleted=" + isDeleted +
                ", products=" + products +
                ", customers=" + customers +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSellDate() {
        return sellDate;
    }

    public void setSellDate(LocalDate sellDate) {
        this.sellDate = sellDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(BigDecimal totPrice) {
        this.totPrice = totPrice;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }
}
