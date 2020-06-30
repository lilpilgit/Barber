<%@ page import="functions.StaticFunc" %>
<%@ page import="model.mo.StatisticsEarnings" %>
<%@ page import="model.mo.User" %>
<%@ page import="java.sql.Time" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
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

    /* Prendo l'oggetto admin con tutte le informazioni necessarie */
    User admin = null;
    if (request.getAttribute("admin") != null) {
        admin = (User) request.getAttribute("admin");
    }

    /* Prendo il parametro "statisticsEarnings" per conoscere tutte le diverse statistiche calcolate */
    StatisticsEarnings statisticsEarnings = null;
    if (request.getAttribute("statisticsEarnings") != null) {
        statisticsEarnings = (StatisticsEarnings) request.getAttribute("statisticsEarnings");
    }

    /* Prendo il parametro "totalAppointmentGroupByHourStart" per conoscere il numero di appuntamenti totali per ogni fascia oraria in un determinato anno */
    TreeMap<Time, Integer> totalAppointmentGroupByHourStart = null;
    if (request.getAttribute("totalAppointmentGroupByHourStart") != null) {
        totalAppointmentGroupByHourStart = (TreeMap<Time, Integer>) request.getAttribute("totalAppointmentGroupByHourStart");
    }

    /* Parametro per settare di volta in volta dove ci si trova nel title */
    String menuActiveLink = "Statistics";

    /* Parametro per aggiungere la classe "button-side-active" al bottone della pagina in cui si trova */
    String idBtnAttivo = "showStatistics";

%>
<!doctype html>
<html lang="en">
<%@include file="/templates/admin-head.inc" %>
<body>
<%@include file="/templates/admin-sidebar.inc" %>
<div class="page-wrapper chiller-theme toggled">
    <!--Main content of the page-->
    <main class="page-content">
        <div class="container-fluid">
            <h1>Welcome!</h1>
            <hr>
            <h2>Hello <strong><%=admin.getName()%>
            </strong></h2>
            <div class="row">
                <div class="form-group col-md-12" id="to-fill"></div>
            </div>
            <h5>Some statistics about your business ...</h5>
            <hr>
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <canvas id="barChart"></canvas>
                </div>
            </div>
            <br><br>
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <canvas id="pieChart"></canvas>
                </div>
            </div>
        </div>
    </main>
</div>
<script>
    window.addEventListener("load", () => {
        drawChartEarnings(<%=StaticFunc.round(statisticsEarnings.getTotEarningsWithoutDiscount(),2)%>, <%=StaticFunc.round(statisticsEarnings.getTotEarningsWithDiscount(),2)%>, <%=StaticFunc.round(statisticsEarnings.getLostGain(),2)%>);
        let labelsHourStart = [
            <%
                String[] splittedTime;
                for (Map.Entry<Time, Integer> entry : totalAppointmentGroupByHourStart.entrySet()) {
                    splittedTime = entry.getKey().toString().split(":");
                %>
            "<%=splittedTime[0] + ":" + splittedTime[1]%>",
            <%}%>
        ];

        let dataHourStart = [
            <%
                for (Map.Entry<Time, Integer> entry : totalAppointmentGroupByHourStart.entrySet()) {%>
            <%=entry.getValue()%>,
            <%}%>
        ];

        let backgroundColor = [];
        for (let i = 0; i < <%=totalAppointmentGroupByHourStart.size()%>;i++){
            backgroundColor.push(getRandomColor());
        }

        drawChartAppointments(labelsHourStart,dataHourStart,backgroundColor);

    });

</script>
</body>
</html>

