<%@ page import="model.mo.Structure" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del nuovo dipendente è andato a buon fine o meno*/
    String result = null;
    if(request.getAttribute("result") != null){
        result = (String) request.getAttribute("result");
    }
    boolean show = false;
    boolean success = false;
    if (result != null) {
        show = true;
        if (result == "success") {
            success = true;
        }
    }
    /* Prendo il parametro "structure" che mi permette di settare il value dell'input hidden con name="structure" */
    Structure structure = null;
    boolean structurePresent = false;
    if(request.getAttribute("structure") != null){
        /* SE MI È STATO PASSATO L'ATTRIBUTO structure */
        structure = (Structure) request.getAttribute("structure");
        structurePresent = true;
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

        <form id='form_add_employee' method="post" class="needs-validation">
            <div class="form-row">
                <%if(structurePresent){%>
                <div class="text-center">
                    <label for="Structure">You are entering a new employee for the structure:</label>
                    <input type="text" id="Structure"
                           value="<%="ID:{" + structure.getId() + "}_" + structure.getName()%>" readonly>
                </div><%}%>
            </div>
            <div class="form-row">
                <div class="col-md-6 mb-3">
                    <label for="Name">First name</label>
                    <input type="text" class="form-control" name="name" id="Name" placeholder="Mario" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="Surname">Last name</label>
                    <input type="text" class="form-control" name="surname" id="Surname" placeholder="Rossi" required>
                </div>
            </div>
            <!-- TODO Dare la possibilita' al dipendente di modificare la password -->
            <div class="form-row">
                <div class="col-md-4">
                    <label for="Email">Email address</label>
                    <input type="email" class="form-control" name="email" id="Email" aria-describedby="emailHelp"
                           required>
                    <small id="emailHelp" class="form-text text-muted">A random password will be sent to that
                        email</small>
                </div>
                <div class="col-md-4">
                    <label for="Phone">Phone number</label>
                    <input type="tel" name="phone" id="Phone" placeholder="3334445556" pattern="[0-9]{5,20}" required
                           class="form-control">
                </div>
                <div class="col-md-4">
                    <label for="Hire-Date">Hire Date</label>
                    <input type="date" class="form-control" name="hire_date" id="Hire-Date" required>
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <label for="Birth-Date">Birth Date</label>
                    <input type="date" class="form-control" name="birth_date" id="Birth-Date" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Fiscal-Code">Fiscal Code</label>
                    <input type="text" pattern="[A-Z0-9]{16}" class="form-control" name="fiscal_code" id="Fiscal-Code"
                           required>
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <label for="State">State</label>
                    <select class="custom-select" name="state" id="State" required>
                        <option selected disabled value="">Choose...</option>
                        <option>Italy</option>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Region">Region</label>
                    <select class="custom-select" name="region" id="Region" required>
                        <option selected disabled value="">Choose...</option>
                        <option>Abruzzo</option>
                        <option>Basilicata</option>
                        <option>Calabria</option>
                        <option>Emilia-Romagna</option>
                        <option>Friuli-Venezia-Giulia</option>
                        <option>Lazio</option>
                        <option>Liguria</option>
                        <option>Lombardia</option>
                        <option>Marche</option>
                        <option>Molise</option>
                        <option>Piemonte</option>
                        <option>Puglia</option>
                        <option>Sardegna</option>
                        <option>Sicilia</option>
                        <option>Toscana</option>
                        <option>Trentino-Alto-Adige</option>
                        <option>Umbria</option>
                        <option>Valle d'Aosta</option>
                        <option>Veneto</option>

                    </select>
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <label for="City">City</label>
                    <input type="text" class="form-control" name="city" id="City" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Address">Address</label>
                    <input type="text" class="form-control" name="address" id="Address" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="House-number">House number</label>
                    <input type="number" class="form-control" name="house_number" id="House-number">
                </div>
            </div>
            <button type="submit" id="submit_new_employee" class="btn btn-primary" name="submit"
                    value="add_new_employee">Add new Employee
            </button>
            <input type="hidden" name="controllerAction" value="Staff.addEmployee">
        </form>
    </main>
</div>


<script>
    function onLoadFunctionalities() {
        /*addOnClickListenerBtnSidebar();*/
        /*showResult("<%=show%>", "<%=result%>");*/
    }

    window.addEventListener('load', onLoadFunctionalities);
</script>
</body>
</html>

