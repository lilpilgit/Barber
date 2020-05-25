<%@ page import="model.mo.Employee" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = "Employee";
    String controller = "Staff";

    /* Prendo l'ArrayList<Employee> di tutti gli impiegati */
    boolean areEmployees = false;
    ArrayList<Employee> employees = (ArrayList<Employee>) request.getAttribute("employees");
    if (employees != null && employees.size() != 0)
        areEmployees = true;

    /* Prendo il parametro "result" che si occupa di indicarmi se l'inserimento del nuovo dipendente è andato a buon fine o meno*/
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

<jsp:include page="/templates/admin-sidebar.html"/>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3">
            <button onclick="setControllerAction('Staff.showFormNewEmployee')"
                    class="button-side" type="button" id='showFormNewEmployee'>
                <i class="fas fa-user-plus pr-3"></i>
                Add New Employee
            </button>
            <div class="collapse navbar-collapse" id="navbarsExample04">
                <ul class="navbar-nav mr-auto"></ul>
                <form class="form-inline my-2 my-md-0">
                    <input class="form-control" type="text" placeholder="Search">
                </form>
            </div>
        </nav>
        <%if (areEmployees) {%>
        <div class="row justify-content-center">
            <div class="col-auto">
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">ID</th>
                        <th scope="col">Surname</th>
                        <th scope="col">Name</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Email</th>
                        <th scope="col">Address</th>
                        <th scope="col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int i = 1; /* contatore per il numero di impiegati */

                        for (Employee e : employees) {
                    %>
                    <tr>
                        <th scope="row"><%=i++%>
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
                            <button type="button" class="tablebutton" style="color: #1ae2dd;" title="Modify"
                                    onclick="editEmployee(<%=e.getId()%>)"><i class="fas fa-pencil-alt"></i>
                            </button>
                            <button type="button" class="trashbutton" title="Delete"
                                    data-target="#alert<%=user%>"
                                    data-toggle="modal"
                                    onclick=setTmpId(<%=e.getId()%>)>
                                <i class="far fa-trash-alt"></i>
                            </button>
                        </td>
                    </tr>
                    <%}%>
                    </tbody>
                </table>
                <%} else {%>
                <h1>No employee to show.</h1>
                <%}%>
                <form method="post" id="action">
                    <input type="hidden" name="controllerAction" value="">
                    <input type="hidden" name="employeeID" value="">
                </form>
            </div>
        </div>
    </main>
</div>

<input type="hidden" id="tmpIdDel" value="">
<!--MODAL DI CONFERMA ELIMINAZIONE DIPENDENTE-->
<div class="modal fade" id="alert<%=user%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle" style="color: rgba(211,4,0,0.75)">You are removing
                    an employee...</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                You are attempting to permanently delete an employee.<br><br>Are you sure you want to continue?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>


                <!-- TODO AGGIORNARE CON FUNZIONE GENERICA deleteById contenuta in admin.js come con show-customers -->


                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="deleteEmployee(document.getElementById('tmpIdDel').value)">Delete
                    employee
                </button>
            </div>
        </div>
    </div>
</div>
<!--FINE MODAL DI CONFERMA ELIMINAZIONE DIPENDENTE-->


<script>

    window.addEventListener("load",() =>{
        /* TODO impostare il pulsante Staff su hover in modo da fare l'highlight*/

        <%if(resultPresent){%>
        showResult("<%=result%>", "Message:\n<%=applicationMessage%>");
        <%}%>
    })

</script>
</body>
</html>

