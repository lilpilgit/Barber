<%@ page import="model.mo.Structure" %>
<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del nuovo dipendente è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "structure" che mi permette di settare il value dell'input field readonly con name="structure" */
    Structure structure = null;
    boolean structurePresent = false;
    if (request.getAttribute("structure") != null) {
        /* SE MI È STATO PASSATO L'ATTRIBUTO structure */
        structure = (Structure) request.getAttribute("structure");
        structurePresent = true;
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di inserimento ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Prendo il parametro "employeeToModify" che è l'oggetto employee che mi permette di ottenere i campi da settare
     * e dunque modificare a volontà all'interno della pagina */
    User employeeToEdit = null;
    if (request.getAttribute("employeeToModify") != null) {
        employeeToEdit = (User) request.getAttribute("employeeToModify");
    }

    /* Setto una stringa da usare all'interno dell html per decidere se devo mostrare campi vuoti ( inserimento nuovo
     * impiegato) o campi già riempiti ( modifica impiegato esistente ) */
    String action = (employeeToEdit != null) ? "modify" : "insert";

    /* Faccio lo split dell'address per poterlo mostrare all'interno dei vari campi */
    String[] splittedAddress = null;
    int splittedAddressLength = 0;
    /* Splitto sulla | il campo address dell'utente per poterlo visualizzare in ogni campo della form */
    if (employeeToEdit != null) {
        String address = employeeToEdit.getAddress();
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
    String menuActiveLink = "Staff";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showEmployees";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="../../templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">

        <form id="form_add_employee" method="post" class="needs-validation">
            <div class="form-row justify-content-center">
                <div class="text-center">
                    <h4>You are <%=(action.equals("modify")) ? "modifying an " : "entering an"%> Employee</h4>
                </div>
            </div>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-3">
                    <label for="Name">Name</label>
                    <input type="text" class="form-control" name="name" id="Name" placeholder="Mario" required maxlength="30"
                           value="<%=(action.equals("modify")) ? employeeToEdit.getName() : ""%>">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Surname">Surname</label>
                    <input type="text" class="form-control" name="surname" id="Surname" placeholder="Rossi" required maxlength="30"
                           value="<%=(action.equals("modify")) ? employeeToEdit.getSurname() : ""%>">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-md-3">
                    <label for="Email">Email address</label>
                    <input type="email" class="form-control" name="email" id="Email" aria-describedby="emailHelp" maxlength="50"
                           value="<%=(action.equals("modify")) ? employeeToEdit.getEmail() : ""%>"
                           required>
                    <%if (!action.equals("modify")) {%><small id="emailHelp" class="form-text text-muted">A random
                    password will be sent to that
                    email</small><%}%>
                </div>
                <div class="col-md-3">
                    <label for="Phone">Phone number</label>
                    <input type="tel" name="phone" id="Phone" placeholder="3334445556" pattern="[0-9]{5,20}" required
                           value="<%=(action.equals("modify")) ? employeeToEdit.getPhone() : ""%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-3">
                    <label for="Birth-Date">Birth Date</label>
                    <input type="date" class="form-control" name="birth_date" id="Birth-Date" required
                           value="<%=(action.equals("modify")) ? employeeToEdit.getBirthDate() : ""%>">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Fiscal-Code">Fiscal Code</label>
                    <input type="text" pattern="[A-Z0-9]{16}" class="form-control" name="fiscal_code" id="Fiscal-Code" maxlength="16"
                           required value="<%=(action.equals("modify")) ? employeeToEdit.getFiscalCode() : ""%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-3">
                    <label for="State">State</label>
                    <select class="custom-select" name="state" id="State" required>
                        <option selected disabled value="">Choose...</option>
                        <option>ITALY</option>
                    </select>
                </div>
                <div class="col-md-3 mb-3">
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
                <div class="col-md-3 mb-3">
                    <label for="City">City</label>
                    <input type="text" class="form-control" name="city" id="City" maxlength="30" required
                           value="<%=(action.equals("modify")) ? splittedAddress[2] : ""%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-3">
                    <label for="Cap">CAP</label>
                    <input type="number" class="form-control" name="cap" id="Cap" required min="0" max="999999"
                           value="<%=(action.equals("modify")) ? splittedAddress[3] : ""%>">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Street">Street</label>
                    <input type="text" class="form-control" name="street" id="Street" maxlength="30" required
                           value="<%=(action.equals("modify")) ? splittedAddress[4] : ""%>"
                           oninput="toUpperCase(this)">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="House-number">House number</label>
                    <input type="number" class="form-control" name="house_number" id="House-number" min="0" max="999999"
                           value="<%=(action.equals("modify") && splittedAddressLength == 6) ? splittedAddress[5] : ""%>">
                </div>
            </div>
            <br>
            <div class="text-center">
                <button type="submit" id="submit-new-edit-employee" class="btn btn-primary" name="submit"
                        value="<%=(action.equals("modify")) ? "edit_employee" : "add_new_employee"%>">Send
                </button>
            </div>
            <input type="hidden" name="controllerAction"
                   value="<%=(action.equals("modify")) ? "admin.Staff.editEmployee" : "admin.Staff.addEmployee"%>">
            <%if (action.equals("modify")) {%>
            <input type="hidden" name="employeeId" value="<%=employeeToEdit.getId()%>"/>
            <%}%>
        </form>
    </main>
</div>


<script>
    window.addEventListener("load", () => {
        /*addOnClickListenerBtnSidebar();*
        /* Setto come selected tra le option di state e region quella uguale al valore dell'impiegato da modificare */
        <%if(action.equals("modify")){%>
        setSelectedAttribute("State", "<%=splittedAddress[0]%>");
        setSelectedAttribute("Region", "<%=splittedAddress[1]%>")
        <%}%>

    })
</script>
</body>
</html>

