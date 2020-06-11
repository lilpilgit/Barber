<%@ page import="model.mo.Product" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String subject = "Product";
    String controller = "admin.Products";
    /* Prendo l'ArrayList<Product> di tutti i prodotti */
    boolean areProducts = false;
    ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products");
    if (products != null && products.size() != 0)
        areProducts = true;

    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del nuovo cliente è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di cancellazione/modifica ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");
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

    String searchedString = null;
    if (request.getAttribute("searchedString") != null) {
        searchedString = (String) request.getAttribute("searchedString");
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
<%@include file="../../templates/admin-sidebar.jsp" %>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3">
            <button onclick="setControllerAction('admin.Products.showFormNewProduct')"
                    class="button-side" type="button" id='showProducts'>
                <i class="fas fa-plus-square pr-3"></i>
                Add New Product
            </button>
            <div class="collapse navbar-collapse" id="navbarsExample04">
                <ul class="navbar-nav mr-auto"></ul>
                <form class="form-inline my-2 my-md-0" method="post">
                    <input class="form-control" type="text" name="searchString" id="searchString" placeholder="Search"
                           required>
                    <input type="hidden" name="controllerAction" value="admin.Products.searchProducts">
                    <button type="submit" class="btn btn-default"><i class="fas fa-search"></i></button>
                </form>
            </div>
        </nav>
        <%if (areProducts) {%>
        <div class="row justify-content-center">
            <%
                if (searchedString != null) {%>
            <h3>You searched for: <%=searchedString%></h3><br>
        </div>
        <div class="row justify-content-center">
            <form method="post">
                <button type="submit" class="btn btn-default"><i class="fad fa-globe-europe"></i>Show all</button>
                <input type="hidden" name="controllerAction" value="admin.Products.showProducts">
            </form>
            <%}%>
        </div>
        <div class="row justify-content-center">
            <div class="col-auto">
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">ID</th>
                        <th scope="col">Name</th>
                        <th scope="col">Maximum Order Quantity</th>
                        <th scope="col">Producer</th>
                        <th scope="col">Category</th>
                        <th scope="col">Price</th>
                        <th scope="col">Discount</th>
                        <th scope="col">Insert Date</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int i = 1; /* contatore per il numero di prodotti */

                        for (Product p : products) {
                    %>
                    <tr>
                        <th scope="row"><%=i++%>
                        </th>
                        <td><%=p.getId()%>
                        </td>
                        <td><%=p.getName()%>
                        </td>
                        <td><%=p.getMaxOrderQuantity()%>
                        </td>
                        <td><%=p.getProducer()%>
                        </td>
                        <td><%=p.getCategory()%>
                        </td>
                        <td><%=p.getPrice()%>
                        </td>
                        <td><%=p.getDiscount()%>
                        </td>
                        <td><%=p.getInsertDate()%>
                        </td>
                        <td>
                            <button type="button" class="tablebutton" style="color: #1ae2dd;"
                                    title="<%=(p.inShowcase()) ? "Remove from showcase" : "Put in the showcase" %>"
                                    onclick=showcaseById(<%=p.getId()%>,<%=p.inShowcase()%>)>
                                <i class="<%=(p.inShowcase()) ? "far fa-eye checked" : "fas fa-eye-slash unchecked"%>"></i>
                            </button>
                            <button type="button" class="tablebutton" style="color: #1ae2dd;" title="Modify"
                                    title="edit product"
                                    onclick=editProduct(<%=p.getId()%>)>
                                <i class="fas fa-pencil-alt"></i></button>
                            <button type="button" class="trashbutton" title="Delete"
                                    data-target="#alert<%=subject%>"
                                    data-toggle="modal"
                                    onclick=setTmpId(<%=p.getId()%>,'tmpId')>
                                <i class="far fa-trash-alt"></i></button>
                        </td>
                    </tr>
                    <%}%>
                    </tbody>
                </table>
                <%} else {%>
                <h1>There are no products :(</h1>
                <%}%>
                <form method="post" id="action">
                    <input type="hidden" name="controllerAction" value="">
                    <input type="hidden" name="ProductStatus" value="">
                    <input type="hidden" name="<%=subject%>ID" value="">
                </form>

            </div>
        </div>
    </main>
</div>
<input type="hidden" id="tmpId" value="">
<!--MODAL DI CONFERMA ELIMINAZIONE CLIENTE-->
<div class="modal fade" id="alert<%=subject%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="DeleteModal" style="color: rgba(211,4,0,0.75)">You are removing
                    a <%=subject%>...</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                You are attempting to permanently delete a <%=subject%>.<br><br>Are you sure you want to continue?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="deleteById(document.getElementById('tmpId').value, '<%=subject%>','<%=controller%>')">
                    Delete <%=subject%>
                </button>
            </div>
        </div>
    </div>
</div>
<!--FINE MODAL DI CONFERMA ELIMINAZIONE CLIENTE-->


<script>
    window.addEventListener("load", () => {

    })
</script>
</body>
</html>

