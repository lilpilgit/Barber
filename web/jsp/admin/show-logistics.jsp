<%@ page import="functions.StaticFunc" %>
<%@ page import="model.mo.Order" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static services.config.Configuration.TOT_REC_TO_SHOW_LOGISTICS" %>
<%@ page contentType="text/html;charset=UTF-8"%>
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

    /* Prendo il parametro per sapere il numero di pagina da visualizzare al momento */
    Long currentPage = 1L;
    if (request.getAttribute("currentPage") != null) {
        currentPage = (Long) request.getAttribute("currentPage");
    }

    Long previousPage = currentPage - 1;
    Long nextPage = currentPage + 1;

    /* Prendo il parametro per sapere il numero totale di pagine da visualizzare*/
    Long totalNumberOfPages = 1L;
    if (request.getAttribute("totalNumberOfPages") != null) {
        totalNumberOfPages = (Long) request.getAttribute("totalNumberOfPages");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Logistics";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showLogistics";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.inc" %>
<body>
<%@include file="../../templates/admin-sidebar.inc" %>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <h3 class="text-center"><b>CUSTOMER'S ORDERS</b></h3>
        <div class="row justify-content-center">
            <% if (areOrders) { %>
            <div class="col-auto">
                    <table class="table table-hover table-bordered">
                        <thead class="thead-dark">
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
                        <tbody class="fixedheight">
                        <%
                            int i = (int) (((currentPage - 1) * TOT_REC_TO_SHOW_LOGISTICS) + 1); /* contatore per il numero di ordini */
                            String popoverSellDate = "";
                            /*if (areOrders) {*/
                            for (Order l : logisticOrders) {
                                int valueStatus = 0; /* valore intero corrispondente all'attuale status tra i valori 0/25/50/75/100/-1*/
                                boolean canceled = false; /* flag per sapere se è cancellato */
                                boolean delivered = false; /* flag per sapere se è stato consegnato dunque ordine completato */
                                if (l.getStatus().equals(StaticFunc.NOTHING_NEW)) {
                                    valueStatus = 0;
                                } else if (l.getStatus().equals(StaticFunc.PROCESSING)) {
                                    valueStatus = 25;
                                } else if (l.getStatus().equals(StaticFunc.SENT)) {
                                    valueStatus = 50;
                                } else if (l.getStatus().equals(StaticFunc.DELIVERING)) {
                                    valueStatus = 75;
                                } else if (l.getStatus().equals(StaticFunc.DELIVERED)) {
                                    valueStatus = 100;
                                    delivered = true;
                                    System.out.println("l.getSellDate() ==> " + l.getSellDate());
                                    popoverSellDate = "data-toggle='popover' data-trigger='hover' title='Sell Date' data-content='" + l.getSellDate() + "'";
                                } else if (l.getStatus().equals(StaticFunc.CANCELED)) {
                                    valueStatus = -1;
                                    canceled = true;
                                }
                        %>
                        <tr class="<%=(canceled) ? "table-danger" : (delivered) ? "table-success" : ""%>" <%=(delivered) ? popoverSellDate : ""%>>
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
                            <td><%if (canceled) {%>
                                <i class="fas fa-times-circle" title="Order deleted"></i>
                                <%} else if (delivered) {%>
                                <i class="fas fa-clipboard-check" title="Order delivered"></i>
                                <%} else {%>
                                <button type="button" class="tablebutton" style="color: #1ae2dd;"
                                        data-target="#alertSetStatusOrder"
                                        data-toggle="modal"
                                        onclick="setTmpId(<%=l.getId()%>,'tmpIdStatus');setRadiosStatusOrder(<%=valueStatus%>)">
                                    <i class="fas fa-pencil-alt"></i>
                                </button>
                                <%}%>
                            </td>
                        </tr>
                        <%}%>
                        </tbody>
                    </table>
            </div>
        </div>
        <div class="container text-center">
            <!-- PAGINATION -->
            <%if (currentPage > 2) {%>
            <a class="btn btn-dark" href="#" role="button" onclick="showParticularPage('page_action',1);">First Page</a>
            <%}%>
            <%if (currentPage > 1) {%>
            <a class="btn btn-dark" href="#" role="button"
               onclick="showParticularPage('page_action',<%=previousPage%>);"><i class="fas fa-arrow-left"></i></a>
            <%}%>
            <%if (currentPage < totalNumberOfPages) {%>
            <a class="btn btn-dark" href="#" role="button" onclick="showParticularPage('page_action',<%=nextPage%>);"><i
                    class="fas fa-arrow-right"></i></a>
            <a class="btn btn-dark" href="#" role="button"
               onclick="showParticularPage('page_action',<%=totalNumberOfPages%>)">Last&rsaquo;&rsaquo;</a>
            <%}%>
        </div>
        <%
        } else {
        %>
        <h1>There aren't orders :(</h1>
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
                <input type="radio" id="radio_nothing_new" name="status" value="<%=StaticFunc.NOTHING_NEW%>">
                <label for="radio_nothing_new"><%=StaticFunc.NOTHING_NEW%>
                </label><br>
                <input type="radio" id="radio_processing" name="status" value="<%=StaticFunc.PROCESSING%>">
                <label for="radio_processing"><%=StaticFunc.PROCESSING%>
                </label><br>
                <input type="radio" id="radio_sent" name="status" value="<%=StaticFunc.SENT%>">
                <label for="radio_sent"><%=StaticFunc.SENT%>
                </label><br>
                <input type="radio" id="radio_delivering" name="status" value="<%=StaticFunc.DELIVERING%>">
                <label for="radio_delivering"><%=StaticFunc.DELIVERING%>
                </label><br>
                <input type="radio" id="radio_delivered" name="status" value="<%=StaticFunc.DELIVERED%>">
                <label for="radio_delivered"><%=StaticFunc.DELIVERED%>
                </label><br>
                <input type="radio" id="radio_canceled" name="status" value="<%=StaticFunc.CANCELED%>">
                <label for="radio_canceled"><%=StaticFunc.CANCELED%>
                </label><br>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="modifyStatusOrder(document.getElementById('tmpIdStatus').value,'status',<%=currentPage%>)">
                    Modify status
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
    <input type="hidden" name="sellDate" id="sellDate" value="">
    <input type="hidden" name="pageToShow" value="">
</form>

<form method="post" id="page_action">
    <input type="hidden" name="controllerAction" value="admin.Logistics.showLogistics">
    <input type="hidden" name="pageToShow" value="">
</form>

<script>
    window.addEventListener("load", () => {

        /* necessario per il popover  */
        $(document).ready(function () {
            $('[data-toggle="popover"]').popover({})
        });

        /* refresh della pagina chiamando il form della sidebar */
        window.setTimeout("setControllerAction('admin.Logistics.showLogistics')", 240000); /* timeout in millisecondi */
    })
</script>
</body>
</html>

