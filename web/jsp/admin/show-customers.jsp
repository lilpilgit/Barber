<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.Customer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = "Customer";
    String controller = "Customers";
/*    Customer c = null;
    Boolean blockedStatus = c.getBlocked();*/

    /* Prendo l'ArrayList<Customer> di tutti gli impiegati */
    boolean areCustomers = false;
    ArrayList<Customer> customers = (ArrayList<Customer>) request.getAttribute("customers");
    if (customers != null && customers.size() != 0)
        areCustomers = true;

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
    <!--JAVASCRIPT PER LA PAGINA ADMIN 'VUOTA'-->
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</head>
<body>

<jsp:include page="../../templates/admin-sidebar.html"/>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <div class="row justify-content-center">
            <div class="col-auto">
                <%if (areCustomers) {%>
                <table class="table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">Id</th>
                        <th scope="col">Email</th>
                        <th scope="col">Address</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Num Booked</th>
                        <th scope="col">Num Order</th>
                        <th scope="col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int i = 1; /* contatore per il numero di impiegati */

                        for (Customer c : customers) {
                            boolean blockedStatus = c.getBlocked();
                    %>
                    <tr class="<%=blockedStatus ? "table-danger" : ""%>">
                        <th scope="row"><%=i++%>
                        </th>
                        <td><%=c.getId()%>
                        </td>
                        <td><%=c.getUser().getEmail()%>
                        </td>
                        <td><%=c.getUser().getAddress()%>
                        </td>
                        <td><%=c.getUser().getPhone()%>
                        </td>
                        <td><%=c.getNumBookedReservations()%>
                        </td>
                        <td><%=c.getNumOrderedProduct()%>
                        </td>
                        <td>
                            <button type="button" class="tablebutton" style="color: <%=blockedStatus ? "green" : "red"%>;"
                                    title="<%=blockedStatus ? "Unblock" : "Block"%>"
                                    data-target="#alertBanCust"
                                    data-toggle="modal"
                                    onclick=<% if(blockedStatus) { %>
                                            "unBlockById(<%=c.getId()%>, '<%=user%>','<%=controller%>')">
                            <% } else { %>
                                            "blockById(<%=c.getId()%>, '<%=user%>','<%=controller%>')">
                            <%}%>
                                <i class="fas <%=c.getBlocked() ? "fa-unlock-alt" : "fa-ban"%>"></i>
                            </button>
                            <button type="button" class="trashbutton" title="Delete"
                                    data-target="#alert<%=user%>"
                                    data-toggle="modal"
                                    onclick=setTmpId(<%=c.getId()%>)>
                                <i class="far fa-trash-alt"></i>
                            </button>
                        </td>
                    </tr>
                    <%}%>
                    </tbody>
                </table>
                <%} else {%>
                <h1>There are no customers :(</h1>
                <%}%>
                <form method="post" id="action">
                    <input type="hidden" name="controllerAction" value="">
                    <input type="hidden" name="<%=user%>ID" value="">
                </form>
            </div>
        </div>
    </main>
</div>

<input type="hidden" id="tmpId" value="">

<!--MODAL DI CONFERMA ELIMINAZIONE CLIENTE-->
<div class="modal fade" id="alert<%=user%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="DeleteModal" style="color: rgba(211,4,0,0.75)">You are removing
                    a <%=user%>...</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                You are attempting to permanently delete a <%=user%>.<br><br>Are you sure you want to continue?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" id="ultimateBtnDel" class="btn btn-primary"
                        style="background-color: rgba(255,5,3,0.66)"
                        onclick="deleteById(document.getElementById('tmpId').value, '<%=user%>','<%=controller%>')">Delete <%=user%>
                </button>
            </div>
        </div>
    </div>
</div>
<!--FINE MODAL DI CONFERMA ELIMINAZIONE CLIENTE-->

<script>
    function onLoadFunctionalities() {
        /* TODO impostare il pulsante Customers su hover in modo da fare l'highlight*/

        <%if(resultPresent){%>
        showResult("<%=result%>", "Message:\n<%=applicationMessage%>");
        <%}%>
    }

    function setTmpId(id) {
        document.getElementById("tmpId").value = id;
        console.log("ID SETTATO TMP: " + document.getElementById("tmpId").value);
    }

    window.addEventListener('load', onLoadFunctionalities);
</script>
</body>
</html>

