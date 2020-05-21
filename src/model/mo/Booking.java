package model.mo;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Booking {
    private Long id;
    private Boolean isDeleted;                /* Booking deleted or not? */
    private String deletedReason;
    private LocalDate date;
    private Instant hourStart;
    private BigDecimal price;
    private Instant hourEnd;

    /* N : 1 */
    private Employee employee;
    /* N : 1 */
    private Customer customer;
    /* N : 1 */
    private Structure structure;

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", isDeleted=" + isDeleted +
                ", deletedReason='" + deletedReason + '\'' +
                ", date=" + date +
                ", hourStart=" + hourStart +
                ", price=" + price +
                ", hourEnd=" + hourEnd +
                ", employee=" + employee +
                ", customer=" + customer +
                ", structure=" + structure +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getDeletedReason() {
        return deletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getHourStart() {
        return hourStart;
    }

    public void setHourStart(Instant hourStart) {
        this.hourStart = hourStart;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(Instant hourEnd) {
        this.hourEnd = hourEnd;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}