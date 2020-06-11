<%@ page import="functions.StaticFunc" %>
<%@ page import="model.mo.Order" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del nuovo cliente è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di cancellazione/modifica ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Prendo l'array list di ordini con i parametri relativi al cliente */
    ArrayList<Order> logisticOrders = null;
    if (request.getAttribute("logisticOrders") != null) {
        logisticOrders = (ArrayList<Order>) request.getAttribute("logisticOrders");
    }
    boolean areOrders = false;
    if (logisticOrders != null && logisticOrders.size() != 0)
        areOrders = true;

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Logistics";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showLogistics";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="../../templates/admin-sidebar.jsp" %>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <span class="text-center"><h4>Customer's orders</h4></span>

        <div class="row justify-content-center">
            <div class="col-auto">
                <%--        <%if (areEmployees) {%>--%>
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">ID</th>
                        <th scope="col">Customer Email</th>
                        <th scope="col">Customer Name</th>
                        <th scope="col">Customer Phone</th>
                        <th scope="col">Status</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int i = 1; /* contatore per il numero di ordini */
                        if (areOrders) {
                            for (Order l : logisticOrders) {
                    %>
                    <tr>
                        <th scope="row"><%=i++%>
                        </th>
                        <td><%=l.getId()%>
                        </td>
                        <td><%=l.getCustomer().getEmail()%>
                        </td>
                        <td><%=l.getCustomer().getName()%> , <%=l.getCustomer().getSurname()%>
                        </td>
                        <td><%=l.getCustomer().getPhone()%>
                        </td>
                        <td><%=l.getStatus()%>
                        </td>
                        <td>
                            <button type="button" class="tablebutton" style="color: #1ae2dd;"
                                    data-target="#alertSetStatusOrder"
                                    data-toggle="modal"
                                    onclick="setTmpId(<%=l.getId()%>,'tmpIdStatus');"><i
                                    class="fas fa-pencil-alt"></i>
                            </button>

                        </td>
                    </tr>
                    <%}%>
                    </tbody>
                </table>
            </div>
        </div>
        <%
        } else {
        %>
        <h1>Non ci sono ordini mi disp...</h1>
        <%}%>
    </main>
</div>

<input type="hidden" id="tmpIdStatus" value="">
<!--MODAL DI CONFERMA MODIFICA STATUS -->
<div class="modal fade" id="alertSetStatusOrder" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle" style="color: rgba(211,4,0,0.75)">You are modifying
                    status of order...</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                You are attempting to modify status of order.<br><br>Select one of this possible status:<br><br>

                <input type="radio" id="nothing_new" name="status" value="<%=StaticFunc.NOTHING_NEW%>" checked="true">
                <label for="nothing_new"><%=StaticFunc.NOTHING_NEW%>
                </label><br>
                <input type="radio" id="processing" name="status" value="<%=StaticFunc.PROCESSING%>">
                <label for="processing"><%=StaticFunc.PROCESSING%>
                </label><br>
                <input type="radio" id="sent" name="status" value="<%=StaticFunc.SENT%>">
                <label for="sent"><%=StaticFunc.SENT%>
                </label><br>
                <input type="radio" id="delivering" name="status" value="<%=StaticFunc.DELIVERING%>">
                <label for="delivering"><%=StaticFunc.DELIVERING%>
                </label><br>
                <input type="radio" id="delivered" name="status" value="<%=StaticFunc.DELIVERED%>">
                <label for="delivered"><%=StaticFunc.DELIVERED%>
                </label><br>
                <input type="radio" id="canceled" name="status" value="<%=StaticFunc.CANCELED%>">
                <label for="canceled"><%=StaticFunc.CANCELED%>
                </label><br>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>


                <!-- TODO AGGIORNARE CON FUNZIONE GENERICA deleteById contenuta in admin.js come con show-customers -->


                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="modifyStatusOrder(document.getElementById('tmpIdStatus').value,'status')">Modify status
                </button>
            </div>
        </div>
    </div>
</div>
<!--FINE MODAL DI CONFERMA MODIFICA STATUS -->
<form method="post" id="order_action">
    <input type="hidden" name="controllerAction" value="">
    <input type="hidden" name="idOrder" value="">
    <input type="hidden" name="status" value="">
</form>

<script>
    window.addEventListener("load", () => {

    })
</script>
</body>
</html>

