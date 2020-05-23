<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%

%>
<!doctype html>
<html lang="en">
<jsp:include page="../../templates/head.html"/>
<body>

<jsp:include page="../../templates/header.html"/>


<br><br>
<div id="div-prova">

    <p>BANANE</p>
</div>


<jsp:include page="../../templates/footer.html"/>

<script type="text/javascript">
    window.onload = function afterPageLoad() {
        setButtonActive("showBooking");
        setModalLogin();
    }
</script>
<%--<script type="text/javascript">

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
</script>--%>

</body>
</html>
