<!-------------------------------------------------- Navigation in Header ------------------------------------------------------->
<header>
    <div class="container-fluid sticky-top">
        <nav class="navbar navbar-expand-md navbar-light ">
            <div class="container">
                <span class="navbar-brand"><img src="img/homepage/logo_jp2.png" alt="United_Barber_Salon.png"></span>

                <div class="navbar-nav ml-auto">
                    <button class="btn btnheader" type="button" id='showHome'
                            onclick=setNavFormHome('Home.view')>
                        Home
                    </button>
                    <% if (loggedOn) {%>
                    <button class="btn btnheader" type="button" id='showBook'
                            onclick=setNavFormHome('Home.showBook')>
                        Book!
                    </button>
                    <%}%>
                    <button class="btn btnheader" type="button" id='showShop'
                            onclick=setNavFormHome('Home.showShop')>
                        Shop
                    </button>
                    <button class="btn btnheader" type="button" id='showContact'
                            onclick=setNavFormHome('Home.showContactForm')>
                        Contact
                    </button>
                    <% if (loggedOn) {%>
                    <button class="logged" type="button" id='showCart' title="Cart"
                            onclick=setNavFormHome('Home.showCart')>
                        <i class="fas fa-shopping-basket"></i>
                    </button>
                    <button class="logged" type="button" id='showWishlist' title="Wishlist"
                            onclick=setNavFormHome('Home.showWishlist')>
                        <i class="fas fa-star"></i>
                    </button>
                    <div class="dropdown">
                        <button class="logged dropdown-toggle" type="button" id='showOptions'
                                data-target=""
                                data-toggle="dropdown">
                            <i class="fas fa-user"></i>
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu text-center">
                            <li>
                                <button class="btn btn-default gold font-weight-light" type="button"
                                        id='showBookings' onclick=setNavFormHome('Home.showBookings')>
                                    Bookings
                                </button>
                            </li>
                            <li>
                                <button class="btn btn-default gold font-weight-light" type="button"
                                        id='showOrders'onclick=setNavFormHome('Home.showOrders')>
                                    Orders
                                </button>
                            </li>
                            <li>
                                <button class="btn btn-default gold font-weight-light" type="button"
                                        id='showProfile' onclick=setNavFormHome('Home.showProfile')>
                                    Edit profile
                                </button>
                            </li>
                            <li>
                                <button class="btn btn-default gold font-weight-light" type="button" id='logoutID'
                                        onclick=setNavFormHome('Home.logout')>Logout
                                </button>
                            </li>
                        </ul>
                    </div>
                    <%} else {%>
                    <button class="logged" type="button" id='showEdit'
                            data-target="#loginModal"
                            data-toggle="modal">
                        <i class="fas fa-user"></i>
                    </button>
                    <%}%>


                    <!-- MULTI LANGUAGE -->

                    <!--                        <div class="btn-group">
                                                <button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
                                                    <span class="flag-icon flag-icon-gb"></span> ENG
                                                </button>
                                                <div class="dropdown-menu">
                                                    <a class="dropdown-item" href="#"><span class="flag-icon flag-icon-it"></span> ITA</a>
                                                </div>
                                            </div>-->

                </div>
            </div>
        </nav>
    </div>
</header>
<script>
    function setNavFormHome(value) {
        let navForm = document.getElementById('navForm');
        navForm.elements['controllerAction'].value = value;
        navForm.submit();
    }
</script>
<!--form per decidere quale controller e method chiamare quando vengono cliccati i pulsanti della navbar-->
<form name="navForm" id="navForm" method="post">
    <input type="hidden" name="controllerAction" value="">
</form>

<!--PRESO DA https://mdbootstrap.com/docs/jquery/modals/forms/-->

