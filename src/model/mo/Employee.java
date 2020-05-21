package model.mo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Employee {
    /*discende dalla classe User dunque deve avere lo stesso ID che è presente nella tabella USER*/
    private User user;
    /*memorizzo l'id che setterò dopo che avrò aggiornato la tabella counter*/
    private Long id;
    private LocalDate birthDate;
    private String fiscalCode;
    private LocalDate hireDate;
    /*  private String picName; TODO:foto dipendente da implementare????*/
    /* N : 1 */
    private Structure structure;




    @Override
    public String toString() {
        return "Employee{" +
                "user=" + user +
                ", id=" + id +
                ", birthDate=" + birthDate +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", hireDate=" + hireDate +
                ", structure=" + structure +
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

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
