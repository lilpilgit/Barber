package model.mo;

public class ExtendedProduct extends Product {
    private Integer requiredQuantity;
    private boolean inWishlist;
    private boolean inCart;

    public ExtendedProduct(Product product){
        this.setName(product.getName());
        this.setPrice(product.getPrice());
        this.setId(product.getId());
        this.setProducer(product.getProducer());
        this.setCategory(product.getCategory());
        this.setPictureName(product.getPictureName());
        this.setDiscount(product.getDiscount());
        this.setDescription(product.getDescription());
        this.setMaxOrderQuantity(product.getMaxOrderQuantity());
        this.setStructure(product.getStructure());
        this.setInsertDate(product.getInsertDate());
        this.setShowcase(product.inShowcase());
        this.setDeleted(product.isDeleted());
    }

    public ExtendedProduct(){
        super();
    }

    @Override
    public String toString() {
        return "ExtendedProduct{" +
                "requiredQuantity=" + requiredQuantity +
                ", inWishlist=" + inWishlist +
                ", inCart=" + inCart +
                "} " + super.toString();
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public boolean isInWishlist() {
        return inWishlist;
    }

    public boolean isInCart() {
        return isInCart();
    }

    public void setInWishlist(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }
}
