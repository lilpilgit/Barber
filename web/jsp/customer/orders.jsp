<%@page import="functions.StaticFunc" %>
<%@ page import="model.mo.ExtendedProduct" %>
<%@ page import="model.mo.Order" %>
<%@ page import="model.mo.User" %>
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
        <% int numberCollapse = 0;
            for (Order order : orders) {
        %>
        <div class="card">
            <div class="card-header " id="heading_<%=numberCollapse%>">
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
                                  boolean allowCancelOrder = false;
                                  String valueProgressBar = "0";
                                  boolean canceled = false; /*TODO settare colore rosso di sfondo per ordini cancellati*/
                                  if (order.getStatus().equals(StaticFunc.NOTHING_NEW)) {
                                      valueProgressBar = "0";
                                      allowCancelOrder = true; /* è ancora in tempo per cancellarlo */
                                  } else if (order.getStatus().equals(StaticFunc.PROCESSING)) {
                                      valueProgressBar = "25";
                                      allowCancelOrder = true; /* è ancora in tempo per cancellarlo */
                                  } else if (order.getStatus().equals(StaticFunc.SENT)) {
                                      valueProgressBar = "50";
                                  } else if (order.getStatus().equals(StaticFunc.DELIVERING)) {
                                      valueProgressBar = "75";
                                  } else if (order.getStatus().equals(StaticFunc.DELIVERED)) {
                                      valueProgressBar = "100";
                                  } else if (order.getStatus().equals(StaticFunc.CANCELED)) {
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
                                    data-target="#collapse_<%=numberCollapse%>"
                                    title="Show ordered products " aria-expanded="true"
                                    aria-controls="collapse_<%=numberCollapse%>>">
                                Show
                            </button>
                        </div>
                    </div>
                </h6>
            </div>
            <div id="collapse_<%=numberCollapse%>" class="collapse" aria-labelledby="heading_<%=numberCollapse%>>"
                 data-parent="#accordion">
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
                            <%for (ExtendedProduct item : order.getItemList()) {%>
                            <tr>
                                <!-- ATTENZIONE PER ESSERE CORRETTI, IL FETCH DEI PRODOTTI DOVREBBE INCLUDERE ANCHE QUELLI CANCELLATI -->
                                <td>
                                    <div class="media">
                                        <div class="img-wrap"><img src="img/products/<%=item.getPictureName()%>"
                                                                   class="img-thumbnail img-sm"></div>
                                        <figcaption class="media-body pl-1">
                                            <h6 class="title text-truncate"><%=item.getName()%>
                                            </h6>
                                            <dl class="param param-inline small">
                                                <dt>Producer: <%=item.getProducer()%>
                                                </dt>
                                            </dl>
                                        </figcaption>
                                    </div>
                                </td>
                                <td class="text-center align-middle">
                                    <span class="font-weight-bold"><h4><%=item.getRequiredQuantity()%></h4></span>
                                </td>

                                <td class="text-right align-middle">
                                    <button class="btn btn-outline-secondary" title="See more about product"
                                            onclick="showProductFromOrder(<%=item.getId()%>)">Show more
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
                            <%if (valueProgressBar.equals("100")) {%>
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
                            <%if (allowCancelOrder) {%>
                            <tr class="text-center">
                                <td colspan="100%">
                                    <span>
                            <!-- TODO SE LA BARRA DI PROGRESSO E' SUPERIORE AL 25% NON MOSTRARE IL BOTTONE! PERCHE' SIGNIFICA CHE E' STATO SPEDITO-->
                                    <button class="btn btn-danger"
                                            data-target="#alertDeleteOrder"
                                            data-toggle="modal"
                                            onclick="setTmpId(<%=order.getId()%>);"
                                            title="Please.. Don't do this!!!">Cancel Order</button>
                            </span>
                                </td>
                            </tr>
                            <%}%>
                            </tfoot>
                            <!-------------------------------- END OF TABLE FOOTER ------------------------------------>
                        </table>
                    </div> <!-- card.// -->
                </div>
                <!--container end.//-->
            </div>
        </div>
        <%
                numberCollapse++;
            }/* fine del ciclo for per quanto riguarda ciascun ordine*/%>

    </div>
</div>


<input type="hidden" id="tmpIdDel" value="">
<!--MODAL DI CONFERMA ELIMINAZIONE ORDINE-->
<div class="modal fade" id="alertDeleteOrder" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle" style="color: rgba(211,4,0,0.75)">You are canceling
                    an order...</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                You are attempting to permanently cancel an order.<br><br>Are you sure you want to continue?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>


                <!-- TODO AGGIORNARE CON FUNZIONE GENERICA deleteById contenuta in admin.js come con show-customers -->


                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="cancelOrder(document.getElementById('tmpIdDel').value)">Cancel
                    order
                </button>
            </div>
        </div>
    </div>
</div>
<!--FINE MODAL DI CONFERMA ELIMINAZIONE ORDINE-->


<form method="post" id="order_action">
    <input type="hidden" name="controllerAction" value="">
    <input type="hidden" name="idProduct" value="">
    <input type="hidden" name="idOrder" value="">
</form>

<!---------------------------------------------- End of Book section ------------------------------------------------>

<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {

    });


</script>
</body>
</html>