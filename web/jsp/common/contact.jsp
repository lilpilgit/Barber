<%@ page import="model.dao.DAOFactory" %>
<%@ page import="model.dao.StructureDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    boolean data = false;
    boolean error = false; //set to true to test the alert of error on sending email
    String name = request.getParameter("contact_name");
    String email = request.getParameter("contact_email");
    String message = request.getParameter("contact_message");

    if (name != null && email != null && message != null)
        data = true;

%>

<!doctype html>
<html lang="en">
<jsp:include page="/templates/head.html"/>
<body>

<jsp:include page="/templates/header.jsp"/>

<!---------------------------------------------- IMAGE CONTACT  ------------------------------------------------------->

<div class="image-contact">
    <img src="/img/contact/shop_facade.jpeg" class="img-fluid rounded mx-auto d-block" alt="Shop Facade">
    <div class="text">CONTACT US!</div>
</div>
<%if (data) {%>
<div class="alert alert-success alert-dismissible my-alert">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Sent! </strong>We will get back to you as soon as possible.
</div>
<%} else if (error) {%>
<div class="alert alert-danger alert-dismissible my-alert">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Error! </strong>There was an error sending the message ... Please try again later.
</div>
<%}%>
<div class="container h-75">
    <div class="row h-100 justify-content-center align-items-center">
        <div class="col-10 col-md-8 col-lg-6 back-form-contact">
            <!-- Form -->
            <form class="contact-form" action="" method="post">
                <span class="contact-form text-center"><h1>SEND US A MESSAGE</h1></span>
                <p class="description">For more information on our services or to request support, fill out the form in
                    each of its fields and you will be contacted as soon as possible.</p>
                <!-- Input fields -->
                <div class="form-group">
                    <label for="contact_name">Name:</label>
                    <input type="text" pattern="[A-Za-z-.' ]{1,32}" title="Non inserire numeri o caratteri speciali!"
                           class="form-control username" id="contact_name" placeholder="Your name..."
                           name="contact_name" required>
                </div>
                <div class="form-group">
                    <label for="contact_email">Email:</label>
                    <input type="email" class="form-control" id="contact_email" placeholder="Your email..."
                           name="contact_email" required>
                </div>

                <div class="form-group">
                    <label for="contact_text">Message:</label>
                    <textarea class="form-control" id="contact_text" name="contact_message" rows="3"
                              required></textarea>
                </div>
                <div class="pt-2">
                    <button type="submit" class="button btn-contact-form">Submit</button>
                </div>

            </form>

            <!-- Form end -->
        </div>
    </div>
</div>

<jsp:include page="/templates/footer.html"/>
<script type="text/javascript">
    window.onload = function afterPageLoad() {
        setButtonActive("showContact");
        setModalLogin();
    }
</script>
</body>
</html>