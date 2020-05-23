<%@page import="model.dao.DAOFactory" %>
<%@page import="model.dao.ProductDAO" %>
<%@page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    DAOFactory df = DAOFactory.getDAOFactory(DAOFactory.MYSQLJDBCIMPL);
    if (df != null) {
        df.beginTransaction();
    } else {
        throw new RuntimeException("ERRORE NELL'IF DA AGGIUSTARE");
    }
    ProductDAO productDAO = df.getProductDAO();

    ArrayList<Product> products = productDAO.findShowcaseProduct();

    df.commitTransaction();
    df.closeTransaction();
%>
<!doctype html>
<html lang="en">
<head>
    <!--------------------------------------------- Meta tags --------------------------------------------------------->
    <meta charset="utf-8">

    <title>UBS United Barber Salon</title>

    <link rel="apple-touch-icon" sizes="180x180" href="img/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="img/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="img/favicon/favicon-16x16.png">

    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script type="text/javascript" src="assets/js/jquery-3.5.1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" data-auto-replace-svg="nest"></script>
    <link href="assets/css/style.css" rel="stylesheet">
    <script type="text/javascript" src="assets/js/main.js"></script>

</head>
<body>

<jsp:include page="templates/header.html"/>

<!-------------------------------------------------- Image Slider ----------------------------------------------------->


<div class="container pt-5 pb-5">
    <div id="carousel" class="carousel slide container carousel-height px-4" data-ride="carousel">
        <ol class="carousel-indicators">
            <li data-target="#carousel" data-slide-to="0" class="active"></li>
            <li data-target="#carousel" data-slide-to="1"></li>
            <li data-target="#carousel" data-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="img/homepage/img1.jpg" class="d-block w-100 carousel-height" alt="Immagine_1">
            </div>
            <div class="carousel-item">
                <img src="img/homepage/img2.webp" class="d-block w-100 carousel-height" alt="Immagine_2">
            </div>
            <div class="carousel-item ">
                <img src="img/homepage/img3.jpg" class="d-block w-100 carousel-height" alt="Immagine_3">
            </div>
        </div>
        <a class="carousel-control-prev" href="#carousel" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#carousel" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
</div>

<!--------------------------------------------- Fast Cheap Reliable --------------------------------------------------->

<div class="container">
    <div class="row text-center">
        <div class="col-xs-12 col-sm-6 col-md-4">
            <img class="fast" src="https://img.icons8.com/ios-filled/64/000000/fast-track.png" alt="Fast"/>
            <h3>FAST</h3>
            <p>Guaranteed service within 30 minutes.</p>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-4">
            <img class="cheap" src="https://img.icons8.com/pastel-glyph/64/000000/dollar-coin.png" alt="cheap"/>
            <h3>CHEAP</h3>
            <p>We have the best prices on the market.</p>
        </div>
        <div class="col-sm-12 col-md-4">
            <img class="love" src="https://img.icons8.com/ios-filled/50/000000/trust.png" alt="Trusted"/>
            <h3>RELIABLE</h3>
            <p>For over 30 years, we do everything with love.</p>
        </div>
    </div>
</div>


<!------------------------------------------ Product Window ----------------------------------------------------------->

<div class="container-fluid ">
    <div class="row welcome text-center">
        <div class="col-12">
            <h1 class="display-4">Our Best Product!</h1>
        </div>
        <hr>
    </div>
</div>

<div class="container py-2">
    <div class="row padding">
        <%
            for (Product product : products) {%>
        <div class="col-md-4">
            <div class="card">
                <div class="text-center">
                    <div class="tab-content-shop"><img src="img/products/<%=product.getPictureName()%>"
                         alt="<%=product.getPictureName()%>"></div>
                </div>
                <div class="card-body toBottom text-center">
                    <h4 class="card-title"><%=product.getName()%>
                    </h4>
                    <p class="card-text">&euro;<%=product.getPrice()%>
                    </p>
                    <div class="container">
                    <a href="jsp/common/product.jsp?id=<%=product.getId()%>" class="btn btn-outline-secondary">See Product</a>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
    </div>
</div>

<!----------------------------------------------- Link To shop -------------------------------------------------------->

<div class="container text-center">
    <a class="button button2 text-center" href="jsp/common/shop.jsp">See more...</a>
</div>
<div class="container-fluid">
    <div class="row welcome text-center">
        <hr>
    </div>
</div>


<!-------------------------------------------------- Our Team --------------------------------------------------------->
<div class="container-fluid py-4 ">
    <div class="row text-center">
        <div class="col-12">
            <h1 class="display-4">We Are A Big Family</h1>
            <div class="container pb-4 ">
                <img class="img-thumbnail myrounded" src="img/homepage/team.jpg" height="500" alt="Team.jpg">
            </div>



           <%-- <div class="container py-4">
                <button type="button" class="button button2" data-toggle="collapse" data-target="#demo">Check it!
                </button>
            </div>
            <div id="demo" class="collapse">

                <!---------------------------------- Show Team ------------------------------------->

               &lt;%&ndash; <div class="container py-3 ml-auto">
                    <div class="fixit">
                        <img class="rounded-circle profile-img" src="img/homepage/Long-Beard-Styles.jpg"
                             alt="BarbaLungaStyles.jpg">
                        <h2>Mario Rossi</h2>
                        <p>Our beard expert will amaze you with many ideas for new styles</p>
                        <br>
                    </div>
                </div>&ndash;%&gt;
--%>

            </div>
        </div>
    </div>
</div>
<jsp:include page="templates/footer.html"/>
<script>
    window.onload = function afterPageLoad() {
        setButtonActive("index");
        setModalLogin();
    }
</script>
</body>
</html>
