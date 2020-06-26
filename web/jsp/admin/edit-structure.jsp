<%@ page import="model.mo.Structure" %>
<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
    /* Prendo il parametro "result" che si occupa di indicarmi se la modifica dei dati della struttura è andata a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di modifica ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Prendo il parametro "structureToModify" che è l'oggetto structure che mi permette di ottenere i campi da settare
     * e dunque modificare a volontà all'interno della pagina */
    Structure structureToEdit = null;
    if (request.getAttribute("structureToModify") != null) {
        structureToEdit = (Structure) request.getAttribute("structureToModify");
    }

    String[] splittedAddress = null;
    int splittedAddressLength = 0;
    /* Splitto sulla | il campo address della struttura per poterlo visualizzare in ogni campo della form */
    if (structureToEdit != null) {
        String address = structureToEdit.getAddress();
        splittedAddress = address.split("\\|");
        splittedAddressLength = splittedAddress.length;
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

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Structure";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showStructure";

%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="../../templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content text-center">
        <br>
        <form id="form_edit_structure" method="post" class="needs-validation">
            <div class="form-row justify-content-center">
                <div class="col-md-2 mb-3">
                    <label for="Name">Name</label>
                    <input type="text" name="name" id="Name" maxlength="30" required
                           value="<%=structureToEdit.getName()%>"
                           class="form-control">
                </div>
                <div class="col-md-2 mb-3">
                    <label for="Phone">Phone number</label>
                    <input type="tel" name="phone" id="Phone" placeholder="3334445556" pattern="[0-9]{5,20}" required
                           value="<%=structureToEdit.getPhone()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-2 mb-3">
                    <label for="Opening-Time">Opening Time</label>
                    <input type="time" class="form-control" name="opening_time" id="Opening-Time" required readonly
                           value="<%=structureToEdit.getOpeningTime()%>">
                </div>
                <div class="col-md-2 mb-3">
                    <label for="Closing-Time">Closing Time</label>
                    <input type="time" class="form-control" name="closing_time" id="Closing-Time" required readonly
                           value="<%=structureToEdit.getClosingTime()%>"
                           class="form-control">
                </div>
                <div class="col-md-2 mb-3">
                    <label for="Slot">Slot</label>
                    <input type="time" class="form-control" name="slot" id="Slot" required readonly
                           value="<%=structureToEdit.getSlot()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-2 mb-3">
                    <label for="State">State</label>
                    <select class="custom-select" name="state" id="State" required>
                        <option selected disabled value="">Choose...</option>
                        <option>ITALY</option>
                    </select>
                </div>
                <div class="col-md-2 mb-3">
                    <label for="Region">Region</label>
                    <select class="custom-select" name="region" id="Region" required>
                        <option selected disabled value="">Choose...</option>
                        <option>ABRUZZO</option>
                        <option>BASILICATA</option>
                        <option>CALABRIA</option>
                        <option>EMILIA-ROMAGNA</option>
                        <option>FRIULI-VENEZIA-GIULIA</option>
                        <option>LAZIO</option>
                        <option>LIGURIA</option>
                        <option>LOMBARDIA</option>
                        <option>MARCHE</option>
                        <option>MOLISE</option>
                        <option>PIEMONTE</option>
                        <option>PUGLIA</option>
                        <option>SARDEGNA</option>
                        <option>SICILIA</option>
                        <option>TOSCANA</option>
                        <option>TRENTINO-ALTO-ADIGE</option>
                        <option>UMBRIA</option>
                        <option>VALLE D'AOSTA</option>
                        <option>VENETO</option>
                    </select>
                </div>
                <div class="col-md-2 mb-3">
                    <label for="City">City</label>
                    <input type="text" class="form-control" name="city" id="City" maxlength="30" required
                           value="<%=splittedAddress[2]%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-md-2 mb-3">
                    <label for="Cap">CAP</label>
                    <input type="number" class="form-control" name="cap" id="Cap" required min="0" max="999999"
                           value="<%=splittedAddress[3]%>">
                </div>
                <div class="col-md-2 mb-3">
                    <label for="Street">Street</label>
                    <input type="text" class="form-control"  name="street" id="Street" maxlength="30" required
                           value="<%=splittedAddress[4]%>"
                           oninput="toUpperCase(this)">
                </div>
                <div class="col-md-2 mb-3">
                    <label for="House-number">House number</label>
                    <input type="number" class="form-control" name="house_number" id="House-number" min="0" max="999999"
                           value="<%=(splittedAddressLength == 6) ? splittedAddress[5] : ""%>">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <button type="submit" id="submit-edit-structure" class="btn btn-primary" name="submit"
                    value="edit_structure">Send
            </button>
            <input type="hidden" name="controllerAction"
                   value="admin.Structure.editStructure">
        </form>
    </main>
</div>


<script>
    window.addEventListener("load",() =>{
        /*addOnClickListenerBtnSidebar();*/
        /* Setto come selected tra le option di state e region quella uguale al valore della struttura da modificare */
        setSelectedAttribute("State", "<%=splittedAddress[0]%>");
        setSelectedAttribute("Region", "<%=splittedAddress[1]%>")
    })
</script>
</body>
</html>

