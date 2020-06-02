<div class="page-wrapper chiller-theme toggled">
    <nav id="sidebar" class="sidebar-wrapper">
        <div class="sidebar-content">

            <div class="sidebar-header">
                <div class="user-pic">
                    <img src="https://champsbarberschool.com/mainsite/wp-content/uploads/2013/12/barber-gallery-7.jpg"
                         alt="User picture"/>
                    <button onclick="setControllerAction('admin.Admin.showProfile')" title="Edit your profile"
                            class="button-side" type="button" id='showAdmin'>
                        <i class="fas fa-user-edit"></i>
                    </button>
                </div>
                <div class="user-info">
                    <span class="user-name"><%=loggedUser.getName()%>&nbsp;&nbsp;<strong><%=loggedUser.getSurname()%></strong></span>
                    <span class="user-role">Administrator</span>
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
                </ul>
            </div>
            <!-- sidebar-menu  -->
        </div>
        <!-- sidebar-content  -->
        <div class="sidebar-footer button-side-footer">
            <button type="submit" form="logoutForm"
                    class="button-side-footer" id="logout">
                <i class="fa fa-power-off"></i>
            </button>
        </div>

    </nav>
    <form id="controller" method="post">
        <input type="hidden" name="controllerAction" value="">
    </form>

    <form id="logoutForm" method="post">
        <input type="hidden" name="controllerAction" value="home.Home.logout">
    </form>
</div>
<script>
    function setControllerAction(value) {
        let form = document.getElementById('controller');
        form.elements['controllerAction'].value = value;
        form.submit();
    }
</script>