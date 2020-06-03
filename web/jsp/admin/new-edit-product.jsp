<%@ page import="model.mo.Product" %>
<%@ page import="model.mo.Structure" %>
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

    /* Prendo il parametro "structure" che mi permette di settare il value dell'input field readonly con name="structure" */
    Structure structure = null;
    boolean structurePresent = false;
    if (request.getAttribute("structure") != null) {
        /* SE MI È STATO PASSATO L'ATTRIBUTO structure */
        structure = (Structure) request.getAttribute("structure");
        structurePresent = true;
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

    /* Setto una stringa da usare all'interno dell html per decidere se devo mostrare campi vuoti ( inserimento nuovo
     * impiegato) o campi già riempiti ( modifica impiegato esistente ) */
    String action = (productToEdit != null) ? "modify" : "insert";

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
                <div class="col-md-4 mb-1">
                    <label for="Name">Name</label>
                    <input type="text" name="name" id="Name" required
                           value="<%=(action.equals("modify")) ? productToEdit.getName() : ""%>"
                           class="form-control">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="Producer">Producer</label>
                    <input type="text" name="producer" id="Producer" placeholder="Garnier" required
                           value="<%=(action.equals("modify")) ? productToEdit.getProducer() : ""%>"
                           class="form-control">
                </div>
            </div>
            <hr>
            <div class="form-row justify-content-center">
                <div class="col-md-5 mb-1">
                    <label for="Category">Category</label>
                    <input type="text" name="category" id="Category" required
                           value="<%=(action.equals("modify")) ? productToEdit.getCategory() : ""%>"
                           class="form-control">
                </div>
                <div class="col-md-5 mb-3">
                    <label for="Description">Description</label>
                    <textarea class="form-control" name="description" id="Description" required
                              rows="8" form="Description"
                              style="resize: none;"><%=(action.equals("modify")) ? productToEdit.getDescription() : ""%></textarea>
                </div>
            </div>
            <hr>
            <div class="form-row justify-content-center text-center">
                <div class="col-md-3 mb-3">
                    <label for="Price">Price</label>
                    <input type="number" name="price" id="Price" required
                           min="0" step="0.01"
                           value="<%=(action.equals("modify")) ? productToEdit.getPrice() : ""%>"
                           class="form-control">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Discount">Discount</label>
                    <input type="number" name="discount" id="Discount"  required
                           value="<%=(action.equals("modify")) ? productToEdit.getDiscount() : ""%>"
                           class="form-control">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Quantity">Quantity</label>
                    <input type="number" name="quantity" id="Quantity"  required
                           value="<%=(action.equals("modify")) ? productToEdit.getQuantity() : ""%>"
                           class="form-control">
                </div>
                <div class="form-row justify-content-center">
                    <div class="col-md-5 mb-3">
                        <label for="Pic_name">Picture</label>
                        <input type="text" name="pic_name" id="Pic_name" required
                               value="<%=(action.equals("modify")) ? productToEdit.getPictureName() : ""%>"
                               class="form-control">
                    </div>
                    <div class="col-md-5 mb-3">
                        <label for="Insert_date">Insert Date </label>
                        <input type="date" name="insert_date" id="Insert_date" required
                               value="<%=(action.equals("modify")) ? productToEdit.getInsertDate() : ""%>"
                               class="form-control">
                    </div>
                </div>
            </div>
            <hr>
            <button type="submit" id="submit-edit-product" class="btn btn-primary" name="submit"
                    value="<%=(action.equals("modify")) ? "edit_product" : "add_new_product"%>">Send
            </button>
            <input type="hidden" name="controllerAction"
                   value="<%=(action.equals("modify")) ? "admin.Products.editProduct" : "admin.Products.addProduct"%>">
            <%if (action.equals("modify")) {%>
            <input type="hidden" name="productId" value="<%=productToEdit.getId()%>"/>
            <%}%>
        </form>
    </main>
</div>


<script>
    window.addEventListener("load",() =>{

    })
</script>
</body>
</html>

