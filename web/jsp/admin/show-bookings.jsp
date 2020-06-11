<%@ page import="model.mo.Booking" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = "Booking";
    String controller = "Bookings";

    /* Prendo l'ArrayList<Booking> di tutti gli appuntamenti */
    boolean areBookings = false;
    ArrayList<Booking> bookings = (ArrayList<Booking>) request.getAttribute("bookings");
    if (bookings != null && bookings.size() != 0) {
        areBookings = true;
    }

    /* Prendo la data da mostrare nel calendario che deve essere modificata di volta in volta tranne la prima volta, vedere
     *  il javascript a fondo pagina.
     * */
    LocalDate dataToShow = null;
    if (request.getAttribute("currentDate") != null) {
        dataToShow = (LocalDate) request.getAttribute("currentDate");
    }

    /* Prendo il parametro "loggedOn" che mi consente di sapere se l'utente attuale è loggato o meno */
    Boolean loggedOn = false;
    if (request.getAttribute("loggedOn") != null) {
        loggedOn = (Boolean) request.getAttribute("loggedOn");
    }

    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del nuovo cliente è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "loggedUser" che mi consente di sapere qual'è l'utente attualmente loggato */
    User loggedUser = null;
    if (request.getAttribute("loggedUser") != null && loggedOn != null) {
        loggedUser = (User) request.getAttribute("loggedUser");
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di cancellazione/modifica ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Bookings";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showBookings";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="../../templates/admin-sidebar.jsp" %>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3">
            <div class="form-inline">
                <label for="Booking-Date" class="pr-2" style="color: #818894;">Select a date: </label>
                <input type="date" class="form-control" value="<%=dataToShow%>" name="currentDate" id="Booking-Date" form="action"
                       onchange="showBookingsAdmin();">
            </div>
        </nav>
        <div class="row justify-content-center">
            <div class="col-auto">
                <%if (areBookings) {%>
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">Hour Start</th>
                        <th scope="col">Customer</th>
                        <th scope="col">Email</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int i = 1; /* contatore per il numero di impiegati */

                        for (Booking b : bookings) {
                            Boolean deletedStatus = b.isDeleted();
                    %>
                    <tr class="<%=deletedStatus ? "table-danger" : ""%>">
                        <th scope="row"><%=i++%>
                        </th>
                        <td><%=b.getHourStart()%>
                        </td>
                        <td><%=b.getCustomer().getName()%> , <%=b.getCustomer().getSurname()%>
                        </td>
                        <td><%=b.getCustomer().getEmail()%>
                        </td>
                        <td><%=b.getCustomer().getPhone()%>
                        </td>
                        <td>
                            <button type="button" class="trashbutton" title="Delete"
                                    data-target="#alert<%=user%>"
                                    data-toggle="modal"
                                    onclick=setTmpId(<%=b.getId()%>,'tmpId')>
                                <i class="far fa-trash-alt"></i>
                            </button>
                        </td>
                    </tr>
                    <%}%>
                    </tbody>
                </table>
                <%} else {%>
                <h1>There are no bookings :(</h1>
                <%}%>
                <form method="post" id="action">
                    <input type="hidden" name="controllerAction" value="">
                    <input type="hidden" name="<%=user%>ID" value="">
                </form>
            </div>
        </div>
    </main>
</div>

<input type="hidden" id="tmpIdDel" value="">
<!--MODAL DI CONFERMA CANCELLAZIONE PRENOTAZIONE-->
<div class="modal fade" id="alert<%=user%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle" style="color: rgba(211,4,0,0.75)">You are deleting
                    a booking...</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                You are attempting to delete a booking.<br><br>Are you sure you want to continue?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>


                <!-- TODO AGGIORNARE CON FUNZIONE GENERICA deleteById contenuta in admin.js come con show-customers -->


                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="deleteEmployee(document.getElementById('tmpIdDel').value)">Delete
                    employee
                </button>
            </div>
        </div>
    </div>
</div>
<!--FINE MODAL DI CONFERMA ELIMINAZIONE DIPENDENTE-->


<script>

    window.addEventListener("load", () => {
<%--        <%if(dataToShow == null){%>--%>
<%--            /* la chiamo solamente se è la prima volta che devo*/--%>
<%--            setCurrentDate("Booking-Date");--%>
<%--        <%}%>--%>
    })

</script>
</body>
</html>

