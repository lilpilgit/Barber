<%@ page import="model.mo.Product" %>
<%@ page import="model.mo.Structure" %>
<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page errorPage="../error/404.jsp" %>--%>
<%@ page session="false" %>
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
<%@include file="/templates/admin-sidebar.jsp" %>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content text-center">

        <form id="form_edit_product" method="post" enctype="multipart/form-data" class="needs-validation">
            <br>
            <div class="form-row justify-content-center">
                <div class="col-md-2 mb-1">
                    <label for="Name">Name</label>
                    <input type="text" name="name" id="Name" required
                           value="<%=(action.equals("modify")) ? productToEdit.getName() : ""%>"
                           class="form-control">
                </div>
                <div class="col-md-2 mb-3">
                    <label for="Producer">Producer</label>
                    <input type="text" name="producer" id="Producer" placeholder="Garnier" required
                           value="<%=(action.equals("modify")) ? productToEdit.getProducer() : ""%>"
                           class="form-control">
                </div>
                <div class="col-md-2 mb-3">
                    <label for="Category">Category</label>
                    <input type="text" name="category" id="Category" required
                           value="<%=(action.equals("modify")) ? productToEdit.getCategory() : ""%>"
                           class="form-control">
                </div>
            </div>
            <hr>
            <div class="form-row justify-content-center">
                <div class="col-md-5 mb-3">
                    <label for="Description">Description</label>
                    <textarea class="form-control" name="description" id="Description" required
                              rows="8" form="form_edit_product"
                              style="resize: none;"><%=(action.equals("modify")) ? productToEdit.getDescription() : ""%></textarea>
                </div>
            </div>
            <hr>
            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-3">
                    <label for="Price">Price</label>
                    <input type="number" name="price" id="Price" required
                           min="0" step="0.01"
                           onchange="this.value = this.value.replace(/,/g, '.')"
                           value="<%=(action.equals("modify")) ? productToEdit.getPrice() : ""%>"
                           class="form-control">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="Discount">Discount</label>
                    <input type="number" name="discount" id="Discount" min="0" max="100" required
                           value="<%=(action.equals("modify")) ? productToEdit.getDiscount() : "0"%>"
                           class="form-control">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="maxOrderQuantity">Maximum Order Quantity</label>
                    <input type="number" name="maxOrderQuantity" id="maxOrderQuantity" min="0" required
                           value="<%=(action.equals("modify")) ? productToEdit.getMaxOrderQuantity() : ""%>"
                           class="form-control">
                </div>
            </div>
            <!--https://plugins.krajee.com/file-avatar-upload-demo snippet per modifica e caricamento immagine -->
            <%if (action.equals("modify")) {%>
            <div class="form-row justify-content-center">
                <div class="col-md-6">
                    <div class="card-shop"><!-- da modificare eventualmente-->
                        <div class="text-center ">
                            <div class="tab-content-shop"><img src="img/products/<%=productToEdit.getPictureName()%>"
                                                               alt="<%=productToEdit.getPictureName()%>"></div>
                        </div>
                    </div>
                </div>
            </div>
            <%}%>
            <div class="form-row justify-content-center pb-4">
                <div class="col-6">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Picture product</span>
                    </div>
                </div>
                <div class="custom-file">
                    <input type="file" name="picture" class="custom-file-input form-control"
                           id="Picture" <%=(action.equals("modify")) ? "" : "required"%>>
                    <label class="custom-file-label" for="Picture"><%=(action.equals("modify")) ? "Change image" : "Choose image"%></label>
                </div>
                </div>
            </div>

            <%if (action.equals("modify")) {%>
            <input type="hidden" name="picture_name_stored"
                   id="Picture_name_stored" readonly required
                   value="<%=productToEdit.getPictureName()%>">
            <%}%>

            <div class="form-row justify-content-center">
                <div class="col-md-3 mb-3">
                    <label for="Insert_date">Insert Date </label>
                    <input type="date" name="insert_date" id="Insert_date" required readonly
                           value="<%=(action.equals("modify")) ? productToEdit.getInsertDate() : ""%>"
                           class="form-control">
                </div>
            </div>

            <hr>
            <br>
            <button type="submit" id="submit-edit-product" class="btn btn-primary" name="submit"
                    value="<%=(action.equals("modify")) ? "edit_product" : "add_new_product"%>">Send
            </button>
            <input type="hidden" name="controllerAction"
                   value="<%=(action.equals("modify")) ? "admin.Products.editProduct" : "admin.Products.addProduct"%>">
            <%if (action.equals("modify")) {%>
            <input type="hidden" name="productId" value="<%=productToEdit.getId()%>"/>
            <%}%>
            <!-- parametro per sapere se è nello showcase o meno -->
            <%if (action.equals("modify")) {%>
            <input type="hidden" name="showcase" value="<%=productToEdit.inShowcase()%>"/>
            <%}%>
        </form>
    </main>
</div>


<script>
    window.addEventListener("load", () => {
        <%if(!action.equals("modify")){%>
        setCurrentDate("Insert_date");
        <%}%>

        /* use javascript to show the name of the choosed file,
         * as written in the documentation: https://v4-alpha.getbootstrap.com/components/forms/#file-browser
        */
        $('#Picture').on('change', function () {
            //get the file name
            let fileName = $(this).val().replace('C:\\fakepath\\', " ");
            //replace the "Choose a file" label
            $(this).next('.custom-file-label').html(fileName);
        })

    })
</script>
</body>
</html>

