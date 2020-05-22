package model.dao;

import model.exception.DuplicatedObjectException;
import model.mo.Admin;

public interface AdminDAO {

    Admin findById(Long id);

    boolean update(Admin admin) throws DuplicatedObjectException;

}