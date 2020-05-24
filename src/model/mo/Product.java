package model.mo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Product {
    private Long id;
    private String producer;
    private BigDecimal price;
    private Integer discount;
    private String name;
    private LocalDate insertDate;
    private String pictureName;
    private String description;
    private Integer quantity;
    private String category;
    private Boolean showcase;                   /* The product is highlighted ? */
    private Boolean isDeleted;

    /* N : 1 with Structure */
    private Structure structure;

    /* M : N with Orders */
    private ArrayList<Customer> orders;
    /* M : N with Cart */
    private ArrayList<Customer> carts;
    /* M : N with Wishlist */
    private ArrayList<Customer> wishlists;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", producer='" + producer + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", name='" + name + '\'' +
                ", insertDate=" + insertDate +
                ", pictureName='" + pictureName + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", showcase=" + showcase +
                ", isDeleted=" + isDeleted +
                ", structure=" + structure +
                ", orders=" + orders +
                ", carts=" + carts +
                ", wishlists=" + wishlists +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(LocalDate insertDate) {
        this.insertDate = insertDate;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getShowcase() {
        return showcase;
    }

    public void setShowcase(Boolean showcase) {
        this.showcase = showcase;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public ArrayList<Customer> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Customer> orders) {
        this.orders = orders;
    }

    public ArrayList<Customer> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Customer> carts) {
        this.carts = carts;
    }

    public ArrayList<Customer> getWishlists() {
        return wishlists;
    }

    public void setWishlists(ArrayList<Customer> wishlists) {
        this.wishlists = wishlists;
    }
}
