package model.session.mo;

public class LoggedUser {
  
  private Long id;
  private String name;
  private String surname;
  private Boolean isAdmin;
  private Boolean isEmployee;
  private Boolean isCustomer;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Boolean getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(Boolean admin) {
    isAdmin = admin;
  }

  public Boolean getIsEmployee() {
    return isEmployee;
  }

  public void setIsEmployee(Boolean employee) {
    isEmployee = employee;
  }

  public Boolean getIsCustomer() {
    return isCustomer;
  }

  public void setIsCustomer(Boolean customer) {
    isCustomer = customer;
  }
}
