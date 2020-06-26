package model.dao;

import model.mo.ExtendedProduct;
import model.mo.User;

import java.util.ArrayList;

public interface WishlistDAO {

    ArrayList<ExtendedProduct> fetchWishlist(User user);

    boolean inWishlist(User user,Long idProduct) throws UnsupportedOperationException;

    boolean removeProductFromWishlist(User user, Long idProduct) throws UnsupportedOperationException;

    boolean addProductToWishlist(User user, Long idProduct) throws UnsupportedOperationException;

}
