package model.mo;

public class ServiceBooking {
    private Long idService;
    private Long idBooking;

    @Override
    public String toString() {
        return "ServiceBooking{" +
                "idService=" + idService +
                ", idBooking=" + idBooking +
                '}';
    }

    public Long getIdService() {
        return idService;
    }

    public void setIdService(Long idService) {
        this.idService = idService;
    }

    public Long getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Long idBooking) {
        this.idBooking = idBooking;
    }
}
