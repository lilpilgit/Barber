package model.session.dao;

import model.session.mo.LoggedUser;

public interface LoggedUserDAO {

  public LoggedUser insert(
          Long userId,
          String name,
          String surname,
          Boolean isAdmin,
          Boolean isEmployee,
          Boolean isCustomer);

  public void update(LoggedUser loggedUser);

  public void destroy();

  public LoggedUser find();
  
}
