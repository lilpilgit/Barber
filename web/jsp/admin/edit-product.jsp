<%@ page import="model.mo.Product" %>
<%@ page import="model.mo.User" %>
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
    Product productToEdit = null;
    if (request.getAttribute("productToModify") != null) {
        productToEdit = (Product) request.getAttribute("productToModify");
    }

    /* Prendo il parametro "loggedOn" che mi consente di sapere se l'utente attuale è loggato o meno */
    Boolean loggedOn = false;
    if (request.getAttribute("loggedOn") != null) {
        loggedOn = (Boolean) request.getAttribute("loggedOn");
    }

    /* Prendo il parametro "loggedUser" che mi consente di sapere qual'è l'utente attualmente loggato */
    User loggedUser = null;
    if (request.getAttribute("loggedUser") != null && loggedOn != null) {
        loggedUser = (User) request.getAttribute("loggedUser");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Products";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showProducts";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="/templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">

        <form id='form_edit_product' method="post" class="needs-validation">
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-5 mb-3">
                    <label for="Name">Name</label>
                    <input type="text" name="name" id="Name" required
                           value="<%=productToEdit.getName()%>"
                           class="form-control">
                </div>
                <div class="col-md-5 mb-3">
                    <label for="Producer">Producer</label>
                    <input type="text" name="phone" id="Producer" placeholder="Garnier" required
                           value="<%=productToEdit.getProducer()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-5 mb-3">
                    <label for="Category">Category</label>
                    <input type="text" name="category" id="Category" required
                           value="<%=productToEdit.getCategory()%>"
                           class="form-control">
                </div>
                <div class="col-md-5 mb-3">
                    <label for="Description">Description</label>
                    <textarea class="form-control" name="description" id="Description" required
                              rows="8" form="Description" style="resize: none;"><%=productToEdit.getDescription()%></textarea>
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-5 mb-3">
                    <label for="Pic_name">Picture</label>
                    <input type="text" name="pic_name" id="Pic_name" required
                           value="<%=productToEdit.getPictureName()%>"
                           class="form-control">
                </div>
                <div class="col-md-5 mb-3">
                    <label for="Insert_date">Insert Date </label>
                    <input type="date" name="insert_date" id="Insert_date" required
                           value="<%=productToEdit.getInsertDate()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <div class="form-row justify-content-center text-center">
                <div class="col-md-3 mb-3">
                    <label for="Price">Price</label>
                    <input type="number" name="price" id="Price" required
                           min="0" step="0.01"
                           value="<%=productToEdit.getPrice()%>"
                           class="form-control">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Discount">Discount</label>
                    <input type="number" name="discount" id="Discount"  required
                           value="<%=productToEdit.getDiscount()%>"
                           class="form-control">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Quantity">Quantity</label>
                    <input type="number" name="quantity" id="Quantity"  required
                           value="<%=productToEdit.getQuantity()%>"
                           class="form-control">
                </div>
            </div>
            <br>
            <hr>
            <br>
            <button type="submit" id="submit-edit-product" class="btn btn-primary" name="submit"
                    value=edit_product">Send
            </button>
            <input type="hidden" name="controllerAction"
                   value="admin.Products.editProduct">
        </form>
    </main>
</div>


<script>
    window.addEventListener("load",() =>{

    })
</script>
</body>
</html>

