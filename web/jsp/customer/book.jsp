<%@page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.User" %>

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

<h1> Here u can book your appointment</h1>

<!---------------------------------------------- End of Book section ------------------------------------------------>

<%@ include file="/templates/footer.html"%>
<script type="text/javascript">

    window.addEventListener("load",() =>{
    });
</script>
</body>
</html>