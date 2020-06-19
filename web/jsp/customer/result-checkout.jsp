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

    /* non mostrare l'alert predefinito */
    boolean showAlert = false;

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Order";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = null;

%>
<html lang="en">
<%@include file="/templates/head.jsp" %>
<body>
<%@include file="/templates/header.jsp" %>

<div class="row padding">
    <%
        if (result != null && result.equals("success")) {
            /* order added successfully */
    %>
    <div class="result-checkout-container">
        <h3 class="result-checkout">It's all OK!</h3>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-6">
                    <!-- TODO: non carica l'animazione ma solo l'immagine finale in quanto c'è l'alert che blocca il caricamento della pagina -->
                    <!--preso da https://lottiefiles.com/web-player?lottie_url=https%3A%2F%2Fassets2.lottiefiles.com%2Fpackages%2Flf20_evNjeW.json-->
                    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
                    <lottie-player src="https://assets2.lottiefiles.com/packages/lf20_evNjeW.json" background="transparent"
                                   speed="0.5" style="height: 300px;" autoplay></lottie-player>
                </div>
            </div>
            <p><%=applicationMessage%></p>
        </div>
    </div>
    <%}%>
</div>


<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {
        /* TODO non FUNZIONAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA!!! */
        /* bypasso l'alert di default */
        showMessage = function() {};
    });
</script>
</body>
</html>
