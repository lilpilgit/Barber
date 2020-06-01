package model.mo;

public class ExtendedProduct extends Product {
    private Integer requiredQuantity;

    @Override
    public String toString() {
        return "ExtendedProduct{" +
                "requiredQuantity=" + requiredQuantity +
                "} " + super.toString();
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
}
