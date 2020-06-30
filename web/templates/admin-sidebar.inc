<div class="page-wrapper chiller-theme toggled">
    <nav id="sidebar" class="sidebar-wrapper">
        <div class="sidebar-content">
            <div class="sidebar-header">
                <div class="user-pic">
                    <img src="img/admin/business-man-3.svg" alt="User picture"/>
                    <button onclick="setControllerAction('admin.Admin.showProfile')" title="Edit your profile"
                            class="button-side" type="button" id='showAdmin'>
                        <i class="fas fa-user-edit"></i>
                    </button>
                </div>
                <div class="user-info pt-1">
                    <span class="user-name"><strong>Administrator</strong></span>
                </div>
            </div>

            <!------------------------------------------ sidebar -------------------------------------------------->

            <div class="sidebar-menu">
                <ul>
                    <li class="header-menu">
                        <span>General</span>
                    </li>
                    <li>
                        <button onclick="setControllerAction('admin.Staff.showEmployees')"
                                class="button-side" type="button" id='showEmployees'>
                            <i class="fa fa-gem pr-3"></i>
                            Staff
                        </button>
                    </li>
                    <li>
                        <button onclick="setControllerAction('admin.Customers.showCustomers')"
                                class="button-side" type="button" id='showCustomers'>
                            <i class="fa fa-users pr-3"></i>
                            Customers
                        </button>
                    </li>
                    <li>
                        <button onclick="setControllerAction('admin.Products.showProducts')"
                                class="button-side" type="button" id='showProducts'>
                            <i class="fa fa-shopping-cart pr-3"></i>
                            Products
                        </button>
                    </li>
                    <li>
                        <button onclick="setControllerAction('admin.Bookings.showBookings')"
                                class="button-side" type="button" id='showBookings'>
                            <i class="far fa-calendar pr-3"></i>
                            Bookings
                        </button>
                    </li>
                    <li>
                        <button onclick="setControllerAction('admin.Logistics.showLogistics')"
                                class="button-side" type="button" id='showLogistics'>
                            <i class="fas fa-shipping-fast pr-3"></i>
                            Logistics
                        </button>
                    </li>
                    <li>
                        <button onclick="setControllerAction('admin.Structure.showStructure')"
                                class="button-side" type="button" id='showStructure'>
                            <i class="fas fa-building pr-3"></i>
                            Structure
                        </button>
                    </li>
                    <li>
                        <button onclick="setControllerAction('admin.Welcome.welcome')"
                                class="button-side" type="button" id='showStatistics'>
                            <i class="fas fa-chart-line pr-3"></i>
                            Statistics
                        </button>
                    </li>
                </ul>
            </div>
            <!-- sidebar-menu  -->
        </div>
        <!-- sidebar-content  -->
        <div class="sidebar-footer justify-content-center">
            <button class="button-backtohome" type="submit" form="goBackToHome"
                    class="button-side-footer" id="toHome" title="Go back to home">
                <i class="fa fa-home"></i>
            </button>
            <button class="button-logout-admin" type="submit" form="logoutForm"
                    class="button-side-footer" id="logout" title="Logout">
                <i class="fa fa-power-off"></i>
            </button>
        </div>

    </nav>
    <form id="controller" method="post">
        <input type="hidden" name="controllerAction" value="">
        <input type="hidden" name="currentDate" id="currentDate" value="">
    </form>
    <form id="logoutForm" method="post">
        <input type="hidden" name="controllerAction" value="home.Home.logout">
    </form>
    <form id="goBackToHome" method="post">
        <input type="hidden" name="controllerAction" value="home.Home.view">
    </form>
</div>

<!---------------------------------------- MODAL FOR APPLICATION MESSAGE ---------------------------------------------->

<div class="modal fade" id="adminAppMessage" role="dialog">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header text-light bg-dark text-center">
                <h5 class="modal-title">Application message:</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body" id="applicationMessage" style="min-height: 100px;">
                <span style="font-style: italic;" id="spanMessage"></span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!---------------------------------------- END MODAL FOR APPLICATION MESSAGE ------------------------------------------>

<script>
    function setControllerAction(value) {
        let form = document.getElementById('controller');
        form.elements['controllerAction'].value = value;
        form.submit();
    }
</script>