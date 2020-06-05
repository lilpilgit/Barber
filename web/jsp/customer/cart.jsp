<%@page import="model.mo.ExtendedProduct" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.ArrayList" %>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    /* Prendo il parametro "loggedOn" che mi consente di sapere se l'utente attuale è loggato o meno */
    Boolean loggedOn = false;
    if (request.getAttribute("loggedOn") != null) {
        loggedOn = (Boolean) request.getAttribute("loggedOn");
    }

    /* Prendo il parametro "loggedUser" che mi consente di sapere qual'è l'utente attualmente loggato */
    User loggedUser = null;
    if (request.getAttribute("loggedUser") != null && loggedOn != null) {
        loggedUser = (User) request.getAttribute("loggedUser");
    }

    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    ArrayList<ExtendedProduct> cart = null;
    if (request.getAttribute("cart") != null) {
        cart = (ArrayList<ExtendedProduct>) request.getAttribute("cart");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Cart";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showCart";
%>
<!doctype html>
<html lang="en">

<%@include file="/templates/head.jsp" %>

<body>

<%@include file="/templates/header.jsp" %>
<!------------------------------------------------ Shopping Cart ----------------------------------------------------->

<!-- PRESO DA https://bootsnipp.com/snippets/O5mM8 -->

<div class="container py-4">
    <div class="cart-box">
        <div class="row justify-content-center pb-4">
            <span class="logged"><i class="fas fa-shopping-basket"></i></span>
            <h3>Welcome to your shopping cart</h3>
            <span class="logged"><i class="fas fa-shopping-basket"></i></span>
        </div>
        <table class="table table-hover shopping-cart-wrap">
            <thead class="text-muted">
            <tr>
                <th scope="col">Product</th>
                <th scope="col" width="120">Quantity</th>
                <th scope="col" width="120">Price</th>
                <th scope="col" width="200" class="text-right">Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                float totSaved = 0;
                float totPrice = 0;
                for (ExtendedProduct ep : cart) {%>
            <tr>
                <td>
                    <figure class="media">
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
                    </figure>
                </td><!-- RICORDARSI DI INCREMENTARE L'ID PER OGNI CICLO -->
                <td>
                    <!--COUNTER OF QUANTITY-->
                    <div id="counter_qta_<%=ep.getId()%>" style="height: 73px;"
                         class='main'>
                        <input id="quantity_<%=ep.getId()%>" class='counter'
                               type="number" max="<%=ep.getQuantity()%>" min="1"
                               readonly style="width: 90px;"
                               value="<%=ep.getRequiredQuantity()%>" required/>
                        <div class="row justify-content-center">
                            <button id="minus_button_<%=ep.getId()%>" class='btn'
                                    title='Down'><i
                                    class='fa fa-minus'></i></button>
                            <button id="plus_button_<%=ep.getId()%>" class='btn'
                                    title='Up'><i class='fa fa-plus'></i>
                            </button>
                        </div>
                    </div>
                    <script>
                        handlerCounterQtaProduct("counter_qta_<%=ep.getId()%>", "minus_button_<%=ep.getId()%>", "quantity_<%=ep.getId()%>", "plus_button_<%=ep.getId()%>", <%=ep.getQuantity()%>);
                    </script>
                </td>
                <td>
                    <%
                        if (ep.getDiscount() != null && ep.getDiscount() != 0) {
                            BigDecimal saved = ep.getPrice().multiply(BigDecimal.valueOf(ep.getDiscount()).divide((BigDecimal.valueOf(100)))).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal discountedPrice = ep.getPrice().subtract(saved);
                            totSaved += (saved.multiply(BigDecimal.valueOf(ep.getRequiredQuantity()))).floatValue(); /* aggiungo ai soldi totali risparmiati il parziale risparmiato */
                            System.err.println("saved:" + saved + "--- totSaved:" + totSaved);
                    %>
                    <div class="price-wrap">
                        <var class="price"><%=discountedPrice.multiply(BigDecimal.valueOf(ep.getRequiredQuantity()))%>
                        </var>
                        <small class="text-muted">(each)</small>
                    </div> <!-- price-wrap .// -->
                    <%
                        totPrice += (discountedPrice.multiply(BigDecimal.valueOf(ep.getRequiredQuantity()))).floatValue(); /* aggiungo al totale il prezzo scontato ...*/
                        System.err.println("discountedPrice:" + discountedPrice + "--- totPrice:" + totPrice);

                    } else {%>
                    <div class="price-wrap">
                        <var class="price"><%=ep.getPrice().multiply(BigDecimal.valueOf(ep.getRequiredQuantity()))%>
                        </var>
                        <small class="text-muted">(each)</small>
                    </div> <!-- price-wrap .// -->
                    <%
                            totPrice += (ep.getPrice().multiply(BigDecimal.valueOf(ep.getRequiredQuantity()))).floatValue(); /* ... oppure quello non scontato */
                            System.err.println("ep.getPrice():" + ep.getPrice() + "--- totPrice:" + totPrice);
                        }%>
                </td>
                <td class="text-right">
<%--                    <%=(ep.isInWishlist()) ? ":hover" : ""%> TODO: crea la classe e fai na roba simile--%>
                    <button class="btn <%=(ep.isInWishlist()) ? "btn-gold-active" : "btn-outline-gold"%>" title="<%=(ep.isInWishlist()) ? "Remove from wishlist" : "Add to wishlist"%>"
                            data-toggle="tooltip"
                            data-original-title="<%=(ep.isInWishlist()) ? "Delete from Wishlist" : "Save to Wishlist"%>" onclick="<%=(ep.isInWishlist()) ? "removeProductFromWishlist(" : "addProductToWishlist("%><%=ep.getId() + ");"%>">
                        <i class="fas fa-star"></i></button>

                    <button class="btn btn-outline-danger" onclick="removeProductFromCart(<%=ep.getId()%>)"> × Remove
                    </button>
                </td>
            </tr>
            <%}%>
            </tbody>
        </table>
        <%
            BigDecimal totSavedBD = BigDecimal.valueOf(totSaved).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal totPriceBD = BigDecimal.valueOf(totPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

        %>
        <div class="text-center">
            <span>You Save: </span>
            <span class="font-weight-bold"><%=totSavedBD%> &euro;</span>
            <br>
            <span>Total Price: </span>
            <span class="font-weight-bold"><%=totPriceBD%> &euro;</span>
        </div>
        <hr>
        <div class="text-center pt-1">
            <button class="btngeneric" onclick=setNavFormHome('home.Home.showCheckout')>Checkout</button>
        </div>
    </div> <!-- card.// -->
</div>
</div>
<form method="post" id="action_product">
    <input type="hidden" name="controllerAction" value="">
    <input type="hidden" name="idProduct" value="">
    <input type="hidden" name="from" value="cart">
</form>


<!---------------------------------------------- End of Shopping Chart ------------------------------------------------>

<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {

    });
</script>
</body>
</html>