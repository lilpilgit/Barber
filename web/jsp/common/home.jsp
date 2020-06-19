<%@page import="model.mo.Product" %>
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

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di login/logout ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");/*null se loggato correttamente*/
    }

    /* Prendo l'ArrayList<Product> di tutti i prodotti dal parametro showcase */
    ArrayList<Product> products = null;
    if (request.getAttribute("showcase") != null) {
        products = (ArrayList<Product>) request.getAttribute("showcase");
    }

    /* Prendo il parametro per decidere cosa fare nel caso in cui la registrazione sia andata a buon fine TODO mostrare il modal di login in automatico in base al fatto che registered sia true o meno */
    Boolean registered = null;
    if (request.getAttribute("registered") != null) {
        registered = (Boolean) request.getAttribute("registered");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Home";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showHome";

%>
<!doctype html>
<html lang="en">
<%@ include file="/templates/head.jsp" %>

<body>
<%@ include file="/templates/header.jsp" %>

<!------------------------------------------- SHOWCASE --- ---------------------------------------------------------->

<% if (products.size() != 0) { %>

<div class="container-fluid ">
    <div class="row welcome text-center">
        <div class="col-12">
            <h1 class="display-4">Our Best Product!</h1>
        </div>
        <hr>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div id="myCarousel" class="carousel slide" data-ride="carousel" <%--data-interval="0"--%>>
                <!-- Carousel indicators -->
                <ol class="carousel-indicators">

                    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <% int counter = 1;
                        while (counter != products.size()) {
                            if ((counter % 4) == 0) { %>
                    <li data-target="#myCarousel" data-slide-to="1"></li>
                    <%
                            }
                            ++counter;
                        }
                    %>
                </ol>
                <!-- Wrapper for carousel items -->
                <div class="carousel-inner">
                    <% boolean firstItem = true;
                        counter = 4;
                        for (Product product : products) {
                            if ((counter % 4) == 0) {%>
                    <div class="item carousel-item <%=firstItem ? "active" : ""%>">
                        <div class="container">
                            <div class="row justify-content-center">
                                <%}%>
                                <div class="col-sm-3">
                                    <div class="thumb-wrapper">
                                        <div class="img-box">
                                            <img src="img/products/<%=product.getPictureName()%>"
                                                 alt="<%=product.getPictureName()%>">
                                        </div>
                                        <div class="thumb-content">
                                            <h4><%=product.getName()%>
                                            </h4>
                                            <%
                                                if (product.getDiscount() != null && product.getDiscount() != 0) {
                                                    BigDecimal discountedPrice = product.getPrice().subtract(product.getPrice().multiply(BigDecimal.valueOf(product.getDiscount()).divide((BigDecimal.valueOf(100)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                                            %>
                                            <p class="item-price"><span
                                                    style="text-decoration: line-through;">&euro;<%=product.getPrice()%></span>

                                                <span><%=discountedPrice%></span>
                                            </p>
                                            <%} else {%>
                                            <p class="item-price">
                                                <span>&euro;<%=product.getPrice()%></span>
                                            </p>
                                            <%} if (loggedOn && loggedUser.getType() == 'A') {%>
                                            <button class="btn btn-outline-secondary" title="Remove from showcase"
                                                    onclick=setProductForm('<%=product.getId()%>')>Remove
                                            </button>
                                            <%} else {%>
                                            <button class="btn btn-outline-secondary"
                                                    onclick=setProductForm('<%=product.getId()%>')>See Product
                                            </button>
                                            <%}%>
                                        </div>
                                    </div>
                                </div>
                                <% counter++; %>
                                <%if ((counter % 4) == 0 || (counter - 4) == products.size()) { %>
                            </div>
                        </div>
                    </div>
                    <%
                            }
                            firstItem = false;
                        }
                    %>
                </div>
                <!-- Carousel controls -->
                <a class="carousel-control left carousel-control-prev" href="#myCarousel" data-slide="prev">
                    <i class="fa fa-angle-left"></i>
                </a>
                <a class="carousel-control right carousel-control-next" href="#myCarousel" data-slide="next">
                    <i class="fa fa-angle-right"></i>
                </a>
            </div>
        </div>
    </div>
</div>

<!----------------------------------------------- Link To shop -------------------------------------------------------->
<%if (loggedOn && loggedUser.getType() == 'A') {%>
<!-- Se l'utente loggato e' amministratore, non mostro il pulsante See more...  -->
<%} else if ((loggedUser == null) || (loggedOn && (loggedUser.getType() == 'C'))) {%>
<div class="container text-center">
    <button class="btn btnheader active2" onclick="setNavFormHome('home.Shop.showShop')" type="button" id='showShop'>
        See more...
    </button>
</div>
<%}%>

<div class="container-fluid">
    <div class="row welcome text-center">
        <hr>
    </div>
</div>
<%}%>

<!-------------------------------------------------- Image Slider ----------------------------------------------------->
<div class="container text-center">
    <h1 class="display-4">Come to our salon!</h1>
</div>

<div class="container pt-2 pb-2">
    <div id="carousel" class="carousel slide container carousel-height px-4" data-ride="carousel">
        <ol class="carousel-indicators">
            <li data-target="#carousel" data-slide-to="0" class="active"></li>
            <li data-target="#carousel" data-slide-to="1"></li>
            <li data-target="#carousel" data-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="img/homepage/img1.jpg" class="d-block w-100 carousel-height" alt="Immagine_1">
            </div>
            <div class="carousel-item">
                <img src="img/homepage/img2.webp" class="d-block w-100 carousel-height" alt="Immagine_2">
            </div>
            <div class="carousel-item ">
                <img src="img/homepage/img3.jpg" class="d-block w-100 carousel-height" alt="Immagine_3">
            </div>
        </div>
        <a class="carousel-control-prev" href="#carousel" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#carousel" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
</div>

<!--------------------------------------------- Fast Cheap Reliable --------------------------------------------------->

<div class="container">
    <div class="row text-center">
        <div class="col-xs-12 col-sm-6 col-md-4">
            <img class="fast" src="https://img.icons8.com/ios-filled/64/000000/fast-track.png" alt="Fast"/>
            <h3>FAST</h3>
            <p>Guaranteed service within 30 minutes.</p>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-4">
            <img class="cheap" src="https://img.icons8.com/pastel-glyph/64/000000/dollar-coin.png" alt="cheap"/>
            <h3>CHEAP</h3>
            <p>We have the best prices on the market.</p>
        </div>
        <div class="col-sm-12 col-md-4">
            <img class="love" src="https://img.icons8.com/ios-filled/50/000000/trust.png" alt="Trusted"/>
            <h3>RELIABLE</h3>
            <p>For over 30 years, we do everything with love.</p>
        </div>
    </div>
</div>


<!-------------------------------------------------- Our Team --------------------------------------------------------->
<div class="container-fluid py-4 ">
    <div class="row text-center">
        <div class="col-12">
            <h1 class="display-4">We Are A Big Family</h1>
            <div class="container pb-4 ">
                <img class="img-thumbnail myrounded" src="img/homepage/team.jpg" height="500" alt="Team.jpg">
            </div>


            <%-- <div class="container py-4">
                 <button type="button" class="button button2" data-toggle="collapse" data-target="#demo">Check it!
                 </button>
             </div>
             <div id="demo" class="collapse">

                 <!---------------------------------- Show Team ------------------------------------->

                &lt;%&ndash; <div class="container py-3 ml-auto">
                     <div class="fixit">
                         <img class="rounded-circle profile-img" src="img/homepage/Long-Beard-Styles.jpg"
                              alt="BarbaLungaStyles.jpg">
                         <h2>Mario Rossi</h2>
                         <p>Our beard expert will amaze you with many ideas for new styles</p>
                         <br>
                     </div>
                 </div>&ndash;%&gt;
 --%>

        </div>
    </div>
</div>
</div>
<!-- form per mostrare il prodotto scelto -->
<form name="showProductForm" id="showProductForm" method="post">
    <input type="hidden" name="controllerAction" value="home.Product.showProduct">
    <input type="hidden" name="idProduct" value="">
</form>
<%@ include file="/templates/footer.html" %>
</body>
</html>
