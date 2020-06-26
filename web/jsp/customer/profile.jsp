<%@ page import="model.mo.User" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
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

    /* messaggio dall applicativo */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Prendo il parametro "result" per conoscere l'esito delle operazioni di modifica dei dati */
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "customer" che indica i dati del cliente da mostrare nella form  */
    User customer = null;
    if (request.getAttribute("customer") != null) {
        customer = (User) request.getAttribute("customer");
    }

    /* Faccio lo split dell'address per poterlo mostrare all'interno dei vari campi */
    String[] splittedAddress = null;
    int splittedAddressLength = 0;
    /* Splitto sulla | il campo address dell'utente per poterlo visualizzare in ogni campo della form */
    if (customer != null) {
        String address = customer.getAddress();
        splittedAddress = address.split("\\|");
        splittedAddressLength = splittedAddress.length;
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Profile";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showProfile";

%>
<!doctype html>
<html lang="en">

<%@include file="/templates/head.jsp" %>

<body>

<%@include file="/templates/header.jsp" %>
<!------------------------------------------------ Book section ----------------------------------------------------->

<div class="container py-4">
    <div class="cart-box">
        <div class="row justify-content-center pb-4">
            <h3>Welcome <b><%=customer.getName()%>
            </b> to your edit section</h3>
        </div>
        <hr>
        <form id='form_edit_profile' method="post" class="needs-validation">
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-2">
                    <label for="Name">Name</label>
                    <input type="text" class="form-control" name="name" id="Name"
                           autocapitalize="on"
                           placeholder="Mario" required
                           value="<%=customer.getName()%>">
                </div>
                <div class="col-md-3 mb-2">
                    <label for="Surname">Surname</label>
                    <input type="text" class="form-control" name="surname" id="Surname"
                           style="text-transform: capitalize;" placeholder="Rossi" required
                           value="<%=customer.getSurname()%>">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-md-3">
                    <label for="Email">Email address</label>
                    <input type="email" class="form-control" name="email" id="Email" aria-describedby="emailHelp"
                           value="<%=customer.getEmail()%>" style="text-transform: lowercase;"
                           required>
                </div>
                <div class="col-md-3">
                    <label for="Phone">Phone number</label>
                    <input type="tel" name="phone" id="Phone" placeholder="3334445556" pattern="[0-9]{5,20}" required
                           value="<%=customer.getPhone()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-2">
                    <label for="State">State</label>
                    <select class="custom-select" name="state" id="State" required>
                        <option selected disabled value="">Choose...</option>
                        <option>ITALY</option>
                    </select>
                </div>
                <div class="col-md-3 mb-2">
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
                <div class="col-md-3 mb-2">
                    <label for="City">City</label>
                    <input type="text" class="form-control" name="city" id="City" required
                           value="<%=splittedAddress[2]%>"
                           oninput="toUpperCase(this)">
                </div>
            </div>
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-2">
                    <label for="Cap">CAP</label>
                    <input type="number" class="form-control" name="cap" id="Cap" required min="0"
                           value="<%=splittedAddress[3]%>">
                </div>
                <div class="col-md-3 mb-2">
                    <label for="Street">Street</label>
                    <input type="text" class="form-control" name="street" id="Street" required
                           value="<%=splittedAddress[4]%>"
                           oninput="toUpperCase(this)">
                </div>
                <div class="col-md-3 mb-2">
                    <label for="House-number">House number</label>
                    <input type="number" class="form-control" name="house_number" id="House-number" min="0"
                           value="<%=(splittedAddressLength == 6) ? splittedAddress[5] : ""%>">
                </div>
            </div>
            <div class="text-center pt-1">
                <button type="submit" form="form_edit_profile" class="btngeneric">Update profile</button>
            </div>
            <input type="hidden" name="controllerAction" value="home.Profile.updateProfile">
        </form>
    </div>
</div>


<!---------------------------------------------- End of Book section ------------------------------------------------>

<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {
        setSelectedAttribute("State", "<%=splittedAddress[0]%>");
        setSelectedAttribute("Region", "<%=splittedAddress[1]%>");
    })
</script>
</body>
</html>