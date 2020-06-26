<%@page import="model.mo.Product" %>
<%@page import="model.mo.User" %>
<%@page import="java.util.ArrayList" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page errorPage="../error/404.jsp" %>
<%@page session="false" %>
<%

    /* Prendo il parametro "categoryFiltered" per settare il dropdown dopo il refresh della pagina */
    String categoryFiltered = (request.getAttribute("categoryFiltered") != null) ? (String) request.getAttribute("categoryFiltered") : "All";

    /* Prendo il parametro "brandFiltered" per settare il dropdown dopo il refresh della pagina */
    String brandFiltered = (request.getAttribute("brandFiltered") != null) ? (String) request.getAttribute("brandFiltered") : "All";

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

    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento della prenotazione è andato a buon fine o meno*/
    String result = null;
    boolean resultPresent = false;
    if (request.getAttribute("result") != null) {
        result = (String) request.getAttribute("result");
        resultPresent = true;
    }

    /* Prendo il parametro "applicationMessage" che è il messaggio proveniente dal controller sul Server relativo all'operazione
     * di login/logout ( se è andata a buon fine o meno) */
    String applicationMessage = null;
    if (request.getAttribute("applicationMessage") != null) {
        applicationMessage = (String) request.getAttribute("applicationMessage");/*null se loggato correttamente*/
    }

    /* Prendo l'ArrayList<Product> di tutti i prodotti dal parametro showcase */
    ArrayList<Product> products_to_show = null;
    if (request.getAttribute("products") != null) {
        products_to_show = (ArrayList<Product>) request.getAttribute("products");
    } else {
        throw new RuntimeException("PARAMETRO products_to_show NULL");
    }

    /* Prendo l'ArrayList<String> di tutte le categorie */
    ArrayList<String> all_categories = null;

    if (request.getAttribute("categories") != null) {
        all_categories = (ArrayList<String>) request.getAttribute("categories");
    } else {
        throw new RuntimeException("PARAMETRO all_categories NULL");
    }


    /* Prendo l'ArrayList<String> di tutti i brands */
    ArrayList<String> all_producers = null;
    if (request.getAttribute("brands") != null) {
        all_producers = (ArrayList<String>) request.getAttribute("brands");
    } else {
        throw new RuntimeException("PARAMETRO all_producers NULL");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Shop";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showShop";

%>
<!doctype html>
<html lang="en">
<%@ include file="/templates/head.jsp" %>
<body>

<%@ include file="/templates/header.jsp" %>

<!-------------------------------------------- Welcome Our Store --------------------------------------------------------->

<div class="container-fluid text-center pt-5">
    <h1 class="display-3">Welcome To Our Shop!</h1>
</div>

<!-------------------------------------------- NavBar Product --------------------------------------------------------->

<form name="filterForm" id="filterForm" method="post">

    <%--    <input type="hidden" name="filter" value="0">--%>
    <%--    <input type="hidden" name="category" value="All">--%>
    <%--    <input type="hidden" name="brand" value="All">--%>

    <div class="container pt-4 pb-3" style="border-bottom: 2px solid black;">
        <label for="category_select_menu">Category:<span>&nbsp;</span></label>
        <select id="category_select_menu" name="category">
            <%int num_cat = 0;%>
            <option id="category_<%=num_cat%>" value="All">All</option>
            <%
                for (String cat : all_categories) {
                    boolean selected = false;
                    if (cat.equals(categoryFiltered)) {
                        selected = true;
                    }
                    num_cat++;
            %>
            <option id="category_<%=num_cat%>" value="<%=cat%>" <%=(selected) ? "selected" : ""%>><%=cat%>
            </option>
            <%}%>
        </select>
        <!-- Dropdown -->
        <label for="brand_select_menu">Brand:<span>&nbsp;</span></label>
        <select id="brand_select_menu" name="brand">
            <%int num_brand = 0;%>
            <option id="brand_<%=num_brand%>" value="All">All</option>
            <%
                for (String producer : all_producers) {
                    boolean selected = false;
                    if (producer.equals(brandFiltered)) {
                        selected = true;
                    }
                    num_brand++;
            %>
            <option id="brand_<%=num_brand%>" value="<%=producer%>" <%=(selected) ? "selected" : ""%>><%=producer%>
            </option>
            <%}%>
        </select>
        <!-- Bottone che submitta la form filterForm con gli hidden input il cui value viene modificati con la setFormFilter() e i relativi listener -->
        <button type="submit" class="btn ml-3 active2" id='filter_btn'>Filter</button>
    </div>
    <input type="hidden" name="controllerAction" value="home.Shop.showShop">
</form>
<!-- form per mostrare il prodotto scelto -->
<form name="showProductForm" id="showProductForm" method="post">
    <input type="hidden" name="controllerAction" value="home.Product.showProduct">
    <input type="hidden" name="idProduct" value="">
</form>

<!--------------------------------------------- Product section ------------------------------------------------------->

<div class="container py-2">
    <div class="row padding">
        <%
            if (products_to_show != null && products_to_show.isEmpty()) {
                /*there is no product to display*/
        %>
        <div class="no-products-container">
            <h3 class="no-products-title">We are sorry...</h3>
            <div>
                <img class="no-products-thumb" src="img/shop/empty_warehouse.png" alt="No products present">
                <p>What you requested did not produce any results</p>
            </div>
        </div>

        <%
        } else {
            for (Product product : products_to_show) {
        %>
        <div class="col-md-4">
            <div class="card-shop">
                <div class="text-center ">
                    <div class="tab-content-shop"><img src="img/products/<%=product.getPictureName()%>"
                                                       alt="<%=product.getPictureName()%>"></div>
                </div>
                <div class="card-body toBottom text-center">
                    <h4 class="card-title"><%=product.getName()%>
                    </h4>
                    <p class="card-text">&euro;<%=product.getPrice()%>
                    </p>
                    <div class="container pb-2">
                        <button class="btn btn-dark" onclick=setProductForm('<%=product.getId()%>')>Show</button>
                    </div>
                </div>
            </div>
        </div>
        <%
                }
            }
        %>
    </div>
</div>
<%@ include file="/templates/footer.html" %>
<script type="text/javascript">
    window.addEventListener("load", () => {

    })
</script>
</body>
</html>

