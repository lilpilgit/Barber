<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Structure" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento della prenotazione è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    boolean bookedStatus = false;
    if (request.getAttribute("bookedStatus") != null) {
        bookedStatus = (Boolean) request.getAttribute("bookedStatus");
    }

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

    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    Structure structure = null;
    if (request.getAttribute("structure") != null) {
        /* SE MI È STATO PASSATO L'ATTRIBUTO structure */
        structure = (Structure) request.getAttribute("structure");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Book";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showBook";
%>
<!doctype html>
<html lang="en">

<%@include file="/templates/head.jsp"%>

<body>

<%@include file="/templates/header.jsp"%>
<!------------------------------------------------ Book section ----------------------------------------------------->

<div class="container text-center my-4" style="background-color: #f1e7cb; border-radius: 25px;">
    <img src="img/homepage/book.jpg" alt="BookImage">
    <h4>We are open every day from 9:00 to 18:00!</h4>
    <h3>You can book your appointment after selecting the date and time below.</h3>

    <div class="d-flex justify-content-center" >
        <div class="col-4 pt-4 my-5 book-box" >
            <label for="appointment-date">Select a date:</label>
            <input type="date" id="appointment-date" name="appointment_date" required
                   onchange="findSlot('<%=structure.getId()%>' , this.value)">
            <hr>
            <label for="time">Choose an hour:</label>
            <select id="time" name="time" required>
            </select>
            <hr>
            <button class="btn btnheader active2"
                    <%if (!bookedStatus) {%>
                    onclick="bookNow('<%=loggedUser.getId()%>', 'time', 'appointment-date')"
                    <%} else {%>
                    onclick="showResult('fail','You have already an appointment!')"
                    <%}%>
                    type="button" id='book-now'>
                Book Now!
            </button>
        </div>
    </div>
</div>
<form method="post" id="action_book">
    <input type="hidden" name="controllerAction" value="">
    <input type="hidden" name="idCustomer" value="">
    <input type="hidden" name="selected_date" value="">
    <input type="hidden" name="selected_time" value="">
</form>
<!---------------------------------------------- End of Book section ------------------------------------------------>

<%@ include file="/templates/footer.html"%>
<script type="text/javascript">

    window.addEventListener("load",() =>{
        setDateBook("appointment-date");
        findSlot('<%=structure.getId()%>' , document.getElementById("appointment-date").value);
    });
</script>
</body>
</html>