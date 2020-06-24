<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ page session="false" %>
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

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = null;
    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "404 | Page Not Found";
%>
<html lang="en">
<%@include file="/templates/head.jsp" %>
<body>
<%@include file="/templates/header.jsp" %>
<html>
<body>
<div class="row padding">
    <div class="result-checkout-container">
        <h1 class="result-checkout">An error occurred...</h1>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-6">
                    <img src="img/error/close_door_once.gif" alt="product_not_found" class="rounded mx-auto d-block"
                         width="300px">
                </div>
            </div>
            <p>There was probably a problem if you are here ... Try again later, we are already working on it
                ...<br><br></p>
        </div>
    </div>
</div>
<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {

    });
</script>
</body>
</html>
