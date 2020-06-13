<%@page import="model.mo.Product" %>
<%@page import="model.mo.User" %>
<%@page import="java.util.ArrayList" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%

    /* Prendo il parametro "categoryFiltered" per settare il dropdown dopo il refresh della pagina */
    String categoryFiltered = (request.getAttribute("categoryFiltered") != null) ? (String) request.getAttribute("categoryFiltered") : "All";

    /* Prendo il parametro "brandFiltered" per settare il dropdown dopo il refresh della pagina */
    String brandFiltered = (request.getAttribute("brandFiltered") != null) ? (String) request.getAttribute("brandFiltered") : "All";

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
    }

    /* Prendo l'ArrayList<String> di tutte le categorie */
    ArrayList<String> all_category = null;
    if (request.getAttribute("categories") != null) {
        all_category = (ArrayList<String>) request.getAttribute("categories");
    }

    /* Prendo l'ArrayList<String> di tutti i brands */
    ArrayList<String> all_producer = null;
    if (request.getAttribute("brands") != null) {
        all_producer = (ArrayList<String>) request.getAttribute("brands");
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

<div class="container pt-4 pb-3" style="border-bottom: 2px solid black;">
    <label for="category_select_menu">Category:<span>&nbsp;</span></label>
    <select id="category_select_menu" name="category">
        <%long id_cat = 0;%>
        <option id="category_<%=id_cat%>" value="<%=id_cat%>">All</option>
        <%
            for (String cat : all_category) {
                id_cat++;
        %>
        <option id="category_<%=id_cat%>" value="<%=id_cat%>"><%=cat%>
        </option>
        <%}%>
    </select>
    <!-- Dropdown -->
    <label for="brand_select_menu">Brand:<span>&nbsp;</span></label>
    <select id="brand_select_menu" name="brand">
        <%long id_brand = 0;%>
        <option id="brand_<%=id_brand%>" value="<%=id_brand%>">All</option>
        <%
            for (String producer : all_producer) {
                id_brand++;
        %>
        <option id="brand_<%=id_brand%>" value="<%=id_brand%>"><%=producer%>
        </option>
        <%}%>
    </select>
    <!-- Bottone che submitta la form filterForm con gli hidden input il cui value viene modificati con la setFormFilter() e i relativi listener -->
    <button type="submit" form="filterForm" class="btn ml-3 active2" id='filter_btn'>Filter</button>
</div>
<form name="filterForm" id="filterForm" method="post">
    <input type="hidden" name="controllerAction" value="home.Shop.showShop">
    <input type="hidden" name="filter" value="0">
    <input type="hidden" name="category" value="All">
    <input type="hidden" name="brand" value="All">
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
                    <div class="container">
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
        setButtonActive('<%=idBtnAttivo%>');
        setFilterForm("category_select_menu", "brand_select_menu", "<%=categoryFiltered%>", "<%=brandFiltered%>");
        setSelectedFilter("category_select_menu", "<%=categoryFiltered%>");
        setSelectedFilter("brand_select_menu", "<%=brandFiltered%>");
    })
</script>
</body>
</html>

