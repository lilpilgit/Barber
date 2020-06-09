<%@ page import="java.util.ArrayList" %>
<%@ page import="model.mo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = "Customer";
    String controller = "admin.Customers";
/*    Customer c = null;
    Boolean blockedStatus = c.getBlocked();*/

    /* Prendo l'ArrayList<Customer> di tutti gli impiegati */
    boolean areCustomers = false;
    ArrayList<User> customers = (ArrayList<User>) request.getAttribute("customers");
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
    String menuActiveLink = "Customers";

    /* Parametro per aggiungere la classe che lo mantiene attivo al bottone della pagina in cui si trova */
    String idBtnAttivo = "showCustomers";

%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.jsp" %>
<body>
<%@include file="../../templates/admin-sidebar.jsp"%>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <div class="row justify-content-center">
            <div class="col-auto">
                <%if (areCustomers) {%>
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">N°</th>
                        <th scope="col">Id</th>
                        <th scope="col">Email</th>
                        <th scope="col">Address</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int i = 1; /* contatore per il numero di impiegati */

                        for (User c : customers) {
                            boolean blockedStatus = c.isBlocked();
                    %>
                    <tr class="<%=blockedStatus ? "table-danger" : ""%>">
                        <th scope="row"><%=i++%>
                        </th>
                        <td><%=c.getId()%>
                        </td>
                        <td><%=c.getEmail()%>
                        </td>
                        <td><%=c.getAddress()%>
                        </td>
                        <td><%=c.getPhone()%>
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
                                <i class="fas <%=c.isBlocked() ? "fa-unlock-alt" : "fa-ban"%>"></i>
                            </button>
                            <button type="button" class="trashbutton" title="Delete"
                                    data-target="#alert<%=user%>"
                                    data-toggle="modal"
                                    onclick=setTmpId(<%=c.getId()%>,'tmpId')>
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
    window.addEventListener("load",() =>{

    })

</script>
</body>
</html>

