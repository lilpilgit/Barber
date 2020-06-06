package model.dao;

import model.exception.DuplicatedObjectException;
import model.mo.ExtendedProduct;
import model.mo.Product;
import model.mo.Structure;
import model.mo.User;

import java.time.LocalDate;
import java.util.ArrayList;

public interface CartDAO {

    ArrayList<ExtendedProduct> fetchCart(User user) throws UnsupportedOperationException;

    boolean addProductToCart(User user, Long idProduct, Integer desiredQty) throws UnsupportedOperationException;

    boolean removeProductFromCart(User user, Long idProduct) throws UnsupportedOperationException;

    boolean changeDesiredQuantity(User user, Long idProduct, boolean increase, Integer desiredQuantity) throws UnsupportedOperationException;
}
