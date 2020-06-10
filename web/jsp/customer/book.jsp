<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Structure" %>

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
            <input type="date" id="appointment-date" name="appointment date"
                   onchange="findSlot('<%=structure.getOpeningTime()%>' , '<%=structure.getClosingTime()%>')">
            <hr>
            <label for="time">Choose an hour:</label>
            <select id="time" name="time">
<%--                <option value="09:00">09.00 AM</option>
                <option value="09:30">09.30 AM</option>
                <option value="10:00">10.00 AM</option>
                <option value="10:30">10.30 AM</option>
                <option value="11:00">11.00 AM</option>
                <option value="11:30">11.30 AM</option>
                <option value="12:00">12.00 PM</option>
                <option value="12:30">12.30 PM</option>
                <option value="13:00">01.00 PM</option>
                <option value="13:30">01.30 PM</option>
                <option value="14:00">02.00 PM</option>
                <option value="14:30">02.30 PM</option>
                <option value="15:00">03.00 PM</option>
                <option value="15:30">03.30 PM</option>
                <option value="16:00">04.00 PM</option>
                <option value="16:30">04.30 PM</option>
                <option value="17:00">05.00 PM</option>
                <option value="17:30">05.30 PM</option>
                <option value="18:00">06.00 PM</option>--%>
            </select>
            <hr>
            <button class="btn btnheader active2" onclick="setNavFormHome('Home.showShop')" type="button" id='showShop'>
                Book Now!
            </button>
        </div>
    </div>
</div>
<!---------------------------------------------- End of Book section ------------------------------------------------>

<%@ include file="/templates/footer.html"%>
<script type="text/javascript">
    window.addEventListener("load",() =>{
        setDateBook("appointment-date");

    });
</script>
</body>
</html>