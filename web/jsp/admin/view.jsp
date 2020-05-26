<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del nuovo cliente è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di cancellazione/modifica ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }
    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Welcome";

    /* Parametro per aggiungere la classe "button-side-active" al bottone della pagina in cui si trova */
    String idBtnAttivo = "";

%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="/templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <div class="container-fluid">
            <h1>Welcome!</h1>
            <hr>
            <h2>Hello <strong><%=loggedUser.getName()%></strong></h2>
            <div class="row">
                <div class="form-group col-md-12" id="to-fill"></div>
            </div>
            <h5>Altre info o statistiche ...</h5>
            <hr>
        </div>
    </main>
</div>
<script>
    window.addEventListener("load",() =>{

    });
</script>
</body>
</html>

