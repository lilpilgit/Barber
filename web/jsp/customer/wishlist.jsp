<%@page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="model.mo.ExtendedProduct" %>
<%@ page import="model.mo.Structure" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
    /* Prendo il parametro "loggedOn" che mi consente di sapere se l'utente attuale è loggato o meno */
    boolean loggedOn = false;
    if (request.getAttribute("loggedOn") != null) {
        loggedOn = (Boolean) request.getAttribute("loggedOn");
    }

    /* Prendo il parametro "loggedUser" che mi consente di sapere qual'è l'utente attualmente loggato */
    User loggedUser = null;
    if (loggedOn && request.getAttribute("loggedUser") != null) {
        loggedUser = (User) request.getAttribute("loggedUser");
    }

    /* Prendo il parametro "result" che si occupa di indicarmi se le operazioni effettuate su carrello/wishlist sono andate a buon fine o meno */
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    ArrayList<ExtendedProduct> wishlist = null;
    if (request.getAttribute("wishlist") != null) {
        wishlist = (ArrayList<ExtendedProduct>) request.getAttribute("wishlist");
    }

    /* Prendo l'oggetto struttura per conoscere le informazioni da mostrare nel footer */
    Structure structure = null;
    if(request.getAttribute("structure") != null){
        structure = (Structure) request.getAttribute("structure");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Wishlist";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showWishlist";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/head.inc"%>
<body>
<%@include file="/templates/header.jsp"%>

<!------------------------------------------------ WishList ----------------------------------------------------->

<div class="container py-4">
    <div class="cart-box">
        <!-- Se ci sono prodotti nella wishlist -->
        <%if (!wishlist.isEmpty()) {%>
        <div class="row justify-content-center pb-4">
            <span class="logged"><i class="fas fa-star"></i></span>
            <h3>Welcome to your wishlist</h3>
            <span class="logged"><i class="fas fa-star"></i></span>
        </div>
        <table class="table table-hover shopping-cart-wrap">
            <thead class="text-muted">
            <tr>
                <th scope="col">Product</th>
                <th scope="col" width="120">Price</th>
                <th scope="col" width="200" class="text-right">Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (ExtendedProduct ep: wishlist) {%>
            <tr>
                <td>
                    <div class="media">
                        <div class="cart-img text-center">
                            <div class="img-wrap"><img src="img/products/<%=ep.getPictureName()%>"
                                                       class="img-thumbnail img-sm"></div></div>
                        <figcaption class="media-body">
                            <h6 class="title text-truncate"><%=ep.getName()%>
                            </h6>
                            <dl class="param param-inline small">
                                <dt>Producer:</dt>
                                <dd><%=ep.getProducer()%>
                                </dd>
                            </dl>
                            <%if (ep.getDiscount() != null && ep.getDiscount() != 0) {%>
                            <dl class="param param-inline small">
                                <dt>Discount:</dt>
                                <dd><%=ep.getDiscount()%>%<i class="fas fa-piggy-bank"></i></dd>
                            </dl>
                            <%}%>
                        </figcaption>
                    </div>
                </td>
                <td>
                    <%
                        if (ep.getDiscount() != null && ep.getDiscount() != 0) {
                            BigDecimal saved = ep.getPrice().multiply(BigDecimal.valueOf(ep.getDiscount()).divide((BigDecimal.valueOf(100)))).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal discountedPrice = ep.getPrice().subtract(saved);
                    %>
                    <div class="price-wrap">
                        <var class="price"><%=discountedPrice%>
                        </var>
                        <small class="text-muted">(each)</small>
                    </div> <!-- price-wrap .// -->
                    <%
                    } else {%>
                    <div class="price-wrap">
                        <var class="price"><%=ep.getPrice()%>
                        </var>
                        <small class="text-muted">(each)</small>
                    </div> <!-- price-wrap .// -->
                    <%}%>
                </td>
                <td class="text-right">
                    <button class="btn <%=(ep.isInCart()) ? "btn-gold-active" : "btn-outline-gold"%>"
                            title="<%=(ep.isInCart()) ? "Remove from cart" : "Add to cart"%>"
                            data-toggle="tooltip"
                            data-original-title="Save to Cart"
                            onclick="<%=(ep.isInCart()) ? "removeProductFromCart(" : "addProductToCart("%><%=ep.getId() + ");"%>">
                        <i class="fas fa-shopping-basket"></i></button>
                    <button class="btn btn-outline-danger" onclick="removeProductFromWishlist(<%=ep.getId()%>)"> × Remove
                    </button>
                </td>
            </tr>
            <%}%>
            </tbody>
        </table>
        <hr>
        <div class="text-center pt-1">
            <button class="btngeneric"
                    onclick=setNavFormHome('home.Cart.showCart')>Go to shopping Cart!</button>
        </div>
        <%} else {%>
        <!-- Non ci sono prodotti nella wishlist -->
        <div class="text-center">
            <h2>There are no products in your wishlist</h2>
            <div class="container justify-content-center">
                <img src="img/error/product_not_found_once.gif" alt="product_not_found" class="rounded mx-auto d-block"
                     width="300">
            </div>
            <h4 class="text-muted font-italic" style="padding-top: 10px">Go to shop and add the products you want to buy!</h4>
        </div>
        <%}%>
    </div> <!-- card.// -->
</div>

<form method="post" id="action_product">
    <input type="hidden" name="controllerAction" value="">
    <input type="hidden" name="idProduct" value="">
    <input type="hidden" name="from" value="wishlist">
</form>

<!---------------------------------------------- End of WishList ------------------------------------------------>

<%@ include file="/templates/footer.jsp"%>
<script type="text/javascript">
    window.addEventListener("load",() =>{
    });
</script>
</body>
</html>