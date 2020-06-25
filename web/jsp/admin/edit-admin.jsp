<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
    /* Prendo il parametro "result" che si occupa di indicarmi se la modifica dell'admin è andata a buon fine o meno*/
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

    /* Prendo il parametro "adminToModify" che è l'oggetto admin che mi permette di ottenere i campi da settare
     * e dunque modificare a volontà all'interno della pagina */
    User adminToEdit = null;
    if (request.getAttribute("adminToModify") != null) {
        adminToEdit = (User) request.getAttribute("adminToModify");
    }

    String[] splittedAddress = null;
    int splittedAddressLength = 0;
    /* Splitto sulla | il campo address dell'utente per poterlo visualizzare in ogni campo della form */
    if (adminToEdit != null) {
        String address = adminToEdit.getAddress();
        splittedAddress = address.split("\\|");
        splittedAddressLength = splittedAddress.length;
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Profile";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = null;
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="/templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content text-center">
        <br>
        <form id='form_edit_admin' method="post" class="needs-validation">
            <div class="form-row justify-content-center">
                <div class="col-sm-2 mb-3">
                    <label for="Name">First name</label>
                    <input type="text" class="form-control" name="name" id="Name" placeholder="Mario" required
                           value="<%=adminToEdit.getName()%>">
                </div>
                <div class="col-sm-2 mb-3">
                    <label for="Surname">Last name</label>
                    <input type="text" class="form-control" name="surname" id="Surname" placeholder="Rossi" required
                           value="<%=adminToEdit.getSurname()%>">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-sm-2">
                    <label for="Email">Email address</label>
                    <input type="email" class="form-control" name="email" id="Email" aria-describedby="emailHelp"
                           value="<%=adminToEdit.getEmail()%>"
                           required>
                </div>
                <div class="col-sm-2">
                    <label for="Phone">Phone number</label>
                    <input type="tel" name="phone" id="Phone" placeholder="3334445556" pattern="[0-9]{5,20}" required
                           value="<%=adminToEdit.getPhone()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-sm-2 mb-3">
                    <label for="Birth-Date">Birth Date</label>
                    <input type="date" class="form-control" name="birth_date" id="Birth-Date" required
                           value="<%=adminToEdit.getBirthDate()%>">
                </div>
                <div class="col-sm-2 mb-3">
                    <label for="Fiscal-Code">Fiscal Code</label>
                    <input type="text" pattern="[A-Z0-9]{16}" class="form-control" name="fiscal_code" id="Fiscal-Code"
                           required value="<%=adminToEdit.getFiscalCode()%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-sm-2 mb-3">
                    <label for="State">State</label>
                    <select class="custom-select" name="state" id="State" required>
                        <option selected disabled value="">Choose...</option>
                        <option>ITALY</option>
                    </select>
                </div>
                <div class="col-sm-2 mb-3">
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
                <div class="col-sm-2 mb-3">
                    <label for="City">City</label>
                    <input type="text" class="form-control" name="city" id="City" required
                           value="<%=splittedAddress[2]%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-sm-2 mb-3">
                    <label for="Cap">CAP</label>
                    <input type="number" class="form-control" name="cap" id="Cap" required min="0"
                           value="<%=splittedAddress[3]%>">
                </div>
                <div class="col-sm-2 mb-3">
                    <label for="Street">Street</label>
                    <input type="text" class="form-control" name="street" id="Street" required
                           value="<%=splittedAddress[4]%>"
                           oninput="toUpperCase(this)">
                </div>
                <div class="col-sm-2 mb-3">
                    <label for="House-number">House number</label>
                    <input type="number" class="form-control" name="house_number" id="House-number" min="0"
                           value="<%=(splittedAddressLength == 6) ? splittedAddress[5] : ""%>">
                </div>
            </div>
            <br>
            <button type="submit" id="submit-edit-admin" class="btn btn-primary" name="submit"
                    value=edit_admin">Send
            </button>
            <input type="hidden" name="controllerAction"
                   value="admin.Admin.editAdmin">
            <input type="hidden" name="adminId" value="<%=adminToEdit.getId()%>"/>

        </form>
    </main>
</div>


<script>
    window.addEventListener("load", () => {
        /*addOnClickListenerBtnSidebar();*/
        /* Setto come selected tra le option di state e region quella uguale al valore dell'admin da modificare */
        setSelectedAttribute("State", "<%=splittedAddress[0]%>");
        setSelectedAttribute("Region", "<%=splittedAddress[1]%>")

    });</script>
</body>
</html>

