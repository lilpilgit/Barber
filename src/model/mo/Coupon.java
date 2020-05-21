package model.mo;

import java.time.LocalDate;

public class Coupon {
    private Long id;
    private Long idCustomer;
    private Long idBooking;
    private Integer code;
    private Integer percentageDiscount;
    private LocalDate expirationDate;
    private LocalDate issuingDate;
    private Boolean isUsed;    /* IsUsed viene settato a 1 anche nel caso in cui il Coupon venga cancellato */

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", idCustomer=" + idCustomer +
                ", idBooking=" + idBooking +
                ", code=" + code +
                ", percentageDiscount=" + percentageDiscount +
                ", expirationDate=" + expirationDate +
                ", issuingDate=" + issuingDate +
                ", isUsed=" + isUsed +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Long getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Long idBooking) {
        this.idBooking = idBooking;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(Integer percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(LocalDate issuingDate) {
        this.issuingDate = issuingDate;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }
}
