package model.mo;

import java.time.LocalDate;

public class Sale {
    private Long idStructure;
    private LocalDate date;
    private Long idProduct;

    @Override
    public String toString() {
        return "Sale{" +
                "idStructure=" + idStructure +
                ", date=" + date +
                ", idProduct=" + idProduct +
                '}';
    }

    public Long getIdStructure() {
        return idStructure;
    }

    public void setIdStructure(Long idStructure) {
        this.idStructure = idStructure;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }
}
