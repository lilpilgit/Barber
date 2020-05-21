<%@page import="model.dao.BookingDAO" %>
<%@page import="model.dao.DAOFactory" %>
<%@page import="model.mo.Booking" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.Month" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    DAOFactory df = DAOFactory.getDAOFactory(DAOFactory.MYSQLJDBCIMPL);
    if (df != null) {
        df.beginTransaction();
    } else {
        throw new RuntimeException("ERRORE NELL'IF DA AGGIUSTARE");
    }
    BookingDAO bookingDAO = df.getBookingDao();
    LocalDate prova = LocalDate.of(2020, Month.MAY, 20);
    ArrayList<Booking> bookings = bookingDAO.findBookingsByDate(prova);
    List<BookingPair> bookingPairs = new ArrayList();
    for (Booking booking : bookings) {
        bookingPairs.add(new BookingPair(booking.getHourStart().toString(), booking.getHourEnd().toString()));
    }
    int durata = 20;

    /* dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SS'Z'");
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));*/

    /*-------------------------------------------------------------------------*/
/*    DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
    outputFormat.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));

    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");*/
    /*-------------------------------------------------------------------------*/

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

<%--<%
    for (Booking booking : bookings) {

%>
&lt;%&ndash;
<h1><%=booking.getHourStart().atZone(ZoneId.of("Europe/Rome")).toLocalTime()%>
&ndash;%&gt;
<h1><%=booking.getHourStart()%>
</h1><br>
<%}%><br><br><br><br><br><br>--%>


<br><br>
<div id="div-prova"></div>


<jsp:include page="templates/footer.html"/>
<script type="text/javascript">

    window.onload = function afterPageLoad() {
        setButtonActive("booking");
        setModalLogin();
        /*        document.getElementById("date_picker").addEventListener("change",()=>{
                    getSelectedDateOfBooking("selected_date");
                });*/

        /*get local time zone in javascript*/
        console.log(Intl.DateTimeFormat().resolvedOptions().timeZone);
        /*----------------------------------*/
        let div = document.getElementById("div-prova");
        let hour;
        let minute;
        /*





        <%
                                                        for (int i=0; i < bookings.size(); i++){%>

        div.appendChild(document.createTextNode((new Date('


        <%=bookings.get(i).getHourStart()%>').getHours()).toString()));
        div.appendChild(document.createElement("br"));



        <%}%>*/

        <%for (int i=0; i < bookingPairs.size(); i++) {%>
        div.appendChild(document.createTextNode(' | ' + new Date('<%=bookingPairs.get(i).getStart()%>').getHours() + ':' + ((new Date('<%=bookingPairs.get(i).getStart()%>').getMinutes()) < 10 ? '0' : '') + (new Date('<%=bookingPairs.get(i).getStart()%>')).getMinutes()));
        div.appendChild(document.createTextNode(' | ' + new Date('<%=bookingPairs.get(i).getEnd()%>').getHours().toString() + ':' + new Date('<%=bookingPairs.get(i).getEnd()%>').getMinutes().toString()));
        div.appendChild(document.createElement("br"));
        <%}%>

        /*


        <%for (int i=0; i < bookingPairs.size(); i++) {%>
        div.appendChild(document.createTextNode(' | ' + new Date('


        <%=bookingPairs.get(i).getStart()%>').toTimeString()));
        div.appendChild(document.createTextNode(' | ' + new Date('


        <%=bookingPairs.get(i).getEnd()%>').toTimeString()));
        div.appendChild(document.createElement("br"));



        <%}%>*/

        /*sumTime();*/
    }
</script>
</body>
</html>
