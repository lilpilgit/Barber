<%@page import="model.dao.DAOFactory" %>
<%@page import="model.dao.ProductDAO" %>
<%@page import="model.mo.Product" %>
<%@page import="java.util.ArrayList" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    DAOFactory df = DAOFactory.getDAOFactory(DAOFactory.MYSQLJDBCIMPL);
    if (df != null) {
        df.beginTransaction();
    } else {
        throw new RuntimeException("ERRORE NELL'IF DA AGGIUSTARE");
    }
    ProductDAO productDAO = df.getProductDAO();
    ArrayList<Product> products_to_show = null;
    ArrayList<String> all_category = productDAO.findAllCategories();
    ArrayList<String> all_producer = productDAO.findAllProducers();
    /*String category = null; BUUUUUUUUUUUUG ANCHE QUI*/
    String category = "All";
    String categoryParam = null; /*parameter to pass to findFilteredProducts*/
    /*String brand = null;BUUUUUUUUUUUUG ANCHE QUI*/
    String brand = "All";
    String brandParam = null; /*parameter to pass to findFilteredProducts*/

    /* Se avremo altri criteri di ricerca e filtraggio VA ASSOLUTAMENTE AUTOMATIZZATO CON UN CICLO FOR TUTTI GLI IF SOTTOSTANTI*/
    /*First I see if filters have been applied*/
    if (request.getParameter("filter") != null && Integer.parseInt(request.getParameter("filter")) == 1) {
        /*there are parameters aka modify query based on filters!!!*/
        /*      -------- 1) category ---------       */
        category = request.getParameter("category");
        if (category.equals("All")) {
            categoryParam = "%"; /* In SQL this character is a substitute for zero or more characters*/
        } else {
            categoryParam = category;
        }
        /*      -------- 2) brand ---------       */
        brand = request.getParameter("brand");
        if (brand.equals("All")) {
            brandParam = "%"; /* In SQL this character is a substitute for zero or more characters*/
        } else {
            brandParam = brand;
        }

        products_to_show = productDAO.findFilteredProducts(categoryParam, brandParam);

    } else if (request.getParameter("filter") == null || Integer.parseInt(request.getParameter("filter")) == 0) {
        /*first visit on page/parameters not sent or different from All  */
        products_to_show = productDAO.findAllProducts();
    }


    df.commitTransaction();
    df.closeTransaction();
%>
<!doctype html>
<html lang="en">
<jsp:include page="../../templates/head.html"/>
<body>

<jsp:include page="../../templates/header.jsp"/>

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
    <!-- Button for filter based on category and brand -->
    <a href="" class="btn button button1 ml-3 active2" id='filter_btn'>Filter</a>
</div>


<!--------------------------------------------- Product section ------------------------------------------------------->

<div class="container py-2">
    <div class="row padding">
        <%
            if (products_to_show.isEmpty()) {
                /*there is no product to display*/
        %>
        <div class="no-products-container">
            <h3 class="no-products-title">We are sorry...</h3>
            <div>
                <img class="no-products-thumb" src="../../img/shop/empty_warehouse.png" alt="No products present">
                <p>What you requested did not produce any results</p>
            </div>
        </div>

        <%
        } else {
            for (Product product : products_to_show) {
        %>
        <div class="col-md-4">
            <div class="card">
                <div class="text-center ">
                    <div class="tab-content-shop"><img src="../../img/products/<%=product.getPictureName()%>"
                                                  alt="<%=product.getPictureName()%>"></div>
                </div>
                <div class="card-body toBottom text-center">
                    <h4 class="card-title"><%=product.getName()%>
                    </h4>
                    <p class="card-text">&euro;<%=product.getPrice()%>
                    </p>
                    <div class="container">
                        <a href="product.jsp?id=<%=product.getId()%>" class="btn btn-dark">Show</a>
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
<jsp:include page="../../templates/footer.html"/>
<script type="text/javascript">
    window.onload = function afterPageLoad() {
        setButtonActive("showShop");
        setModalLogin();
        setUrlFiltered("category_select_menu", "brand_select_menu", "<%=category%>", "<%=brand%>", "filter_btn");
        setSelectedFilter("category_select_menu", "<%=category%>");
        setSelectedFilter("brand_select_menu", "<%=brand%>");
    }
</script>
</body>
</html>
