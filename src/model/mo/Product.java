package model.mo;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private Boolean deleted;

    /* N : 1 with Structure */
    private Structure structure;

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
                ", deleted=" + deleted +
                ", structure=" + structure +
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

    public Boolean inShowcase() {
        return showcase;
    }

    public void setShowcase(Boolean showcase) {
        this.showcase = showcase;
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
}
