<%@ page import="model.mo.Structure" %>
<!--------------------------------------------------- Footer ---------------------------------------------------------->
<%
    /* Prendo l'oggetto struttura per conoscere le informazioni da mostrare nel footer */
    Structure structure = null;
    if(request.getAttribute("structure") != null){
        structure = (Structure) request.getAttribute("structure");
    }

    String[] splittedAddressStructure = null;
    int splittedAddressStructureLength = 0;

    String[] splittedTimeStart = null; /* orario di apertura */
    String[] splittedTimeEnd = null; /* orario di chiusura */

    boolean structurePresent = (structure != null);
    if (structurePresent) {
        /* Splitto sulla | il campo address della struttura per poterlo visualizzare in ogni campo del footer */
        String address = structure.getAddress();
        splittedAddressStructure = address.split("\\|");
        splittedAddressStructureLength = splittedAddressStructure.length;

        /* Splitto sui : per poter mostrare solo ore e minuti dell'ora di apertura */
        splittedTimeStart = structure.getOpeningTime().toString().split(":");
        /* Splitto sui : per poter mostrare solo ore e minuti dell'ora di chiusura */
        splittedTimeEnd = structure.getClosingTime().toString().split(":");
    }
%>
<%if (structurePresent) {%>
<footer>
    <div class="container ">
        <div class="row text-center py-4">
            <div class="col-md-4">
                <hr class="light">
                <h6>General Info</h6>
                <hr class="light">
                <p><%=structure.getPhone()%>
                </p>
                <p><%=structure.getName()%>
                </p>
                <p><%=splittedAddressStructure[0] + "," + splittedAddressStructure[1] + "," + splittedAddressStructure[2]%>
                </p>
                <p><%=splittedAddressStructure[4] + ((splittedAddressStructureLength == 6) ? ",N &deg; " + splittedAddressStructure[5] : ",") + splittedAddressStructure[3]%>
                </p>
            </div>
            <div class="col-md-4">
                <hr class="light">
                <h6>Opening Hours</h6>
                <hr class="light">
                <p>From Monday to Sunday: </p>
                <p><%=splittedTimeStart[0] + ":" + splittedTimeStart[1] + " - " + splittedTimeEnd[0] + ":" + splittedTimeEnd[1]%>
                </p>
                <img src="img/homepage/logo_jp.png" alt="logo_jp.png">
            </div>
            <div class="col-md-4 ">
                <hr class="light">
                <h6>Our Social</h6>
                <hr class="light">
                <div class="row text-center padding">
                    <div class="col-12">
                        <h2>Connect</h2>
                        <div>
                            <div class="col-12 social">
                                <a href="https://www.facebook.it"> <i class=" fab fa-facebook"
                                                                      aria-hidden="true"></i></a>
                                <a href="https://www.twitter.it"> <i class=" fab fa-twitter" aria-hidden="true"></i></a>
                                <a href="https://www.googleplus.it"> <i class=" fab fa-google-plus-g"
                                                                        aria-hidden="true"></i></a>
                                <a href="https://www.instagram.it"> <i class=" fab fa-instagram" aria-hidden="true"></i></a>
                                <a href="https://www.youtube.it"> <i class=" fab fa-youtube" aria-hidden="true"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container">
                <hr class="light-100">
                <h5> &copy; unitedbarbersalon.com</h5>
            </div>
        </div>
    </div>
</footer>
<%} else {%>
<footer>
    <div class="container ">
        <div class="row text-center py-4">
            <div class="col-md-4">
                <hr class="light">
                <h6>General Info</h6>
                <hr class="light">
                <p>555-555-555-555</p>
                <p>unitedbarbersalon@ubs.com</p>
                <p>Polo Scientifico Tecnologico</p>
                <p>Ferrara, Italy, 44121</p>
            </div>
            <div class="col-md-4">
                <hr class="light">
                <h6>Opening Hours</h6>
                <hr class="light">
                <p>From Monday to Sunday: </p>
                <p>9:00 - 18:00</p>
                <img src="img/homepage/logo_jp.png" alt="logo_jp.png">
            </div>
            <div class="col-md-4 ">
                <hr class="light">
                <h6>Our Social</h6>
                <hr class="light">
                <div class="row text-center padding">
                    <div class="col-12">
                        <h2>Connect</h2>
                        <div>
                            <div class="col-12 social">
                                <a href="https://www.facebook.it"> <i class=" fab fa-facebook"
                                                                      aria-hidden="true"></i></a>
                                <a href="https://www.twitter.it"> <i class=" fab fa-twitter" aria-hidden="true"></i></a>
                                <a href="https://www.googleplus.it"> <i class=" fab fa-google-plus-g"
                                                                        aria-hidden="true"></i></a>
                                <a href="https://www.instagram.it"> <i class=" fab fa-instagram" aria-hidden="true"></i></a>
                                <a href="https://www.youtube.it"> <i class=" fab fa-youtube" aria-hidden="true"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container">
                <hr class="light-100">
                <h5> &copy; unitedbarbersalon.com</h5>
            </div>
        </div>
    </div>
</footer>
<%}%>