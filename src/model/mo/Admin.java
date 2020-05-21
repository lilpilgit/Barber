package model.mo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin {
    private User user;
    private LocalDate birthDate;
    private String fiscalCode;

    /* 1 : N */
    private ArrayList<Structure> structures;

    @Override
    public String toString() {
        return "Admin{" +
                "user=" + user +
                ", birthDate=" + birthDate +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", structures=" + structures +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }
}
