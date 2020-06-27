<%@ page import="model.mo.Booking" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
    String user = "Booking";
    String controller = "Work";

    /* Prendo l'ArrayList<Booking> di tutti gli appuntamenti */
    boolean areBookings = false;
    ArrayList<Booking> bookings = (ArrayList<Booking>) request.getAttribute("bookings");
    if (bookings != null && bookings.size() != 0) {
        areBookings = true;
    }

    /* Prendo la data da mostrare nel calendario al dipendente */
    LocalDate dataToShow = null;
    if (request.getAttribute("currentDate") != null) {
        dataToShow = (LocalDate) request.getAttribute("currentDate");
    }

    /* Prendo il parametro "loggedOn" che mi consente di sapere se l'utente attuale è loggato o meno */
    boolean loggedOn = false;
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
    if (loggedOn && request.getAttribute("loggedUser") != null) {
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
<!--Main content of the page-->
<main class="page-content">
    <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3">
        <div class="form-inline">
            <label for="Booking-Date" class="pr-2" style="color: #818894;">Your work day</label>
            <input type="date" class="form-control" value="<%=dataToShow%>" name="currentDate" id="Booking-Date"
                   readonly>
            <ul class="legend">
                <li><span class="legendDeletedByCustomer"></span>DELETED BY CUSTOMER</li>
                <li><span class="legendDeletedByAdmin"></span>DELETED BY ADMIN</li>
            </ul>
        </div>
    </nav>
    <div class="row justify-content-center">
        <div class="col-auto">
            <%if (areBookings) {%>
            <table class="table table-hover table-bordered">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">N°</th>
                    <th scope="col">Hour Start</th>
                    <th scope="col">Customer</th>
                    <th scope="col">Email</th>
                    <th scope="col">Phone</th>
                </tr>
                </thead>
                <tbody>
                <%
                    int i = 1; /* contatore per il numero di appuntamenti */

                    for (Booking b : bookings) {
                        /*
                         * flag char per sapere se è stato cancellato dall'admin ==> DELETED 0/FALSE
                         * o se è stato cancellato dal cliente ==> DELETED 1/TRUE
                         * Di default lo metto su N che sta per 'da NESSUNO'
                         */
                        char isDeletedBy = 'N';
                        Boolean deletedStatus = b.isDeleted();
                        String class_color_row = ""; /* di default nessun colore */
                        String tr = "<tr>"; /* di default è una semplice riga */
                        String title = "";
                        if (deletedStatus != null) {
                            /* è stato cancellato da qualcuno...da chi? */
                            if (!deletedStatus) /* è false, cancellato dall'admin */ {
                                isDeletedBy = 'A';
                                class_color_row = "table-danger";
                                title = "Posted by you";
                            } else /* è true, cancellato dal CLIENTE */ {
                                isDeletedBy = 'C';
                                class_color_row = "table-warning";
                                title = "Posted by the customer";
                            }
                            /* ho dovuto adottare il replace altrimenti l'html si interrompe al primo " o ' */
                            tr = "<tr class='" + class_color_row + "' data-toggle='popover' data-trigger='hover' title='" + title + "' data-content='" + b.getDeletedReason().replace("'", "&apos;").replace("\"", "&quot;") + "'>";
                        }

                %>

                <%=tr%>
                <th scope="row"><%=i++%>
                </th>
                <%String[] splittedTime = b.getHourStart().toString().split(":");%>
                <td><%=splittedTime[0] + ":" + splittedTime[1]%>
                </td>
                <td><%=b.getCustomer().getName()%> , <%=b.getCustomer().getSurname()%>
                </td>
                <td><%=b.getCustomer().getEmail()%>
                </td>
                <td><%=b.getCustomer().getPhone()%>
                </td>
                </tr>
                <%}%>
                </tbody>
            </table>
            <%} else {%>
            <h1>There are no bookings :(</h1>
            <%}%>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col text-center">
                <form id="logoutForm" method="post">
                    <input type="submit" class="btn btn-primary"  value="Logout">
                    <input type="hidden" name="controllerAction" value="home.Home.logout">
                </form>
            </div>
        </div>
    </div>

</main>
<form id="refreshWorkForm" method="post">
    <input type="hidden" name="controllerAction" value="employee.Work.showBookings">
    <input type="hidden" value="<%=dataToShow%>" name="currentDate" id="currentDateRefresh">
</form>

<script>

    function refreshWork(){
        document.getElementById('refreshWorkForm').submit()
    }

    window.addEventListener("load", () => {

        /* necessario per il popover  */
        $(document).ready(function () {
            $('[data-toggle="popover"]').popover({})
        });

        /* refresh della pagina chiamando il form della sidebar */
        window.setTimeout(refreshWork, 60000); /* timeout in millisecondi */

    })

</script>
</body>
</html>
