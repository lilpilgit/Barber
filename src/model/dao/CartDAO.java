package model.dao;

import model.mo.ExtendedProduct;
import model.mo.User;

import java.util.ArrayList;

public interface CartDAO {

    ArrayList<ExtendedProduct> fetchCart(User user) throws UnsupportedOperationException;

    boolean addProductToCart(User user, Long idProduct, Integer desiredQty) throws UnsupportedOperationException;

    boolean removeProductFromCart(User user, Long idProduct) throws UnsupportedOperationException;

    boolean changeDesiredQuantity(User user, Long idProduct, boolean increase, Integer desiredQuantity) throws UnsupportedOperationException;

    boolean inCart(User user,Long idProduct) throws UnsupportedOperationException;
}
