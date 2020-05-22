package model.mo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin {
    private User user;
    /*memorizzo l'id che setterò dopo che avrò aggiornato la tabella counter*/
    private Long id;
    private LocalDate birthDate;
    private String fiscalCode;


    /* 1 : N */
    private ArrayList<Structure> structures;

    @Override
    public String toString() {
        return "Admin{" +
                "user=" + user +
                ", id=" + id +
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
