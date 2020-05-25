<%@ page import="model.mo.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    /* Prendo il parametro "result" che si occupa di indicarmi se la modifica dei dati è andata a buon fine o meno*/
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
    Product productToedit = null;
    if (request.getAttribute("structureToModify") != null) {
        productToedit = (Product) request.getAttribute("productToModify");
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

        <h1>Banane lamponi</h1>
       <%-- <form id='form_edit_structure' method="post" class="needs-validation">
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="Name">Name</label>
                    <input type="text" name="name" id="Name" required
                           value="<%=structureToEdit.getName()%>"
                           class="form-control">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="Phone">Phone number</label>
                    <input type="tel" name="phone" id="Phone" placeholder="3334445556" pattern="[0-9]{5,20}" required
                           value="<%=structureToEdit.getPhone()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <label for="Opening-Time">Opening Time</label>
                    <input type="time" class="form-control" name="opening_time" id="Opening-Time" required
                           value="<%=structureToEdit.getOpeningTime()%>">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Closing-Time">Closing Time</label>
                    <input type="time" class="form-control" name="closing_time" id="Closing-Time" required
                           value="<%=structureToEdit.getClosingTime()%>"
                           class="form-control">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Slot">Slot</label> <!--TODO:step default 60 ==> 60 secondi-->
                    <input type="time" class="form-control" name="slot" id="Slot" required
                           value="<%=structureToEdit.getSlot()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
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
                    <input type="text" class="form-control"  name="street" id="Street" required
                           value="<%=splittedAddress[4]%>"
                           oninput="toUpperCase(this)">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="House-number">House number</label>
                    <input type="number" class="form-control" name="house_number" id="House-number" min="0"
                           value="<%=(splittedAddressLength == 6) ? splittedAddress[5] : ""%>">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <button type="submit" id="submit-edit-structure" class="btn btn-primary" name="submit"
                    value=edit_structure">Send
            </button>
            <input type="hidden" name="controllerAction"
                   value="Structure.editStructure">
        </form>--%>
    </main>
</div>


<script>
    window.addEventListener("load",() =>{
        /*addOnClickListenerBtnSidebar();*/
        /* TODO impostare il pulsante Product su hover in modo da fare l'highlight*/
        /* Setto come selected tra le option di state e region quella uguale al valore della struttura da modificare */
        <%--setSelectedAttribute("State", "<%=splittedAddress[0]%>");--%>
        <%--setSelectedAttribute("Region", "<%=splittedAddress[1]%>")--%>

        <%if(resultPresent){%>
        showResult("<%=result%>", "Message:\n<%=applicationMessage%>");
        <%}%>
    })
</script>
</body>
</html>

