package model.mo;

import java.time.LocalDate;

public class Purchase {
    private Long idProduct;
    private LocalDate date;
    private Long idCustomer;

    @Override
    public String toString() {
        return "Purchase{" +
                "idProduct=" + idProduct +
                ", date=" + date +
                ", idCustomer=" + idCustomer +
                '}';
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }
}
