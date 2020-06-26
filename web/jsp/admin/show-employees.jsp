<%@ page import="model.mo.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
    String subject = "Employee";
    String controller = "admin.Staff";

    /* Prendo l'ArrayList<Employee> di tutti gli impiegati */
    boolean areEmployees = false;
    ArrayList<User> employees = (ArrayList<User>) request.getAttribute("employees");
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

    String searchedString = null;
    if (request.getAttribute("searchedString") != null) {
        searchedString = (String) request.getAttribute("searchedString");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Staff";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showEmployees";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>

<%@include file="/templates/admin-sidebar.jsp" %>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-3 sticky-top">
            <button onclick="setControllerAction('admin.Staff.showFormNewEmployee')"
                    class="button-side" type="button" id='showFormNewEmployee'>
                <i class="fas fa-user-plus pr-3"></i>
                Add New Employee
            </button>
            <div class="collapse navbar-collapse" id="navbarsExample04">
                <ul class="navbar-nav mr-auto"></ul>
                <form class="form-inline my-2 my-md-0" method="post">
                    <input class="form-control" type="text" name="searchString" id="searchString"
                           <%if (areEmployees) { if (searchedString != null) {%>placeholder="<%=searchedString%>"
                           <%} else {%>placeholder="Search"<%}}%>
                           required>
                    <input type="hidden" name="controllerAction" value="admin.Staff.searchStaff">
                    <button type="submit" class="btn btn-default"><i class="fas fa-search" style="color: #01c5fc"></i>
                    </button>
                </form>
            </div>
        </nav>
        <%if (searchedString != null) {%>

        <div class="row justify-content-center">
            <form method="post">
                <button type="submit" class="button6" title="Undo search"><i class="fas fa-globe-europe"></i>Show all
                </button>
                <input type="hidden" name="controllerAction" value="admin.Staff.showEmployees">
            </form>
        </div>
        <%}%>
        <%
            if (areEmployees) {%>

        <div class="row justify-content-center">
            <div class="col-auto">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered">
                        <thead class="thead-dark">
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

                            for (User e : employees) {
                        %>
                        <tr>
                            <th scope="row"><%=i++%>
                            </th>
                            <td><%=e.getId()%>
                            </td>
                            <td><%=e.getSurname()%>
                            </td>
                            <td><%=e.getName()%>
                            </td>
                            <td><%=e.getPhone()%>
                            </td>
                            <td><%=e.getEmail()%>
                            </td>
                            <%
                                String employeeAddressFormatted = e.getAddress().replace('|', ',');
                                employeeAddressFormatted = employeeAddressFormatted.substring(0, employeeAddressFormatted.length() - 1); /* tolgo l'ultila virgola */
                            %>
                            <td><%=employeeAddressFormatted%>
                            </td>
                            <td>
                                <button type="button" class="tablebutton" style="color: #1ae2dd;" title="Modify"
                                        onclick="editEmployee(<%=e.getId()%>)"><i class="fas fa-pencil-alt"></i>
                                </button>
                                <button type="button" class="trashbutton" title="Delete"
                                        data-target="#alert<%=subject%>"
                                        data-toggle="modal"
                                        onclick="setTmpId(<%=e.getId()%>,'tmpIdDel');">
                                    <i class="far fa-trash-alt"></i>
                                </button>
                            </td>
                        </tr>
                        <%}%>
                        </tbody>
                    </table>
                </div>
                <%} else {%>
                <div class="row justify-content-center">
                    <h1>No employee to show.</h1>
                </div>
                <%}%>
                <form method="post" id="action">
                    <input type="hidden" name="controllerAction" value="">
                    <input type="hidden" name="<%=subject%>ID" value="">
                </form>
            </div>
        </div>
    </main>
</div>

<input type="hidden" id="tmpIdDel" value="">
<!--MODAL DI CONFERMA ELIMINAZIONE DIPENDENTE-->
<div class="modal fade" id="alert<%=subject%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle" style="color: rgba(211,4,0,0.75)">You are removing
                    an <%=subject%>...</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                You are attempting to permanently delete an <%=subject%>.<br><br>Are you sure you want to continue?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="deleteById(document.getElementById('tmpId').value, '<%=subject%>','<%=controller%>')">
                    Delete
                    <%=subject%>
                </button>
            </div>
        </div>
    </div>
</div>
<!--FINE MODAL DI CONFERMA ELIMINAZIONE DIPENDENTE-->

<script>

    window.addEventListener("load", () => {
    })

</script>
</body>
</html>

