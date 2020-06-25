<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%

    /* Prendo il parametro "contacted" che mi consente di sapere se l'email è stata inviata o meno */
    boolean contacted = false;
    if (request.getAttribute("contacted") != null) {
        contacted = (Boolean) request.getAttribute("contacted");
    }

    /* Prendo il parametro "loggedOn" che mi consente di sapere se l'utente attuale è loggato o meno */
    boolean loggedOn = false;
    if (request.getAttribute("loggedOn") != null) {
        loggedOn = (Boolean) request.getAttribute("loggedOn");
    }

    /* Prendo il parametro "loggedUser" che mi consente di sapere qual è l'utente attualmente loggato */
    User loggedUser = null;
    if (loggedOn && request.getAttribute("loggedUser") != null) {
        loggedUser = (User) request.getAttribute("loggedUser");
    }

    /* Se c'è un utente loggato prendo i suoi dati */
    User customer = null;
    if (loggedUser != null) {
        if (request.getAttribute("customer") != null) {
            customer = (User) request.getAttribute("customer");
        }
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di invio messaggio ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Contact";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showContact";

%>
<!doctype html>
<html lang="en">
<%@include file="/templates/head.jsp" %>
<body>

<%@include file="/templates/header.jsp" %>

<!---------------------------------------------- IMAGE CONTACT  ------------------------------------------------------->

<div class="image-contact">
    <img src="img/contact/shop_facade.jpeg" class="img-fluid rounded mx-auto d-block" alt="Shop Facade">
    <div class="text">CONTACT US!</div>
</div>
<%if (contacted) {%>
<div class="alert alert-success alert-dismissible my-alert">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Sent! </strong><%="\n" + applicationMessage%>
</div>
<%} else if( applicationMessage != null ) {%>
<div class="alert alert-danger alert-dismissible my-alert">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Error! </strong><%="\n" + applicationMessage%>
</div>
<%}%>
<div class="container py-4">
    <div class="row h-100 justify-content-center align-items-center">
        <div class="col-10 col-md-8 col-lg-6 back-form-contact">
            <!-- Form -->
            <form class="contact-form" id="contactForm" action="" method="post">
                <span class="contact-form text-center"><h1>SEND US A MESSAGE</h1></span>
                <p class="description">For more information on our services or to request support, fill out the form in
                    each of its fields and you will be contacted as soon as possible.</p>
                <!-- Input fields -->
                <div class="form-group">
                    <label for="contact_name">Name:</label>
                    <input type="text" pattern="[A-Za-z-.' ]{1,32}" title="Do not enter numbers or special characters!"
                           class="form-control username" id="contact_name" placeholder="Your name..."
                           oninput="this.value=this.value.toUpperCase();"
                           name="contact_name" value="<%=(loggedOn)? customer.getName() : ""%>" <%if(loggedOn){%>
                           readonly<%}%> required>
                </div>
                <div class="form-group">
                    <label for="contact_email">Email:</label>
                    <input type="email" class="form-control" id="contact_email" placeholder="Your email..."
                           name="contact_email" value="<%=(loggedOn)? customer.getEmail() : ""%>" <%if(loggedOn){%>
                           readonly<%}%> required>
                </div>

                <div class="form-group">
                    <label for="contact_text">Message:</label>
                    <textarea class="form-control" id="contact_text" name="contact_message" rows="3"
                              placeholder="I'd love to..."
                              required></textarea>
                </div>
                <div class="pt-2">
                    <button type="submit" form="contactForm" class="button btn-contact-form">Submit</button>
                </div>
                <input type="hidden" name="controllerAction" value="home.Contact.contact">

            </form>

            <!-- Form end -->
        </div>
    </div>
</div>

<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {
    });
</script>
</body>
</html>