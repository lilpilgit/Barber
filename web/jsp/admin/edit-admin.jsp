<%@ page import="model.mo.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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


    /* Prendo il parametro "adminToModify" che è l'oggetto admin che mi permette di ottenere i campi da settare
     * e dunque modificare a volontà all'interno della pagina */
    /*TODO A SESSIONE IMPLEMENTATA, VERRÀ PRESO L'ID DALLA SESSIONE PASSATA COME ATTRIBUTO DELLA REQUEST DAL CONTROLLER*/
    Admin adminToEdit = null;
    if(request.getAttribute("adminToModify") != null){
        adminToEdit = (Admin) request.getAttribute("adminToModify");
    }

    String[] splittedAddress = null;
    int splittedAddressLength = 0;
    /* Splitto sulla | il campo address dell'utente per poterlo visualizzare in ogni campo della form */
    if (adminToEdit != null) {
        String address = adminToEdit.getUser().getAddress();
        splittedAddress = address.split("\\|");
        splittedAddressLength = splittedAddress.length;
    }

%>
<!doctype html>
<html lang="en">
<head>
    <!--------------------------------------------- Meta tags --------------------------------------------------------->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.5.1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" data-auto-replace-svg="nest"></script>
    <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
    <!--PER IL DATEPICKER-->


    <!------------------------------------------------------------------->
    <link href="${pageContext.request.contextPath}/assets/css/style-admin.css" rel="stylesheet">
    <!--JAVASCRIPT PER LA PAGINA ADMIN 'VUOTA'-->
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</head>
<body>

<jsp:include page="../../templates/admin-sidebar.html"/>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">

        <form id='form_edit_admin' method="post" class="needs-validation">
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="Name">First name</label>
                    <input type="text" class="form-control" name="name" id="Name" placeholder="Mario" required
                           value="<%=adminToEdit.getUser().getName()%>"
                           oninput="toUpperCase(this)">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="Surname">Last name</label>
                    <input type="text" class="form-control" name="surname" id="Surname" placeholder="Rossi" required
                           value="<%=adminToEdit.getUser().getSurname()%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-4">
                    <label for="Email">Email address</label>
                    <input type="email" class="form-control" name="email" id="Email" aria-describedby="emailHelp"
                           value="<%=adminToEdit.getUser().getEmail()%>"
                           required>
                </div>
                <div class="col-md-4">
                    <label for="Phone">Phone number</label>
                    <input type="tel" name="phone" id="Phone" placeholder="3334445556" pattern="[0-9]{5,20}" required
                           value="<%=adminToEdit.getUser().getPhone()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <label for="Birth-Date">Birth Date</label>
                    <input type="date" class="form-control" name="birth_date" id="Birth-Date" required
                           value="<%=adminToEdit.getBirthDate()%>">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Fiscal-Code">Fiscal Code</label>
                    <input type="text" pattern="[A-Z0-9]{16}" class="form-control" name="fiscal_code" id="Fiscal-Code"
                           required value="<%=adminToEdit.getFiscalCode()%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <label for="State">State</label>
                    <select class="custom-select" name="state" id="State" required>
                        <option selected disabled value="">Choose...</option>
                        <option>ITALY</option>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
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
                <div class="col-md-4 mb-3">
                    <label for="City">City</label>
                    <input type="text" class="form-control" name="city" id="City" required
                           value="<%=splittedAddress[2]%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <label for="Cap">CAP</label>
                    <input type="number" class="form-control" name="cap" id="Cap" required min="0"
                           value="<%=splittedAddress[3]%>">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Street">Street</label>
                    <input type="text" class="form-control" name="street" id="Street" required
                           value="<%=splittedAddress[4]%>"
                           oninput="toUpperCase(this)">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="House-number">House number</label>
                    <input type="number" class="form-control" name="house_number" id="House-number" min="0"
                           value="<%=(splittedAddressLength == 6) ? splittedAddress[5] : ""%>">
                </div>
            </div>
            <button type="submit" id="submit-edit-admin" class="btn btn-primary" name="submit"
                    value=edit_admin">Send
            </button>
            <input type="hidden" name="controllerAction"
                   value="Admin.editAdmin">
            <input type="hidden" name="adminId" value="<%=adminToEdit.getId()%>"/>

        </form>
    </main>
</div>


<script>
    function onLoadFunctionalities() {
        /*addOnClickListenerBtnSidebar();*/
        /* TODO impostare il pulsante Staff su hover in modo da fare l'highlight*/
        /* Setto come selected tra le option di state e region quella uguale al valore dell'admin da modificare */
        setSelectedAttribute("State", "<%=splittedAddress[0]%>");
        setSelectedAttribute("Region","<%=splittedAddress[1]%>")

        <%if(resultPresent){%>
        showResult("<%=result%>", "Message:\n<%=applicationMessage%>");
        <%}%>
    }

    window.addEventListener('load', onLoadFunctionalities);
</script>
</body>
</html>

