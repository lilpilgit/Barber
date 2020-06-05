package model.mo;

public class ExtendedProduct extends Product {
    private Integer requiredQuantity;
    private boolean inWishlist;

    @Override
    public String toString() {
        return "ExtendedProduct{" +
                "requiredQuantity=" + requiredQuantity +
                ", inWishlist=" + inWishlist +
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

    public void setInWishlist(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }
}
