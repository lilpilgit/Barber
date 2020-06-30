<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ page session="false" %>
<%
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
    String menuActiveLink = "Error";
%>
<html lang="en">
<%@include file="/templates/head.inc" %>
<body>
<div class="row padding">
    <div class="result-checkout-container">
        <h1 class="result-checkout">An error occurred...</h1>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-6">
                    <img src="img/error/close_door_once.gif" alt="an error occurred" class="rounded mx-auto d-block"
                         width="300px">
                </div>
            </div>
            <p>There was probably a problem if you are here ... Try again later, we are already working on it
                ...<br><br></p>
            <div class="justify-content-center">
                <form method="post">
                    <button class="btn btnheader active2" type="submit" id='showHome'>
                        Home
                    </button>
                    <input type="hidden" name="controllerAction" value="home.Home.view">
                </form>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript">
    window.addEventListener("load", () => {

    });
</script>

</body>
</html>
