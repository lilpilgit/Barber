<%@ page import="model.mo.User" %>
<%@ page import="model.mo.Structure" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
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

    /* Prendo il parametro "result" che si occupa di indicarmi se la modifica dei dati è andata a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        if (resultPresent && result.equals("fail")) {
            /* Mostro il messaggio solo se è andato in errore l'ordine */
            applicationMessage = (String) request.getAttribute("applicationMessage");
        }
    }

    /* Prendo l'oggetto struttura per conoscere le informazioni da mostrare nel footer */
    Structure structure = null;
    if(request.getAttribute("structure") != null){
        structure = (Structure) request.getAttribute("structure");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Order";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = null;

%>
<html lang="en">
<%@include file="/templates/head.inc" %>
<body>
<%@include file="/templates/header.jsp" %>

<div class="row padding">
    <%
        if (resultPresent && result.equals("success")) {
            /* order added successfully */
    %>
    <div class="result-checkout-container">
        <h3 class="result-checkout">It's all OK!</h3>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-6">
                    <img src="img/checkout/result_checkout_success_once.gif" alt="checkout success" class="rounded mx-auto d-block"
                         width="300px">
                </div>
            </div>
            <p>Order received correctly. Check the status in your order section.</p>
        </div>
    </div>
    <%} else {%>
    <div class="result-checkout-container">
        <h3 class="result-checkout">Error in your order...</h3>
        <div class="container">
            <h3><%=applicationMessage%>
            </h3>
        </div>
    </div>
    <%}%>
</div>

<%@ include file="/templates/footer.jsp" %>
<script type="text/javascript">
    window.addEventListener("load", () => {

    });
</script>
</body>
</html>