<!--Modal: Login / Register Form-->
<div class="modal fade " id="loginModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-width modal-dialog-centered cascading-modal" role="document">
        <!--Content-->
        <div class="modal-content">

            <!--Modal cascading tabs-->
            <div class="modal-c-tabs">

                <!-- Nav tabs -->
                <ul class="nav nav-tabs md-tabs tabs-2 light-blue darken-3" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" data-toggle="tab" href="#panel7" role="tab">
                            <i class="fas fa-user mr-1"></i>
                            Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#panel8" role="tab">
                            <i class="fas fa-user-plus mr-1"></i>
                            Register</a>
                    </li>
                </ul>

                <!-- Tab panels -->
                <div class="tab-content">
                    <!--Panel 7-->
                    <div class="tab-pane fade in show active" id="panel7" role="tabpanel">

                        <!--Body-->

                        <div class="modal-body modal-background mb-1">
                            <form name="logonForm" id="logonForm" method="post">
                                <div class="text-center pt-3">
                                    <h3>Log in to buy and book!</h3>
                                </div>
                                <div class="row text-center justify-content-center pt-4">
                                    <div class="md-form form-sm col-5">
                                        <input type="email" id="Email"
                                               name="email" maxlength="50" required
                                               class="form-control form-control-sm validate"
                                               oninput="this.value=this.value.toLowerCase();">
                                        <label data-error="wrong" data-success="right" for="Email">
                                            <i class="fas fa-envelope prefix"></i> Your email </label>
                                    </div>
                                    <div class="md-form form-sm col-5 mb-1">
                                        <input type="password" id="Password"
                                               name="password" maxlength="255" required
                                               class="form-control form-control-sm validate">
                                        <label data-error="wrong" data-success="right" for="Password">
                                            <i class="fas fa-lock prefix"></i> Insert password </label>
                                    </div>
                                </div>
                                <div class="text-center my-2">
                                    <button type="submit" form="logonForm" class="btn btn-info">Log in<i
                                            class="fas fa-sign-in-alt ml-1"></i></button>
                                </div>
                                <input type="hidden" name="controllerAction" value="Home.logon"/>
                            </form>
                        </div>

                        <!--Footer-->
                        <div class="modal-footer">
                            <div class="options text-center text-md-right mt-1">
                                <p>Forgot <a href="#" class="blue-text"> Password?</a></p>
                            </div>
                            <button type="button" class="btn btn-outline-info waves-effect ml-auto"
                                    data-dismiss="modal">Close
                            </button>
                        </div>
                    </div>
                    <!--/.Panel 7-->

                    <!--Panel 8-->
                    <div class="tab-pane fade" id="panel8" role="tabpanel">
                        <form name="registerForm" id="registerForm" method="post">

                            <!--Body-->
                            <div class="modal-body">

                                <div class="row justify-content-center">
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="text" class="form-control" name="name" id="Name"
                                               required oninput="this.value=this.value.toUpperCase();">
                                        <label data-error="wrong" data-success="right" for="Name">
                                            <i class="fas fa-user "></i> Name </label>
                                    </div>
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="text" class="form-control" name="surname" id="Surname"
                                               required oninput="this.value=this.value.toUpperCase();">
                                        <label data-error="wrong" data-success="right" for="Surname">
                                            <i class="fas fa-user "></i> Surname </label>
                                    </div>
                                </div>

                                <div class="row justify-content-center">
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="password" id="modalLRInput13" name="password" required
                                               class="form-control form-control-sm validate">
                                        <label data-error="wrong" data-success="right" for="modalLRInput13">
                                            <i class="fas fa-lock prefix"></i> Password</label>
                                    </div>
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="password" id="modalLRInput14" required
                                               class="form-control form-control-sm validate">
                                        <label data-error="wrong" data-success="right" for="modalLRInput14">
                                            <i class="fas fa-lock prefix"></i> Repeat password </label>
                                    </div>
                                </div>

                                <div class="row justify-content-center">
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="email" id="modalLRInput12" name="email"
                                               class="form-control form-control-sm validate " required
                                               oninput="this.value=this.value.toLowerCase();">
                                        <label data-error="wrong" data-success="right" for="modalLRInput12">
                                            <i class="fas fa-envelope prefix"></i> Email </label>
                                    </div>
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="tel" id="modalLRInput15" name="phone"
                                               class="form-control form-control-sm validate" required>
                                        <label data-error="wrong" data-success="right" for="modalLRInput15">
                                            <i class="fas fa-phone"></i> Phone number </label>
                                    </div>
                                </div>

                                <div class="row justify-content-center">
                                    <div class="md-form form-sm col-5 text-center">
                                        <select class="custom-select" name="state" id="State" required>
                                            <option selected disabled value="">Choose...</option>
                                            <option>ITALY</option>
                                        </select>
                                        <label for="State"><i class="fas fa-flag"></i> State</label>
                                    </div>
                                    <div class="md-form form-sm col-5 text-center">
                                        <select class="custom-select" name="region" id="Region" required>
                                            <option selected disabled value="">Choose...</option>
                                            <option>ABRUZZO</option>
                                            <option>BASILICATA</option>
                                            <option>CALABRIA</option>
                                            <option>EMILIA-ROMAGNA</option>
                                            <option>FRIULI-VENEZIA-GIULIA</option>
                                            <option>LAZIO</option>
                                            <option>LIGURIA</option>
                                            <option>LOMBARDIA</option>
                                            <option>MARCHE</option>
                                            <option>MOLISE</option>
                                            <option>PIEMONTE</option>
                                            <option>PUGLIA</option>
                                            <option>SARDEGNA</option>
                                            <option>SICILIA</option>
                                            <option>TOSCANA</option>
                                            <option>TRENTINO-ALTO-ADIGE</option>
                                            <option>UMBRIA</option>
                                            <option>VALLE D'AOSTA</option>
                                            <option>VENETO</option>
                                        </select>
                                        <label for="Region"><i class="fas fa-map-marker-alt"></i> Region </label>
                                    </div>
                                </div>

                                <div class="row justify-content-center">
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="text" class="form-control" name="city" id="City" required
                                               oninput="this.value=this.value.toUpperCase();">
                                        <label data-error="wrong" data-success="right" for="modalLRInput12">
                                            <i class="fas fa-map-marker-alt"></i> City
                                        </label>
                                    </div>
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="number" class="form-control" name="cap" id="Cap" required min="0"
                                               oninput="this.value=this.value.toUpperCase();">
                                        <label data-error="wrong" data-success="right" for="modalLRInput15">
                                            <i class="fas fa-map-marker-alt"></i> Postal Code
                                        </label>
                                    </div>
                                </div>
                                <div class="row justify-content-center">
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="text" class="form-control" name="street" id="Street" required
                                               oninput="this.value=this.value.toUpperCase();">
                                        <label data-error="wrong" data-success="right" for="modalLRInput12">
                                            <i class="fas fa-map-marker-alt"></i> Street
                                        </label>
                                    </div>
                                    <div class="md-form form-sm col-5 text-center">
                                        <input type="number" class="form-control" name="house_number" id="House-number"
                                               min="0">
                                        <label data-error="wrong" data-success="right" for="modalLRInput15">
                                            <i class="fas fa-map-marker-alt"></i> House Number
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <!--Footer-->
                            <div class="row justify-content-center">
                                <div class="col-3 text-center">
                                    <button type="button" class="btn btn-outline-info"
                                            data-dismiss="modal">Close
                                    </button>
                                </div>
                                <div class="col-3 text-center">
                                    <button type="submit" form="registerForm" class="btn btn-info">Sign up
                                        <i class="fas fa-sign-in-alt ml-1"></i>
                                    </button>
                                </div>
                            </div>
                            <input type="hidden" name="controllerAction" value="Home.register"/>
                        </form>
                    </div>
                </div>
                <!--/.Panel 8-->
            </div>

        </div>
        <!--/.Content-->
    </div>
</div>
</div>



