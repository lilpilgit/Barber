<%@ page import="model.mo.ExtendedProduct" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.Structure" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
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

    /* Prendo il parametro "result" per conoscere il risultato delle operazioni possibili nella pagina del carrello */
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

    ArrayList<ExtendedProduct> cart = null;
    if (request.getAttribute("cart") != null) {
        cart = (ArrayList<ExtendedProduct>) request.getAttribute("cart");
    }

    /* Prendo l'oggetto struttura per conoscere le informazioni da mostrare nel footer */
    Structure structure = null;
    if(request.getAttribute("structure") != null){
        structure = (Structure) request.getAttribute("structure");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Cart";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showCart";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/head.inc" %>
<body>
<%@include file="/templates/header.inc" %>

<!------------------------------------------------ Shopping Cart ----------------------------------------------------->

<div class="container py-4">
    <div class="cart-box">
        <!-- Se ci sono prodotti nel carrello -->
        <%if (cart != null && !cart.isEmpty()) {%>
        <div class="row justify-content-center pb-4">
            <span class="logged"><i class="fas fa-shopping-basket"></i></span>
            <h3>Welcome to your shopping cart</h3>
            <span class="logged"><i class="fas fa-shopping-basket"></i></span>
        </div>
        <table class="table table-hover shopping-cart-wrap">
            <thead class="text-muted">
            <tr>
                <th scope="col"><input type="checkbox" id="modify_all_checkboxes" title="Check/Uncheck all"
                                       onchange="checkUncheckAll(this,'productsToBuy')"></th>
                <th scope="col">Product</th>
                <th scope="col" width="120">Quantity</th>
                <th scope="col" width="120">Price</th>
                <th scope="col" width="200" class="text-right">Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                long index_checkbox = 0;
                for (ExtendedProduct ep : cart) {%>
            <tr>
                <td class="align-middle">
                    <span class="px-1"><input type="checkbox" name="productsToBuy" id="btn_checkbox_<%=index_checkbox++%>"
                           value="<%=ep.getId()%>" onchange="modifyTotalPriceAndSaving('productsToBuy')"></span>
                </td>
                <td>
                    <div class="media">
                        <div class="cart-img text-center">
                            <div class="img-wrap"><img src="img/products/<%=ep.getPictureName()%>"
                                                       class="img-thumbnail img-sm" alt="product_picture"></div>
                        </div>
                        <figcaption class="media-body">
                            <h6 class="title text-truncate"><%=ep.getName()%>
                                <input type="hidden" readonly id="productName_<%=ep.getId()%>"
                                       value="<%=ep.getName()%>">
                            </h6>
                            <dl class="param param-inline small">
                                <dt>Producer:</dt>
                                <dd><%=ep.getProducer()%>
                                </dd>
                            </dl>
                            <%if (ep.getDiscount() != null && ep.getDiscount() != 0) {%>
                            <dl class="param param-inline small">
                                <dt>Discount:</dt>
                                <dd><%=ep.getDiscount()%> %<i class="fas fa-piggy-bank"></i></dd>
                                <input type="hidden" readonly id="eachDiscount_<%=ep.getId()%>" value="<%=ep.getDiscount()%>">
                            </dl>
                            <%}else{%>
                            <input type="hidden" readonly id="eachDiscount_<%=ep.getId()%>" value="0">
                            <%}%>
                        </figcaption>
                    </div>
                </td><!-- RICORDARSI DI INCREMENTARE L'ID PER OGNI CICLO -->
                <td>
                    <!--COUNTER OF QUANTITY-->
                    <div id="counter_qta_<%=ep.getId()%>" style="height: 73px;"
                         class='main'>
                        <input id="quantity_<%=ep.getId()%>" class='counter'
                               type="number" max="<%=ep.getMaxOrderQuantity()%>" min="1"
                               readonly style="width: 90px;"
                               value="<%=ep.getRequiredQuantity()%>" required/>
                        <div class="row justify-content-center">
                            <button id="minus_button_<%=ep.getId()%>" class="btn"
                                    title="Down" onclick="changeQuantityProductInCart('decrease', 'quantity_<%=ep.getId()%>',<%=ep.getMaxOrderQuantity()%>,<%=ep.getId()%>,'productsToBuy')"><i
                                    class="fa fa-minus"></i></button>
                            <button id="plus_button_<%=ep.getId()%>" class="btn plus-cart"
                                    title="Up" onclick="changeQuantityProductInCart('increase','quantity_<%=ep.getId()%>',<%=ep.getMaxOrderQuantity()%>,<%=ep.getId()%>,'productsToBuy')"><i
                                    class="fa fa-plus"></i>
                            </button>
                        </div>
                    </div>
                </td>
                <td>
                    <!-- mi interessa per il calcolo con javascript il prezzo già scontato pertanto lo salvo in tale campo hidden -->
                    <input type="hidden" id="eachOriginalPrice_<%=ep.getId()%>" value="<%=ep.getPrice()%>">
                    <%
                        if (ep.getDiscount() != null && ep.getDiscount() != 0) {
                            BigDecimal saved = ep.getPrice().multiply(BigDecimal.valueOf(ep.getDiscount()).divide((BigDecimal.valueOf(100)))).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal discountedPrice = ep.getPrice().subtract(saved);
                    %>
                    <div class="price-wrap">
                        <var class="price"><%=discountedPrice%>
                        </var>
                        <input type="hidden" readonly id="eachFinalPrice_<%=ep.getId()%>" value="<%=discountedPrice%>">
                        <small class="text-muted">(each)</small>
                    </div> <!-- price-wrap .// -->
                    <%
                    } else {%>
                    <div class="price-wrap">
                        <var class="price"><%=ep.getPrice()%>
                        </var>
                        <input type="hidden" readonly id="eachFinalPrice_<%=ep.getId()%>" value="<%=ep.getPrice()%>">
                        <small class="text-muted">(each)</small>
                    </div> <!-- price-wrap .// -->
                    <%}%>
                </td>
                <td class="text-right">
                    <button class="btn <%=(ep.isInWishlist()) ? "btn-gold-active" : "btn-outline-gold"%>"
                            title="<%=(ep.isInWishlist()) ? "Remove from wishlist" : "Add to wishlist"%>"
                            data-toggle="tooltip"
                            data-original-title="<%=(ep.isInWishlist()) ? "Delete from Wishlist" : "Save to Wishlist"%>"
                            onclick="<%=(ep.isInWishlist()) ? "removeProductFromWishlist(" : "addProductToWishlist("%><%=ep.getId() + ");"%>">
                        <i class="fas fa-star"></i></button>

                    <button class="btn btn-outline-danger" onclick="removeProductFromCart(<%=ep.getId()%>)"> × Remove
                    </button>
                </td>
            </tr>
            <%}%>
            </tbody>
        </table>
        <div class="text-center">
            <span>You Save: </span>
            <span id="totalSavedSpan" class="font-weight-bold">0.00 &euro;</span>
            <input type="hidden" readonly id="totalSaved" value="0">
            <br>
            <span>Total Price: </span>
            <span id="totalPriceSpan" class="font-weight-bold">0.00 &euro;</span>
            <input type="hidden" readonly id="totalPrice" value="0">
        </div>
        <hr>
        <div class="text-center pt-1">
            <button class="btngeneric" onclick=goToCheckout('productsToBuy')>Checkout</button>
        </div>
        <%} else {%>
        <!-- Non ci sono prodotti nel carrello -->
        <div class="text-center">
            <h2>There are no products in your cart :(</h2>
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
    <input type="hidden" name="from" value="cart">
</form>

<form method="post" id="action_checkout">
    <input type="hidden" name="controllerAction" value="home.Checkout.showCheckout">
    <input type="hidden" value="" name="checkoutInfo" id="checkoutInfo">
</form>

<!---------------------------------------------- End of Shopping Chart ------------------------------------------------>

<%@ include file="/templates/footer.inc" %>
<script type="text/javascript">
    window.addEventListener("load", () => {
        modifyTotalPriceAndSaving('productsToBuy');
    });
</script>
</body>
</html>