<%@ page import="model.mo.Booking" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
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
<%@include file="../../templates/admin-sidebar.jsp" %>
<div class="page-wrapper chiller-theme toggled">

    <!--Main content of the page-->
    <main class="page-content">
        <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3">
            <div class="form-inline">
                <label for="Booking-Date" class="pr-2" style="color: #818894;">Select a date: </label>
                <input type="date" class="form-control" value="<%=dataToShow%>" name="currentDate" id="Booking-Date"
                       form="action"
                       onchange="showBookingsAdmin();">
                <ul class="legend">
                    <li><span class="legendDeletedByCustomer"></span>DELETED BY CUSTOMER</li>
                    <li><span class="legendDeletedByAdmin"></span>DELETED BY ADMIN</li>
                </ul>
            </div>
        </nav>
        <div class="row justify-content-center">
            <div class="col-auto">
                <%if (areBookings) {%>
                <div class="table-responsive">
                    <table class="table table-hover table-bordered">
                        <thead class="thead-dark">
                        <tr>
                            <th scope="col">N°</th>
                            <th scope="col">Hour Start</th>
                            <th scope="col">Customer</th>
                            <th scope="col">Email</th>
                            <th scope="col">Phone</th>
                            <th scope="col">Action</th>
                        </tr>
                        </thead>
                        <tbody class="fixedheight">
                        <%
                            int i = 1; /* contatore per il numero di impiegati */

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
                                    if (!deletedStatus) /* è false, cancellato dall'admin */ {
                                        isDeletedBy = 'A';
                                        class_color_row = "table-danger";
                                        title = "Posted by you";
                                        /* ho dovuto adottare il replace altrimenti l'html si interrompe al primo " o ' */
                                    } else /* è true, cancellato dal CLIENTE */ {
                                        isDeletedBy = 'C';
                                        class_color_row = "table-warning";
                                        title = "Posted by the customer";
                                        /* ho dovuto adottare il replace altrimenti l'html si interrompe al primo " o ' */
                                    }
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
                        <td>
                            <%if (isDeletedBy == 'N') {%>
                            <button type="button" class="trashbutton" title="Delete"
                                    data-target="#alert<%=user%>Delete"
                                    data-toggle="modal"
                                    onclick=setTmpId(<%=b.getId()%>,'tmpIdDel')>
                                <i class="far fa-trash-alt"></i>
                            </button>
                            <%} else /* è stato cancellato da qualcuno dunque mostro la deleted reason */ {%>
                            <i class="far fa-calendar-times"></i>
                            <%}%>
                            <%--                            <i class="fas fa-info-circle"></i>--%>
                        </td>
                        </tr>
                        <%}%>
                        </tbody>
                    </table>
                </div>
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

<input type="hidden" id="tmpIdDel" name="bookingID" value="" form="deleteBooking">
<!--MODAL DI CONFERMA CANCELLAZIONE PRENOTAZIONE-->
<div class="modal fade" id="alert<%=user%>Delete" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
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
                You are attempting to delete a booking.<br><br>Please provide a deleted reason to be shown to the
                customer
                <form method="post" id="deleteBooking">
                    <textarea style="width: 100%;resize: none" rows="5" required placeholder="Because..."
                              name="deletedReason"></textarea>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="submit" id="ultimateBtnDel" class="btn btn-primary"
                                style="background-color: rgba(255,5,3,0.66)">Delete booking
                        </button>
                        <input type="hidden" name="controllerAction" value="admin.Bookings.deleteBooking">
                        <input type="hidden" class="form-control" value="<%=dataToShow%>" name="currentDate">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<script>

    window.addEventListener("load", () => {

        /* necessario per il popover  */
        $(document).ready(function () {
            $('[data-toggle="popover"]').popover({})
        });

        /* refresh della pagina chiamando il form della sidebar */
        window.setTimeout("setControllerAction('admin.Bookings.showBookings')", 240000); /* timeout in millisecondi */

    })

</script>
</body>
</html>
