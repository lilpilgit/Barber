<head>
    <!--------------------------------------------- Meta tags --------------------------------------------------------->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>UBS-ADMIN | <%=menuActiveLink%></title>
    <link rel="apple-touch-icon" sizes="180x180" href="img/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="img/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="img/favicon/favicon-16x16.png">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script type="text/javascript" src="assets/js/jquery-3.5.1.js"></script>
    <!--Our bootstrap.bundle.js and bootstrap.bundle.min.js include Popper, but not jQuery.-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" data-auto-replace-svg="nest"></script>
    <link href="https://use.fontawesome.com/releases/v5.13.0/css/all.css" rel="stylesheet">
    <!-- MDB core JavaScript -->
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script>
    <link href="assets/css/style-admin.css" rel="stylesheet">
    <script type="text/javascript" src="assets/js/admin.js"></script>
</head>
<script>
    window.addEventListener("load", () => {
        <%if(idBtnAttivo != null) {%>
        setButtonActiveSidebar('<%=idBtnAttivo%>');
        <%}%>
        setCurrentDate('currentDate');
        <%if(resultPresent){%>
        showResult('<%=result%>', '<%=applicationMessage%>');
        <%}%>
    });
</script>