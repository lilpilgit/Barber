<%@ page import="model.mo.Product" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="model.mo.Structure" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
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
    boolean inWishlist = false;
    if (request.getAttribute("inWishlist") != null) {
        inWishlist = (Boolean) request.getAttribute("inWishlist");
    }

    /* Prendo il parametro "inWishlist" che mi consente di sapere se il prodotto in questione si trova nella wishlist dell'utente loggato */
    boolean inCart = false;
    if (request.getAttribute("inCart") != null) {
        inCart = (Boolean) request.getAttribute("inCart");
    }

    /* Prendo l'oggetto struttura per conoscere le informazioni da mostrare nel footer */
    Structure structure = null;
    if(request.getAttribute("structure") != null){
        structure = (Structure) request.getAttribute("structure");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Shop";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showShop"; /* sto visualizzando un prodotto ma lascio attivo il bottone shop*/

%>
<!doctype html>
<html lang="en">
<%@include file="/templates/head.inc" %>
<body>
<%@include file="/templates/header.jsp" %>

<!--------------------------------------------- Product view ------------------------------------------------------->
<div class="container pt-5 text-center">
    <button type="submit" class="btn btnheader active2" id="showShop" form="goBackShop">
        Go back to shop
    </button>
</div>
<div class="container pt-5 pb-5">
    <%
        /* il prodotto nel frattempo è stato cancellato o è stato passato un prodotto non esistente ... */
        if (product == null) {%>
    <div class="card2 text-center">

        <h2>Oops! The product you were looking for doesn't exist</h2>
        <div class="container justify-content-center">

            <img src="img/error/product_not_found_once.gif" alt="product_not_found" class="rounded mx-auto d-block"
                 width="300">
        </div>
        <h4 class="text-muted font-italic" style="padding-top: 10px">You can try another search or go back to our home
            page.</h4>
    </div>
    <%} else {%>
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

                    <%if (!inCart) { /* solo se non è nel carrello mostro il contatore */%>
                    <!--COUNTER OF QUANTITY-->
                    <div class="main text-center" id="counter_qta" style="height: 73px;">
                        <button id="minus_button" class='down-count btn btn-info' title='Down'><i
                                class='fa fa-minus'></i></button>
                        <label for="maxOrderQuantity"></label>
                        <input id="maxOrderQuantity" class='counter' name="desiredQty" type="number"
                               max="<%=product.getMaxOrderQuantity()%>" min="1"
                               readonly form="action_product"
                               value='1' required/>
                        <button id="plus_button" class='up-count btn btn-info' title='Up'><i class='fa fa-plus'></i>
                        </button>
                    </div>
                    <!----------------------->
                    <%}%>
                    <div class="action text-center">
                        <button class="add-to-cart btn <%=(inCart) ? "btn-gold-active" : "btn-outline-gold"%>"
                                type="button" title="<%=(inCart) ? "Remove from cart" : "Add to cart"%>"
                                onclick="<%=(inCart) ? "removeProductFromCart(" : "addProductToCart("%><%=product.getId() + ");"%>">
                            <span class="fas fa-shopping-basket"></span></button>
                        <button class="btn like <%=(inWishlist) ? "btn-gold-active" : "btn-outline-gold"%>"
                                type="button" title="<%=(inWishlist) ? "Remove from wishlist" : "Add to wishlist"%>"
                                onclick="<%=(inWishlist) ? "removeProductFromWishlist(" : "addProductToWishlist("%><%=product.getId() + ");"%>">
                            <span class="fas fa-star"></span></button>
                    </div>
                    <%} else {%>
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
    <%}%>
</div>
<form id="goBackShop" method="post">
    <input type="hidden" name="controllerAction" value="home.Shop.showShop">
    <input type="hidden" name="filter" value="0">
    <input type="hidden" name="category" value="All">
    <input type="hidden" name="brand" value="All">
</form>

<form method="post" id="action_product">
    <input type="hidden" name="controllerAction" value="">
    <input type="hidden" name="idProduct" value="">
    <input type="hidden" name="from" value="product">
</form>
<%@include file="/templates/footer.jsp" %>
<script type="text/javascript">

    window.addEventListener("load", () => {
        <%if (product != null && loggedOn) {%>
        handlerCounterQtaProduct("minus_button", "maxOrderQuantity", "plus_button", <%=product.getMaxOrderQuantity()%>);
        <%}%>
    });

</script>
</body>
</html>
