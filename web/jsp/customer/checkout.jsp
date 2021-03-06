<%@ page import="model.mo.ExtendedProduct" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.Structure" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
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

    /* Prendo il parametro "result" che si occupa di informarmi del risultato delle possibili operazioni previste */
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

    /* Prendo l'oggetto struttura per conoscere le informazioni da mostrare nel footer */
    Structure structure = null;
    if(request.getAttribute("structure") != null){
        structure = (Structure) request.getAttribute("structure");
    }

    ArrayList<ExtendedProduct> checkoutProducts = null;
    if (request.getAttribute("checkoutProducts") != null) {
        checkoutProducts = (ArrayList<ExtendedProduct>) request.getAttribute("checkoutProducts");
    }

    /* Numero di prodotti da mostrare vicino la scritta Order Summary */
    Integer numProducts = 0;
    for (int i = 0; i < checkoutProducts.size(); i++) {
        numProducts += checkoutProducts.get(i).getRequiredQuantity();
    }

    BigDecimal totalPrice = null;
    if (request.getAttribute("totalPrice") != null) {
        totalPrice = (BigDecimal) request.getAttribute("totalPrice");
    }

    BigDecimal totalSaved = null;
    if (request.getAttribute("totalSaved") != null) {
        totalSaved = (BigDecimal) request.getAttribute("totalSaved");
    }

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
    String menuActiveLink = "Checkout";

    /* In questo caso non esiste un bottone di cui cambiare la classe ma la variabile va comunque definita
     *  per evitare errori, basta solamente lasciarla a null */
    String idBtnAttivo = null;

%>
<!doctype html>
<html lang="en">
<%@include file="/templates/head.inc" %>
<body>
<%@include file="/templates/header.inc" %>

<!------------------------------------------------ Book section ----------------------------------------------------->

