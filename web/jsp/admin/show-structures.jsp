<%@ page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    /*    *//* Prendo l'ArrayList<Employee> di tutti gli impiegati *//*
    boolean areEmployees = false;
    ArrayList<Employee> employees = (ArrayList<Employee>) request.getAttribute("employees");
    if (employees != null && employees.size() != 0)
        areEmployees = true;*/
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
    <link href="${pageContext.request.contextPath}/assets/css/style-admin.css" rel="stylesheet">
    <!--JAVASCRIPT PER LA PAGINA ADMIN 'VUOTA'-->
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</head>
<body>

<jsp:include page="../../templates/admin-sidebar.html"/>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <%--        <%if (areEmployees) {%>--%>
        <div class="row justify-content-center">
            <div class="col-auto">
                <table class="table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">ID</th>
                        <th scope="col">Name</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Opening Time</th>
                        <th scope="col">Closing TIme</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <%--            <tbody>
                                <%
                                    int i = 1; /* contatore per il numero di impiegati */

                                    for (Employee e : employees) {
                                %>
                                <tr>
                                    <th scope="row"><%=i%>
                                    </th>
                                    <td><%=e.getId()%>
                                    </td>
                                    <td><%=e.getUser().getSurname()%>
                                    </td>
                                    <td><%=e.getUser().getName()%>
                                    </td>
                                    <td><%=e.getUser().getPhone()%>
                                    </td>
                                    <td><%=e.getUser().getEmail()%>
                                    </td>
                                    <td><%=e.getUser().getAddress()%>
                                    </td>
                                    <td>
                                        <button type="button" class="tablebutton" style="color: #1ae2dd;"><i class="fas fa-pencil-alt"></i>
                                        </button>
                                        <button type="button" class="tablebutton" style="color: black;"><i class="far fa-trash-alt"></i>
                                        </button>
                                    </td>
                                </tr>
                                <%}%>
                                </tbody>--%>
                </table>
            </div>
        </div>
        <%--        <%}else{%>
                <h1>Non ci sono dipendenti mi disp...</h1>
                <%}%>--%>
    </main>
</div>


<script>
    function onLoadFunctionalities() {
        /* TODO impostare il pulsante Structure su hover in modo da fare l'highlight*/
    }

    window.addEventListener('load', onLoadFunctionalities);
</script>
</body>
</html>
