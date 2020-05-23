package model.session.dao.CookieImpl;

import model.mo.User;
import model.session.dao.LoggedUserDAO;
import model.session.mo.LoggedUser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggedUserDAOCookieImpl implements LoggedUserDAO {

  HttpServletRequest request;
  HttpServletResponse response;

  public LoggedUserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  @Override
  public LoggedUser insert(Long userId, String name, String surname, Boolean isAdmin, Boolean isEmployee, Boolean isCustomer) {
    LoggedUser loggedUser = new LoggedUser();
    loggedUser.setId(userId);
    loggedUser.setName(name);
    loggedUser.setSurname(surname);
    loggedUser.setIsAdmin(isAdmin);
    loggedUser.setIsEmployee(isEmployee);
    loggedUser.setIsAdmin(isCustomer);

    Cookie cookie;
    cookie = new Cookie("loggedUser", encode(loggedUser));
    cookie.setPath("/");
    response.addCookie(cookie);

    return loggedUser;
  }

  @Override
  public void update(LoggedUser loggedUser) {

    Cookie cookie;
    cookie = new Cookie("loggedUser", encode(loggedUser));
    cookie.setPath("/");
    response.addCookie(cookie);

  }

  @Override
  public void destroy() {

    Cookie cookie;
    cookie = new Cookie("loggedUser", "");
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

  }

  @Override
  public LoggedUser find() {

    Cookie[] cookies = request.getCookies();
    LoggedUser loggedUser = null;

    if (cookies != null) {
      for (int i = 0; i < cookies.length && loggedUser == null; i++) {
        if (cookies[i].getName().equals("loggedUser")) {
          loggedUser = decode(cookies[i].getValue());
        }
      }
    }

    return loggedUser;

  }

  private String encode(LoggedUser loggedUser) {

    /**
     * Encode the parameter object into a string of the type 'value1#value2#...'
     */
    String encodedLoggedUser;
    encodedLoggedUser
            = loggedUser.getId() + "#"
            + loggedUser.getName() + "#"
            + loggedUser.getSurname() + "#"
            + loggedUser.getIsAdmin() + "#"
            + loggedUser.getIsEmployee() + "#"
            + loggedUser.getIsCustomer() + "#";

    return encodedLoggedUser;

  }

  private LoggedUser decode(String encodedLoggedUser) {
    /**
     * Decode into an object, the parameter string of the type 'value1#value2#...'
     */
    LoggedUser loggedUser = new LoggedUser();

    String[] values = encodedLoggedUser.split("#");

    loggedUser.setId(Long.parseLong(values[0]));
    loggedUser.setName(values[1]);
    loggedUser.setSurname(values[2]);
    loggedUser.setIsAdmin(Boolean.parseBoolean(values[3]));
    loggedUser.setIsEmployee(Boolean.parseBoolean(values[4]));
    loggedUser.setIsCustomer(Boolean.parseBoolean(values[5]));

    return loggedUser;

  }
  
}

