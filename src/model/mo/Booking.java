package model.mo;

import java.time.Instant;
import java.time.LocalDate;

public class Booking {
    private Long id;
    private Boolean deleted;                /* Booking deleted or not? */
    private String deletedReason;
    private LocalDate date;
    private Instant hourStart;

    /* 1 : 1 User */
    private User customer;
    /* N : 1 Structure */
    private Structure structure;

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", deletedReason='" + deletedReason + '\'' +
                ", date=" + date +
                ", hourStart=" + hourStart +
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
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}