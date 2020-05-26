<%@ page import="model.mo.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    /*    *//* Prendo l'ArrayList<Employee> di tutti gli impiegati *//*
    boolean areEmployees = false;
    ArrayList<Employee> employees = (ArrayList<Employee>) request.getAttribute("employees");
    if (employees != null && employees.size() != 0)
        areEmployees = true;*/

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

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Logistics";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showLogistics";
%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>

<%@include file="../../templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <span class="text-center"><h4>Customer's orders</h4></span>

        <div class="row justify-content-center">
            <div class="col-auto">
                <%--        <%if (areEmployees) {%>--%>
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">ID</th>
                        <th scope="col">Customer Email</th>
                        <th scope="col">Customer Name</th>
                        <th scope="col">Customer Phone</th>
                        <th scope="col">Status</th>
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
    window.addEventListener("load",() =>{

    })
</script>
</body>
</html>

