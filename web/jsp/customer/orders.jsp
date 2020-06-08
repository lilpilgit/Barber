<%@page import="functions.StaticFunc" %>
<%@ page import="model.mo.Order" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.ExtendedProduct" %>

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

    ArrayList<Order> orders = null;
    if (request.getAttribute("orders") != null) {
        orders = (ArrayList<Order>) request.getAttribute("orders");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Orders";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showOrders";

%>
<!doctype html>
<html lang="en">

<%@include file="/templates/head.jsp" %>

<body>

<%@include file="/templates/header.jsp" %>
<!------------------------------------------------ Book section ----------------------------------------------------->

<div class="container my-4" style="background-color: #f1e7cb; border-radius: 25px; min-height: 500px;">

    <span class="text-center"><h2><br>HERE YOU CAN FIND YOUR ORDERS</h2></span>

    <!-- PRESO DA https://getbootstrap.com/docs/4.0/components/collapse/ -->

    <!-- RICORDARSI DI INCREMENTARE GLI ID PER OGNI CARD -->
    <!-- STARE ATTENTI AL VALORE data-target CHE DEVE CORRISPONDERE CON L'ID DEL CONTENUTO DELLA CARD -->
    <div class="container px-3 py-4" id="accordion">
        <%for (Order order : orders) {%>
        <div class="card">
            <div class="card-header " id="headingOne">
                <h6 class="mb-0 ">

                    <div class="row">
                        <div class="col pt-2">ORDER ID: #<%=order.getId()%>#</div>
                        <div class="col pt-2">ORDER DATE: <%=order.getOrderDate()%>
                        </div>
                        <div class="col-5 pt-2"><span class="float-left pr-3">STATUS:</span><span class="progress">
                            <!-- PER MODIFICARE STATO AVANZAMENTO CAMBIARE width: 0/25/50/75/100 e aria-valuenow= 0/25/50/75/100 -->
                                <%--0 = nothing-new--%>
                                <%--25 = processing--%>
                                <%--50 = sent--%>
                                <%--75 = delivering--%>
                                <%--100 = delivered--%>
                                <%-- deleted --%>
                                <%-- annulled --%>
                              <%
                                  String valueProgressBar = "0";
                                  boolean canceled = false;
                                  if (order.getStatus().equals(StaticFunc.NOTHING_NEW)) {
                                      valueProgressBar = "0";
                                  } else if (order.getStatus().equals(StaticFunc.PROCESSING)) {
                                      valueProgressBar = "25";
                                  } else if (order.getStatus().equals(StaticFunc.SENT)) {
                                      valueProgressBar = "50";
                                  } else if(order.getStatus().equals(StaticFunc.DELIVERING)) {
                                      valueProgressBar = "75";
                                  } else if(order.getStatus().equals(StaticFunc.DELIVERED)) {
                                      valueProgressBar = "100";
                                  } else if(order.getStatus().equals(StaticFunc.CANCELED)) {
                                      canceled = true;
                                  }
                              %>
                            <span class="progress-bar" role="progressbar" style="width: <%=valueProgressBar%>%"
                                  aria-valuenow="<%=valueProgressBar%>"
                                  aria-valuemin="0" aria-valuemax="100"></span>
                         </span>
                        </div>
                        <div class="col-auto py-0">
                            <button class="btn btn-outline-secondary float-right" data-toggle="collapse"
                                    data-target="#collapseOne"
                                    title="show ordered products " aria-expanded="true" aria-controls="collapseOne">
                                Show
                            </button>
                        </div>
                    </div>
                </h6>
            </div>
            <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordion">
                <div class="card-body pt-3">
                    <div class="card pt-4">
                        <table class="table table-hover shopping-cart-wrap col-8 align-self-center">
                            <thead class="text-muted">
                            <tr>
                                <th scope="col">Product</th>
                                <th scope="col" class="text-center">Ordered Quantity</th>
                                <th scope="col"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <%for (ExtendedProduct item : order.getItemList()){%>
                            <tr>
                                <!-- ATTENZIONE PER ESSERE CORRETTI, IL FETCH DEI PRODOTTI DOVREBBE INCLUDERE ANCHE QUELLI CANCELLATI -->
                                <td>
                                    <div class="media">
                                        <div class="img-wrap"><img src="img/products/<%=item.getPictureName()%>"
                                                                   class="img-thumbnail img-sm"></div>
                                        <figcaption class="media-body pl-1">
                                            <h6 class="title text-truncate"><%=item.getName()%></h6>
                                            <dl class="param param-inline small">
                                                <dt>Producer: <%=item.getProducer()%></dt>
                                            </dl>
                                        </figcaption>
                                    </div>
                                </td>
                                <td class="text-center align-middle">
                                    <span class="font-weight-bold"><h4><%=item.getRequiredQuantity()%></h4></span>
                                </td>

                                <td class="text-right align-middle">
                                    <button class="btn btn-outline-secondary" title="See more about product">Show more
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                            <%} /* fine del ciclo for per quanto riguarda ciascun item dell'ordine*/%>
                            <!----------------------------------- TABLE FOOTER ---------------------------------------->
                            <tfoot>
                            <tr>
                                <td colspan="100%">
                                    <span><b>SHIPPING ADDRESS:</b><%=order.getShippingAddress()%></span>
                                </td>
                            </tr>
                            <!--la sell date va mostrata solo se il prodotto è stato spedito-->
                            <%if(valueProgressBar.equals("100")){%>
                            <tr>
                                <td colspan="100%">
                                    <span><b>SELL DATE:</b><%=order.getSellDate()%></span>
                                </td>
                            </tr>
                            <%}%>
                            <tr>
                                <td colspan="100%">
                                    <span><b>TOTAL PRICE:</b> <%=order.getTotPrice()%> &euro;</span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="100%">
                                    <span><b>STATUS ORDER:</b> <%=order.getStatus()%></span>
                                </td>
                            </tr>
                            <tr class="text-center">
                                <td colspan="100%">
                                    <span>
                            <!-- TODO SE LA BARRA DI PROGRESSO E' SUPERIORE AL 25% NON MOSTRARE IL BOTTONE! PERCHE' SIGNIFICA CHE E' STATO SPEDITO-->
                                    <button class="btn btn-danger"
                                            title="Please.. Don't do this!!!">Cancel Order</button>
                            </span>
                                </td>
                            </tr>
                            </tfoot>
                            <!-------------------------------- END OF TABLE FOOTER ------------------------------------>
                        </table>
                    </div> <!-- card.// -->
                </div>
                <!--container end.//-->
            </div>
        </div>
        <%}/* fine del ciclo for per quanto riguarda ciascun ordine*/%>

        <!-- CANCELLA DA QUI -------------------------------------------------------------------------------->
        <div class="card">
            <div class="card-header " id="heading2">
                <h6 class="mb-0 ">
                    <div class="row">
                        <div class="col pt-2">ORDER ID: --inserire id--</div>
                        <div class="col pt-2">ORDER DATE: 2020-12-12</div>
                        <div class="col-5 pt-2"><span class="float-left pr-3">STATUS:</span><span class="progress">
                            <!-- PER MODIFICARE STATO AVANZAMENTO CAMBIARE width: 0/25/50/75/100 e aria-valuenow= 0/25/50/75/100 -->
                            <span class="progress-bar" role="progressbar" style="width: 75%" aria-valuenow="75"
                                  aria-valuemin="0" aria-valuemax="100"></span>
                         </span>
                        </div>
                        <div class="col-auto py-0">
                            <button class="btn btn-outline-secondary float-right" data-toggle="collapse"
                                    data-target="#collapse2"
                                    title="show ordered products " aria-expanded="true" aria-controls="collapse2">
                                Show
                            </button>
                        </div>
                    </div>
                </h6>
            </div>
            <div id="collapse2" class="collapse" aria-labelledby="headingOne" data-parent="#accordion">
                <div class="card-body pt-3">
                    <div class="card pt-4">
                        <table class="table table-hover shopping-cart-wrap col-8 align-self-center">
                            <thead class="text-muted">
                            <tr>
                                <th scope="col">Product</th>
                                <th scope="col" class="text-center">Ordered Quantity</th>
                                <th scope="col"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    <div class="media">
                                        <div class="img-wrap"><img src="img/products/product3.jpg"
                                                                   class="img-thumbnail img-sm"></div>
                                        <figcaption class="media-body pl-1">
                                            <h6 class="title text-truncate">Product name goes here </h6>
                                            <dl class="param param-inline small">
                                                <dt>Producer:</dt>
                                            </dl>
                                        </figcaption>
                                    </div>
                                </td>
                                <td class="text-center align-middle">
                                    <span class="font-weight-bold"><h4>8</h4></span>
                                </td>

                                <td class="text-right align-middle">
                                    <button class="btn btn-outline-secondary" title="See more about product">Show more
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="media">
                                        <div class="img-wrap"><img src="img/products/product3.jpg"
                                                                   class="img-thumbnail img-sm"></div>
                                        <figcaption class="media-body pl-1">
                                            <h6 class="title text-truncate">Product name goes here </h6>
                                            <dl class="param param-inline small">
                                                <dt>Producer:</dt>
                                            </dl>
                                        </figcaption>
                                    </div>
                                </td>
                                <td class="text-center align-middle">
                                    <span class="font-weight-bold"><h4>8</h4></span>
                                </td>

                                <td class="text-right align-middle">
                                    <button class="btn btn-outline-secondary" title="See more about product">Show more
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="media">
                                        <div class="img-wrap"><img src="img/products/product3.jpg"
                                                                   class="img-thumbnail img-sm"></div>
                                        <figcaption class="media-body pl-1">
                                            <h6 class="title text-truncate">Product name goes here </h6>
                                            <dl class="param param-inline small">
                                                <dt>Producer:</dt>
                                            </dl>
                                        </figcaption>
                                    </div>
                                </td>
                                <td class="text-center align-middle">
                                    <span class="font-weight-bold"><h4>8</h4></span>
                                </td>

                                <td class="text-right align-middle">
                                    <button class="btn btn-outline-secondary" title="See more about product">Show more
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="media">
                                        <div class="img-wrap"><img src="img/products/product3.jpg"
                                                                   class="img-thumbnail img-sm"></div>
                                        <figcaption class="media-body pl-1">
                                            <h6 class="title text-truncate">Product name goes here </h6>
                                            <dl class="param param-inline small">
                                                <dt>Producer:</dt>
                                            </dl>
                                        </figcaption>
                                    </div>
                                </td>
                                <td class="text-center align-middle">
                                    <span class="font-weight-bold"><h4>8</h4></span>
                                </td>

                                <td class="text-right align-middle">
                                    <button class="btn btn-outline-secondary" title="See more about product">Show more
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="media">
                                        <div class="img-wrap"><img src="img/products/product3.jpg"
                                                                   class="img-thumbnail img-sm"></div>
                                        <figcaption class="media-body pl-1">
                                            <h6 class="title text-truncate">Product name goes here </h6>
                                            <dl class="param param-inline small">
                                                <dt>Producer:</dt>
                                            </dl>
                                        </figcaption>
                                    </div>
                                </td>
                                <td class="text-center align-middle">
                                    <span class="font-weight-bold"><h4>8</h4></span>
                                </td>

                                <td class="text-right align-middle">
                                    <button class="btn btn-outline-secondary" title="See more about product">Show more
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="100%">
                                    <span><b>SHIPPING ADDRESS:</b> Italy Veneto Padova Via bianconiglio 12 45080 </span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="100%">
                                    <span><b>SELL DATE:</b> 2020-12-12 </span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="100%">
                                    <span><b>TOTAL PRICE:</b> 9000 &euro;</span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="100%">
                                    <span><b>STATUS ORDER:</b> spedito</span>
                                </td>
                            </tr>
                            <tr class="text-center">
                                <td colspan="100%">
                                    <span>
                            <!-- TODO SE LA BARRA DI PROGRESSO E' SUPERIORE AL 25% NON MOSTRARE IL BOTTONE! PERCHE' SIGNIFICA CHE E' STATO SPEDITO-->
                                    <button class="btn btn-danger"
                                            title="Please.. Don't do this!!!">Cancel Order</button>
                            </span>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div> <!-- card.// -->
                </div>
                <!--container end.//-->
            </div>
        </div>
        <!-- FINO A QUI -------------------------------------------------------------------------------->


    </div>
</div>

<!---------------------------------------------- End of Book section ------------------------------------------------>

<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {
    });
</script>
</body>
</html>