<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
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
    String menuActiveLink = "Home";

    /* Parametro per aggiungere la classe active2 al bottone della pagina in cui si trova */
    String idBtnAttivo = "showHome";

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
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</head>
<body>
<%@include file="/templates/admin-sidebar.html"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <div class="container-fluid">
            <h1>Welcome!</h1>
            <hr>
            <h2>Hello <strong><%=loggedUser.getName()%></strong></h2>
            <div class="row">
                <div class="form-group col-md-12" id="to-fill"></div>
            </div>
            <h5>Altre info o statistiche ...</h5>
            <hr>
        </div>
    </main>
</div>
<script>
    window.addEventListener("load",() =>{

    });
</script>
</body>
</html>

