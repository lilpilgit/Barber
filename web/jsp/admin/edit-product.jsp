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
    Product productToEdit = null;
    if (request.getAttribute("productToModify") != null) {
        productToEdit = (Product) request.getAttribute("productToModify");
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
                    <input type="text" name="description" id="Description" required
                           value="<%=productToEdit.getDescription()%>"
                           class="form-control">
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
                   value="Product.editProduct">
        </form>
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

