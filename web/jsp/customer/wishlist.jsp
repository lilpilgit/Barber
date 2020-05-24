<%@page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.User" %>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    boolean loggedOn = true; /*(Boolean) request.getAttribute("loggedOn");*/
    User loggedUser = null; /*(User) request.getAttribute("loggedUser");*/


    /*    *//* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
 * di login/logout ( se è andata a buon fine o meno) *//*
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");*//*null se loggato correttamente*//*
    }

    *//* Prendo l'ArrayList<Product> di tutti i prodotti dal parametro showcase *//*
    ArrayList<Product> products = null;
    if (request.getAttribute("showcase") != null) {
        products = (ArrayList<Product>) request.getAttribute("showcase");
    }*/

%>
<!doctype html>
<html lang="en">

<!-- RICORDARSI DI CAMBIARE IL PATH QUANDO SARA' POSSIBILE COLLEGARE LA PAGINA AL DISPATCHER -->

<head>
    <!--------------------------------------------- Meta tags --------------------------------------------------------->
    <meta charset="utf-8">

    <title>UBS United Barber Salon</title>

    <link rel="apple-touch-icon" sizes="180x180" href="../../img/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="../../img/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="../../img/favicon/favicon-16x16.png">

    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css">
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script type="text/javascript" src="../../assets/js/jquery-3.5.1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="../../assets/js/bootstrap.min.js"></script>
    <script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" data-auto-replace-svg="nest"></script>
    <link href="../../assets/css/style.css" rel="stylesheet">
    <script type="text/javascript" src="../../assets/js/main.js"></script>

</head>




<body>

<!-------------------------------------------------- Navigation in Header ------------------------------------------------------->
<header>
    <div class="container-fluid sticky-top">
        <nav class="navbar navbar-expand-md navbar-light ">
            <div class="container">
                <span class="navbar-brand"><img src="../../img/homepage/logo_jp2.png" alt="United_Barber_Salon.png"></span>

                <div class="navbar-nav ml-auto">
                    <button class="btn btnheader" type="button" id='showHome'
                            onclick="window.location.href = '/jsp/common/home.jsp'">
                        Home
                    </button>
                    <% if (loggedOn) {%>
                    <button class="btn btnheader" type="button" id='showBook'
                            onclick="window.location.href = '/jsp/common/book.jsp'">
                        Book!
                    </button>
                    <%}%>
                    <button class="btn btnheader" type="button" id='showShop'
                            onclick="window.location.href = '/jsp/common/shop.jsp'">
                        Shop
                    </button>
                    <button class="btn btnheader" type="button" id='showContact'
                            onclick="window.location.href = '/jsp/common/contact.jsp'">
                        Contact
                    </button>
                    <% if (loggedOn) {%>
                    <button class="logged" type="button" id='showCart'>
                        <i class="fas fa-shopping-basket"></i>
                    </button>
                    <button class="logged" type="button" id='showWishlist'>
                        <i class="fas fa-star"></i>
                    </button>
                    <%}%>
                    <button class="logged" type="button" id='showEdit'
                            data-target="#loginModal"
                            data-toggle="modal">
                        <i class="fas fa-user"></i>
                    </button>


                    <!-- MULTI LANGUAGE -->

                    <!--                        <div class="btn-group">
                                                <button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
                                                    <span class="flag-icon flag-icon-gb"></span> ENG
                                                </button>
                                                <div class="dropdown-menu">
                                                    <a class="dropdown-item" href="#"><span class="flag-icon flag-icon-it"></span> ITA</a>
                                                </div>
                                            </div>-->

                </div>

            </div>
        </nav>
    </div>
</header>

<!--PRESO DA https://mdbootstrap.com/docs/jquery/modals/forms/-->

