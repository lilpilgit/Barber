package model.mo;

import java.time.Instant;

public class Structure {

    private Long id;
    private String address;
    private String openingTime;
    private String closingTime;
    private String slot;
    private String name;
    private String phone;
    /* N:1 con ADMIN */
    private Admin admin;

    @Override
    public String toString() {
        return "Structure{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", slot='" + slot + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", admin=" + admin +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}