<div class="container pt-4">
    <div class="row">
        <div class="col-md-4 order-md-2 mb-4">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
                <span class="text-muted">Order Summary</span>
                <span class="badge badge-secondary badge-pill"><%=numProducts%></span>
            </h4>
            <ul class="list-group mb-3">
                <%
                    for (ExtendedProduct ep : checkoutProducts) {
                %>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0"><%=ep.getName()%>
                        </h6>
                        <small class="text-muted font-italic">x<%=ep.getRequiredQuantity()%>
                        </small>
                    </div>
                    <span class="text-muted"><%=ep.getPrice()%>(each)</span>
                </li>
                <%}%>
                <li class="list-group-item d-flex justify-content-between bg-light">
                    <div class="text-success">
                        <h6 class="my-0">Total saved</h6>
                    </div>
                    <span class="text-success">-<%=totalSaved%></span>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                    <span>Total (&euro;)</span>
                    <strong><%=totalPrice%>
                    </strong>
                    <input type="hidden" name="totalPrice" id="totalPrice" value="<%=totalPrice%>" form="buyForm"
                           readonly>
                </li>
            </ul>
        </div>
        <div class="col-md-8 order-md-1">
            <h4 class="mb-3">Shipping address</h4>

            <form class="needs-validation" id="buyForm" method="post">
                <hr class="mb-4">
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" name="sameAddress" value="different"
                           id="same-address"
                           onclick="autoFillShippingAddress(this,addressDB)">
                    <label class="custom-control-label" for="same-address">Shipping address is the same as my billing
                        address</label>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="firstName">First name</label>
                        <input type="text" class="form-control" id="firstName" value="<%=customer.getName()%>" readonly
                               required>
                        <div class="invalid-feedback">
                            Valid first name is required.
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastName">Last name</label>
                        <input type="text" class="form-control" id="lastName" value="<%=customer.getSurname()%>"
                               readonly required>
                        <div class="invalid-feedback">
                            Valid last name is required.
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="email">Email</label>
                    <input type="email" class="form-control" id="email" value="<%=customer.getEmail()%>" readonly
                           required>
                    <div class="invalid-feedback">
                        Please enter a valid email address for shipping updates.
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-5 mb-3" id="div-state">
                        <label for="State">State</label>
                        <select class="custom-select d-block w-100" name="state" id="State" required>
                            <option selected disabled value="">Choose...</option>
                            <option>ITALY</option>
                        </select>
                        <div class="invalid-feedback">
                            Please select a valid state.
                        </div>
                    </div>
                    <div class="col-md-4 mb-3" id="div-region">
                        <label for="Region">Region</label>
                        <select class="custom-select d-block w-100" name="region" id="Region" required>
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
                        <div class="invalid-feedback">
                            Please provide a valid region for shipping.
                        </div>
                    </div>
                    <div class="col-md-5 mb-5 text-center">
                        <label for="City">City</label>
                        <input type="text" class="form-control" name="city" id="City" required
                               oninput="this.value=this.value.toUpperCase();">
                        <div class="invalid-feedback">
                            Please provide a valid city for shipping.
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="Cap">Postal Code</label>
                        <input type="number" class="form-control" name="cap" id="Cap" required min="0"
                               oninput="this.value=this.value.toUpperCase();">
                        <div class="invalid-feedback">
                            Please provide a valid Postal Code for shipping.
                        </div>
                    </div>
                    <div class="col-md-5 mb-5">
                        <label for="Street">Street</label>
                        <input type="text" class="form-control" name="street" id="Street" required
                               oninput="this.value=this.value.toUpperCase();">
                        <div class="invalid-feedback">
                            Please provide a valid Street for shipping.
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="House-number">House Number</label>
                        <input type="number" class="form-control" name="house_number" id="House-number"
                               min="0">
                        <div class="invalid-feedback">
                            Please provide a valid House Number for shipping.
                        </div>
                    </div>
                </div>
                <hr class="mb-4">
                <h4 class="mb-3">Payment</h4>
                <div class="d-block my-3">
                    <div class="custom-control custom-radio">
                        <input id="credit" name="paymentMethod" type="radio" class="custom-control-input" checked
                               required>
                        <label class="custom-control-label" for="credit">Credit card</label>
                    </div>
                    <div class="custom-control custom-radio">
                        <input id="debit" name="paymentMethod" type="radio" class="custom-control-input" required>
                        <label class="custom-control-label" for="debit">Debit card</label>
                    </div>
                    <div class="custom-control custom-radio">
                        <input id="paypal" name="paymentMethod" type="radio" class="custom-control-input" required>
                        <label class="custom-control-label" for="paypal">Paypal</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="cc-name">Name on card</label>
                        <input type="text" class="form-control" id="cc-name" placeholder="" required>
                        <small class="text-muted">Full name as displayed on card</small>
                        <div class="invalid-feedback">
                            Name on card is required
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="cc-number">Credit card number</label>
                        <input type="text" class="form-control" id="cc-number" placeholder="" required>
                        <div class="invalid-feedback">
                            Credit card number is required
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="cc-expiration">Expiration</label>
                        <input type="text" class="form-control" id="cc-expiration" placeholder="" required>
                        <div class="invalid-feedback">
                            Expiration date required
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="cc-expiration">CVV</label>
                        <input type="text" class="form-control" id="cc-cvv" placeholder="" required>
                        <div class="invalid-feedback">
                            Security code required
                        </div>
                    </div>
                </div>
                <hr class="mb-4">
                <%for (ExtendedProduct ep : checkoutProducts) {%>
                <input type="hidden" name="ids" value="<%=ep.getId()%>">
                <input type="hidden" name="quantities" value="<%=ep.getRequiredQuantity()%>">
                <%}%>
                <input type="hidden" name="controllerAction" value="home.Checkout.processCheckout">
                <button class="btn btn-primary btn-lg btn-block" type="submit">Continue to checkout
                </button>
            </form>

        </div>
    </div>
</div>
<!---------------------------------------------- End of Book section ------------------------------------------------>

<%@ include file="/templates/footer.inc" %>
<script type="text/javascript">

    let addressDB;
    window.addEventListener('load', () => {
        /* creo un array con le informazioni di fatturazione prese dal db */
        addressDB = ["<%=splittedAddress[0]%>", "<%=splittedAddress[1]%>", "<%=splittedAddress[2]%>", "<%=splittedAddress[3]%>", "<%=splittedAddress[4]%>", "<%=(splittedAddressLength == 6) ? splittedAddress[5] : ""%>"];

    });

</script>
</body>
</html>