<!--Modal: Login / Register Form-->
<div class="modal fade " id="loginModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-width cascading-modal" role="document">
        <!--Content-->
        <div class="modal-content">

            <!--Modal cascading tabs-->
            <div class="modal-c-tabs">

                <!-- Nav tabs -->
                <ul class="nav nav-tabs md-tabs tabs-2 light-blue darken-3" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" data-toggle="tab" href="#panel7" role="tab">
                            <i class="fas fa-user mr-1"></i>
                            Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#panel8" role="tab">
                            <i class="fas fa-user-plus mr-1"></i>
                            Register</a>
                    </li>
                </ul>

                <!-- Tab panels -->
                <div class="tab-content">
                    <!--Panel 7-->
                    <div class="tab-pane fade in show active" id="panel7" role="tabpanel">

                        <!--Body-->
                        <div class="modal-body modal-background mb-1">
                            <div class="row text-center">
                                <div class="md-form form-sm col-6">
                                    <input type="email" id="Email"
                                           name="email"
                                           class="form-control form-control-sm validate">
                                    <label data-error="wrong" data-success="right" for="Email">
                                        <i class="fas fa-envelope prefix"></i> Your email </label>
                                </div>
                                <div class="md-form form-sm col-6 mb-1">
                                    <input type="password" id="Password"
                                           name="password"
                                           class="form-control form-control-sm validate">
                                    <label data-error="wrong" data-success="right" for="Password">
                                        <i class="fas fa-lock prefix"></i> Insert password </label>
                                </div>
                            </div>
                            <div class="text-center mt-2">
                                <button class="btn btn-info"> Log in <i class="fas fa-sign-in-alt ml-1"></i></button>
                            </div>
                        </div>
                        <!--Footer-->
                        <div class="modal-footer">
                            <div class="options text-center text-md-right mt-1">
                                <p>Forgot <a href="#" class="blue-text"> Password?</a></p>
                            </div>
                        </div>

                    </div>
                    <!--/.Panel 7-->

                    <!--Panel 8-->
                    <div class="tab-pane fade" id="panel8" role="tabpanel">

                        <!--Body-->
                        <div class="modal-body">

                            <div class="row">
                                <div class="md-form form-sm col-4 text-center" >
                                    <input type="text" class="form-control" name="name" id="Name" required>
                                    <label data-error="wrong" data-success="right" for="Name">
                                        <i class="fas fa-user "></i> Name </label>
                                </div>
                                <div class="md-form form-sm col-4 text-center" >
                                    <input type="text" class="form-control" name="surname" id="Surname" required>
                                    <label data-error="wrong" data-success="right" for="Surname">
                                        <i class="fas fa-user "></i> Surname </label>
                                </div>
                                <div class="md-form form-sm col-4 text-center">
                                    <input type="email" id="modalLRInput12" class="form-control form-control-sm validate " required>
                                    <label data-error="wrong" data-success="right" for="modalLRInput12">
                                        <i class="fas fa-envelope prefix"></i> Email </label>
                                </div>
                            </div>

                            <div class="row">
                                <div class="md-form form-sm col-6 text-center">
                                    <input type="password" id="modalLRInput13"
                                           class="form-control form-control-sm validate">
                                    <label data-error="wrong" data-success="right" for="modalLRInput13">
                                        <i class="fas fa-lock prefix"></i> Password</label>
                                </div>
                                <div class="md-form form-sm col-6 text-center">
                                    <input type="password" id="modalLRInput14"
                                           class="form-control form-control-sm validate">
                                    <label data-error="wrong" data-success="right" for="modalLRInput14">
                                        <i class="fas fa-lock prefix"></i> Repeat password </label>
                                </div>
                            </div>

                            <div class="row">

                                <div class="md-form form-sm col-6 text-center">
                                    <input type="tel" id="modalLRInput15" class="form-control form-control-sm validate" required>
                                    <label data-error="wrong" data-success="right" for="modalLRInput15">
                                        <i class="fas fa-phone"></i> Phone number </label>
                                </div>
                            </div>

                            <div class="row">
                                <div class="md-form form-sm col-6 text-center">
                                    <select class="custom-select" name="state" id="State" required>
                                        <option selected disabled value="">Choose...</option>
                                        <option>ITALY</option>
                                    </select>
                                    <label for="State"><i class="fas fa-flag"></i> State</label>
                                </div>
                                <div class="md-form form-sm col-6 text-center">
                                    <select class="custom-select" name="region" id="Region" required>
                                        <option selected disabled value="">Choose...</option>
                                        <option>ABRUZZO</option>
                                        <option>BASILICATA</option>
                                        <option>CALABRIA</option>
                                        <option>EMILIA-ROMAGNA</option>
                                        <option>FRIULI-VENEZIA-GIULIA</option>
                                        <option>LAZIO</option>
                                        <option>LIGURIA</option>
                                        <option>LOMBARDIA</option>
                                        <option>MARCHE</option>
                                        <option>MOLISE</option>
                                        <option>PIEMONTE</option>
                                        <option>PUGLIA</option>
                                        <option>SARDEGNA</option>
                                        <option>SICILIA</option>
                                        <option>TOSCANA</option>
                                        <option>TRENTINO-ALTO-ADIGE</option>
                                        <option>UMBRIA</option>
                                        <option>VALLE D'AOSTA</option>
                                        <option>VENETO</option>
                                    </select>
                                    <label for="Region"><i class="fas fa-map-marker-alt"></i> Region </label>
                                </div>
                            </div>

                            <div class="row">
                                <div class="md-form form-sm col-6 text-center">
                                    <input type="text" class="form-control" name="city" id="City" required>
                                    <label data-error="wrong" data-success="right" for="modalLRInput12">
                                        <i class="fas fa-map-marker-alt"></i> City
                                    </label>
                                </div>
                                <div class="md-form form-sm col-6 text-center">
                                    <input type="number" class="form-control" name="cap" id="Cap" required min="0">
                                    <label data-error="wrong" data-success="right" for="modalLRInput15">
                                        <i class="fas fa-map-marker-alt"></i> Postal Code
                                    </label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="md-form form-sm col-6 text-center">
                                    <input type="text" class="form-control" name="street" id="Street" required>
                                    <label data-error="wrong" data-success="right" for="modalLRInput12">
                                        <i class="fas fa-map-marker-alt"></i> Street
                                    </label>
                                </div>
                                <div class="md-form form-sm col-6 text-center">
                                    <input type="number" class="form-control" name="house_number" id="House-number" min="0">
                                    <label data-error="wrong" data-success="right" for="modalLRInput15">
                                        <i class="fas fa-map-marker-alt"></i> House Number
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="text-center form-sm mt-2">
                            <button class="btn btn-info">Sign up <i class="fas fa-sign-in-alt ml-1"></i></button>
                        </div>

                    </div>
                    <!--Footer-->
                    <div class="modal-footer">
                        <div class="options text-right">
                            <p class="pt-1">Already have an account? <a href="#" class="blue-text">Log In</a></p>
                        </div>
                        <button type="button" class="btn btn-outline-info waves-effect ml-auto"
                                data-dismiss="modal">Close
                        </button>
                    </div>
                </div>
                <!--/.Panel 8-->
            </div>

        </div>
        <!--/.Content-->
    </div>
