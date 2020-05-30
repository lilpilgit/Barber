<%@page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>
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

    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Cart";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showCart";
%>
<!doctype html>
<html lang="en">

<%@include file="/templates/head.jsp"%>

<body>

<%@include file="/templates/header.jsp"%>
<!------------------------------------------------ Shopping Chart ----------------------------------------------------->

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
            <tr>
                <td>
                    <figure class="media">
                        <div class="img-wrap"><img src="img/products/product3.jpg" class="img-thumbnail img-sm"></div>
                        <figcaption class="media-body">
                            <h6 class="title text-truncate">Product name goes here </h6>
                            <dl class="param param-inline small">
                                <dt>Producer: </dt>
                                <dd>Avaha</dd>
                            </dl>
                            <dl class="param param-inline small">
                                <dt>Discount? </dt>
                                <dd><i class="fas fa-piggy-bank"></i></dd>
                            </dl>
                        </figcaption>
                    </figure>
                </td><!-- RICORDARSI DI INCREMENTARE L'ID PER OGNI CICLO -->
                <td>
                    <!--COUNTER OF QUANTITY-->
                    <div id="counter_qta<!-- RICORDARSI DI INCREMENTARE L'ID PER OGNI CICLO -->" style="height: 73px;" class='main'>
                        <input id="quantity<!-- RICORDARSI DI INCREMENTARE L'ID PER OGNI CICLO -->" class='counter' type="number" <%--max="<%=product.getQuantity()%>"--%> min="1"
                               readonly style="width: 90px;"
                               value='1' required/>
                        <div class="row justify-content-center">
                            <button id="minus_button<!-- RICORDARSI DI INCREMENTARE L'ID PER OGNI CICLO -->" class='btn' title='Down'><i
                                    class='fa fa-minus'></i></button>
                            <button id="plus_button<!-- RICORDARSI DI INCREMENTARE L'ID PER OGNI CICLO -->" class='btn' title='Up'><i class='fa fa-plus'></i>
                            </button>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="price-wrap">
                        <var class="price">USD 145</var>
                        <small class="text-muted">(USD5 each)</small>
                    </div> <!-- price-wrap .// -->
                </td>
                <td class="text-right">
                    <button class="btn btn-outline-gold" title="Add to wishlist"
                            data-toggle="tooltip"
                            data-original-title="Save to Wishlist">
                        <i class="fas fa-star"></i></button>
                    <button class="btn btn-outline-danger"> × Remove</button>
                </td>
            </tr>










            <tr>
                <td>
                    <figure class="media">
                        <div class="img-wrap"><img src="img/products/product3.jpg" class="img-thumbnail img-sm"></div>
                        <figcaption class="media-body">
                            <h6 class="title text-truncate">Product name goes here </h6>
                            <dl class="param param-inline small">
                                <dt>Producer: </dt>
                                <dd>Avaha</dd>
                            </dl>
                            <dl class="param param-inline small">
                                <dt>Discount? </dt>
                                <dd><i class="fas fa-piggy-bank"></i></dd>
                            </dl>
                        </figcaption>
                    </figure>
                </td>
                <td>
                    <!--COUNTER OF QUANTITY-->
                    <div id="counter_qta" style="height: 73px;" class='main'>
                        <input id="quantity" class='counter' type="number" <%--max="<%=product.getQuantity()%>"--%> min="1"
                               readonly style="width: 90px;"
                               value='1' required/>
                        <div class="row justify-content-center">
                            <button id="minus_button" class='btn' title='Down'><i
                                    class='fa fa-minus'></i></button>
                            <button id="plus_button" class='btn' title='Up'><i class='fa fa-plus'></i>
                            </button>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="price-wrap">
                        <var class="price">USD 145</var>
                        <small class="text-muted">(USD5 each)</small>
                    </div> <!-- price-wrap .// -->
                </td>
                <td class="text-right">
                    <button class="btn btn-outline-gold" title="Add to wishlist"
                            data-toggle="tooltip"
                            data-original-title="Save to Wishlist">
                        <i class="fas fa-star"></i></button>
                    <button class="btn btn-outline-danger"> × Remove</button>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="text-center">
            <span>You Save: </span>
            <span class="font-weight-bold">5 &euro;</span>
            <br>
            <span>Total Price: </span>
            <span class="font-weight-bold">90 &euro;</span>
        </div>
        <hr>
        <div class="text-center pt-1">
            <button class="btngeneric" onclick=setNavFormHome('Home.showCheckout')>Checkout</button>
        </div>
    </div> <!-- card.// -->
</div>
</div>


<!---------------------------------------------- End of Shopping Chart ------------------------------------------------>

<%@ include file="/templates/footer.html"%>
<script type="text/javascript">
    window.addEventListener("load",() =>{
    });
</script>
</body>
</html>