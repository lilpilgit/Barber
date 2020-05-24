<%@page import="model.mo.Product" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="model.mo.User" %>
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

    /* Prendo il parametro "product" che mi consente di sapere qual'è il prodotto da visualizzare */
    Product product = null;
    if (request.getAttribute("product") != null) {
        product = (Product) request.getAttribute("product");
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

<div class="container pt-5 text-center">
    <!-- Usare i cookie per ritornare alla pagina Shop con i parametri di prima? -->
    <button class="btn btnheader active2" type="button" id='showShop'
            onclick="history.back()">
        Go back to shop
    </button>
</div>

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
                    <!--COUNTER OF QUANTITY-->
                    <div id="counter_qta" style="height: 73px;" class='main'>
                        <button id="minus_button" class='down_count btn btn-info' title='Down'><i
                                class='fa fa-minus'></i></button>
                        <label for="quantity"></label>
                        <input id="quantity" class='counter' type="number" max="<%=product.getQuantity()%>" min="1"
                               readonly
                               value='1' required/>
                        <button id="plus_button" class='up_count btn btn-info' title='Up'><i class='fa fa-plus'></i>
                        </button>
                    </div>
                    <!----------------------->
                    <div class="action">
                        <button class="add-to-cart btn btn-default" type="button">add to cart</button>
                        <button class="like btn btn-default" type="button"><span class="fa fa-heart"></span></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/templates/footer.html"%>
<script type="text/javascript">
    function onLoadHandler() {
        handlerCounterQtaProduct("counter_qta", "minus_button", "quantity", "plus_button", <%=product.getQuantity()%>);
    }

    window.addEventListener("load", onLoadHandler);
</script>
</body>
</html>
