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

    /* Prendo il parametro "result" che si occupa di indicarmi se la modifica dei dati è andata a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }


    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Order";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = null;

%>
<html>
<head>
    <title>Title</title>
</head>
<%@include file="/templates/head.jsp"%>

<body>

<%@include file="/templates/header.jsp"%>

<h1>PAGINA DI PROVA</h1>

<p>
    Sulla base del valore di result mostrare l'application messagge e magari un immagine carina che fa capire
    che l'ordine è andato a buon fne dopodichè aggiungere magari un link in cui si punta alla sezione ordini
    del cliente con scritto Tieni traccia dei tuoi ordini o roba simile (IN INGLESE)
</p>

<%@ include file="/templates/footer.html"%>
<script type="text/javascript">
    window.addEventListener("load",() =>{
    });
</script>
</body>
</html>