</div>
</div>

<!------------------------------------------------ Shopping Chart ----------------------------------------------------->

<!-- PRESO DA https://bootsnipp.com/snippets/O5mM8 -->

<div class="container py-4">
    <div class="cart-box">
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
                        <div class="img-wrap"><img src="../../img/products/product3.jpg" class="img-thumbnail img-sm"></div>
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
                    <button class="btn btn-outline-gold" title="Add to cart"
                            data-toggle="tooltip"
                            data-original-title="Save to Wishlist">
                        <i class="fas fa-shopping-basket"></i></button>
                    <button class="btn btn-outline-danger"> × Remove</button>
                </td>
            </tr>










            <tr>
                <td>
                    <figure class="media">
                        <div class="img-wrap"><img src="../../img/products/product3.jpg" class="img-thumbnail img-sm"></div>
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
                    <button class="btn btn-outline-gold" title="Add to cart"
                            data-toggle="tooltip"
                            data-original-title="Save to Wishlist">
                        <i class="fas fa-shopping-basket"></i></button>
                    <button class="btn btn-outline-danger"> × Remove</button>
                </td>
            </tr>
            </tbody>
        </table>
        <hr>
        <div class="text-center pt-1">
            <button class="btngeneric">Go to shopping Cart!</button>
        </div>
    </div> <!-- card.// -->
</div>
</div>


<!---------------------------------------------- End of Shopping Chart ------------------------------------------------>

<%@ include file="/templates/footer.html"%>
<script type="text/javascript">
    function onLoadHandler() {
        setButtonActive("showWishlist");
        /*
        handlerCounterQtaProduct("counter_qta", "minus_button", "quantity", "plus_button", <%--<%=product.getQuantity()%>--%>);

        */
    }

    window.addEventListener("load", onLoadHandler);
</script>

</body>
</html>
