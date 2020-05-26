<%@ page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
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

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Products";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showProducts";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="../../templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3">
            <button onclick="setControllerAction('Staff.addEmployee')"
                    class="button-side" type="button" id='showProducts'>
                <i class="fas fa-plus-square pr-3"></i>
                Add New Products
            </button>
            <div class="collapse navbar-collapse" id="navbarsExample04">
                <ul class="navbar-nav mr-auto"></ul>
                <form class="form-inline my-2 my-md-0">
                    <input class="form-control" type="text" placeholder="Search">
                </form>
            </div>
        </nav>
        <%if (areProducts) {%>
        <div class="row justify-content-center">
            <div class="col-auto">
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">ID</th>
                        <th scope="col">Name</th>
                        <th scope="col">Quantity</th>
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
                        <td><%=p.getQuantity()%>
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
                                    title="<%=p.getShowcase() ? "Remove from showcase" : "Put in the showcase" %>"
                                    onclick=showcaseById(<%=p.getId()%>,<%=p.getShowcase()%>)>
                                <i class="<%=(p.getShowcase()) ? "far fa-eye checked" : "fas fa-eye-slash unchecked"%>"></i>
                            </button>
                            <button type="button" class="tablebutton" style="color: #1ae2dd;" title="Modify"
                                    title="edit product"
                                    onclick=editProduct(<%=p.getId()%>)>
                                <i class="fas fa-pencil-alt"></i></button>
                            <button type="button" class="trashbutton" title="Delete">
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
                    <input type="hidden" name="ProductID" value="">
                </form>

            </div>
        </div>
    </main>
</div>


<script>
    window.addEventListener("load",() =>{

    })
</script>
</body>
</html>

