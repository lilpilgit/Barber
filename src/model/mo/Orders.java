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
    private String shippingAddress;
    private Boolean deleted;

    /* N:1 COSTUMER */
    private User customer;

    private ArrayList<ExtendedProduct> itemList;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", sellDate=" + sellDate +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", totPrice=" + totPrice +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", deleted=" + deleted +
                ", customer=" + customer +
                ", itemList=" + itemList +
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public ArrayList<ExtendedProduct> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ExtendedProduct> itemList) {
        this.itemList = itemList;
    }
}
