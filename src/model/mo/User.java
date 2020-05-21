package model.mo;

public class User {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String address;
    private String phone;
    private String password;
    private Boolean isAdmin;
    private Boolean isEmployee;
    private Boolean isCustomer;
    private Boolean isDeleted;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                ", isEmployee=" + isEmployee +
                ", isCustomer=" + isCustomer +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean isEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(Boolean employee) {
        isEmployee = employee;
    }

    public Boolean isCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(Boolean customer) {
        isCustomer = customer;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}