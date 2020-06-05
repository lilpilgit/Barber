<%@page import="model.mo.Product" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.math.BigDecimal" %>
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

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di login/logout ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");/*null se loggato correttamente*/
    }

    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del prodotto nel carrello/wishlist è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "product" che mi consente di sapere qual'è il prodotto da visualizzare */
    Product product = null;
    if (request.getAttribute("product") != null) {
        product = (Product) request.getAttribute("product");
    }

    /* Prendo il parametro "inWishlist" che mi consente di sapere se il prodotto in questione si trova nella wishlist dell'utente loggato */
    Boolean inWishlist = false;
    if (request.getAttribute("inWishlist") != null) {
        inWishlist = (Boolean) request.getAttribute("inWishlist");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Shop";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showShop"; /* sto visualizzando un prodotto ma lascio attivo il bottone shop*/

%>
<!doctype html>
<html lang="en">
<head>
    <%@include file="/templates/head.jsp" %>
<body>
<%@include file="/templates/header.jsp" %>


<!--------------------------------------------- Product view ------------------------------------------------------->

<div class="container pt-5 pb-5">
    <div class="card2">
        <div class="container-fluid">
            <div class="wrapper row">
                <div class="preview col-md-6">
                    <div class="preview-pic tab-content text-center">
                        <div class="tab-pane active" id="pic-1"><img src="img/products/<%=product.getPictureName()%>"
                                                                     alt="Empty Picture"/></div>
                    </div>
                </div>
                <div class="details col-md-6">
                    <h1 class="product-title pb-4"><%=product.getName()%>
                    </h1>
                    <p class="product-description"><strong>Producer: </strong> <%=product.getProducer()%>
                    </p>
                    <hr/>
                    <p class="product-description"><strong>Description</strong><br><%=product.getDescription()%>
                    </p>
                    <hr/>
                    <%
                        if (product.getDiscount() != null && product.getDiscount() != 0) {
                            BigDecimal discountedPrice = product.getPrice().subtract(product.getPrice().multiply(BigDecimal.valueOf(product.getDiscount()).divide((BigDecimal.valueOf(100)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                    %>
                    <h4 class="price colors"><strong>Price: </strong> <span
                            class="old-price">&euro;<%=product.getPrice()%></span>&nbsp;&nbsp;&nbsp;&nbsp;<span
                            class="discounted-price">&euro;<%=discountedPrice%></span></h4>
                    <%} else {%>
                    <h4 class="price colors"><strong>Price: </strong><span>&euro;<%=product.getPrice()%></span></h4>
                    <%}%>
                    <br>
                    <% if (loggedOn && loggedUser.getType() == 'C') {%>
                    <!--COUNTER OF QUANTITY-->
                    <div class="text-center" id="counter_qta" style="height: 73px;" class='main'>
                        <button id="minus_button" class='down_count btn btn-info' title='Down'><i
                                class='fa fa-minus'></i></button>
                        <label for="quantity"></label>
                        <input id="quantity" class='counter' name="desiredQty" type="number" max="<%=product.getQuantity()%>" min="1"
                               readonly form="action_product"
                               value='1' required/>
                        <button id="plus_button" class='up_count btn btn-info' title='Up'><i class='fa fa-plus'></i>
                        </button>
                    </div>
                    <!----------------------->
                    <div class="action text-center">
                        <button class="add-to-cart btn btn-outline-gold" type="button" onclick="addProductToCart(<%=product.getId()%>)" ><span
                                class="fas fa-shopping-cart"></span></button>
                        <button class="btn like <%=(inWishlist) ? "btn-gold-active" : "btn-outline-gold"%>"
                                type="button" title="<%=(inWishlist) ? "Remove from wishlist" : "Add to wishlist"%>"
                                onclick="<%=(inWishlist) ? "removeProductFromWishlist(" : "addProductToWishlist("%><%=product.getId() + ");"%>">
                            <span class="fas fa-star"></span></button>
                    </div>
                    <%}else{%>
                    <button class="logged" type="button" id='showModal'
                            data-target="#loginModal"
                            data-toggle="modal">
                        Log in or register to buy!
                        <i class="fas fa-sign-in-alt"></i>
                    </button>
                    <%}%>
                </div>
            </div>
        </div>
    </div>
    <div class="container pt-5 text-center">
        <!-- Usare i cookie per ritornare alla pagina Shop con i parametri di prima?TODO -->
        <button class="btn btnheader active2" type="button" id='showShop'
                onclick="history.back()">
            Go back to shop
        </button>
    </div>
</div>
<form method="post" id="action_product">
    <input type="hidden" name="controllerAction" value="">
    <input type="hidden" name="idProduct" value="">
    <input type="hidden" name="from" value="product">
</form>
<%@include file="/templates/footer.html" %>
<script type="text/javascript">

    window.addEventListener("load", () => {
        handlerCounterQtaProduct("counter_qta", "minus_button", "quantity", "plus_button", <%=product.getQuantity()%>);
    });

</script>
</body>
</html>
