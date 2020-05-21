/*function enableSidebar() {
    /!* jQuery for dropdown sidebar menu *!/
    jQuery(function ($) {
        $(".sidebar-dropdown > a").on('click', function () {
            console.log("click");
            $(".sidebar-submenu").slideUp(200);
            if (
                $(this)
                    .parent()
                    .hasClass("active")
            ) {
                $(".sidebar-dropdown").removeClass("active");
                $(this)
                    .parent()
                    .removeClass("active");
            } else {
                $(".sidebar-dropdown").removeClass("active");
                $(this)
                    .next(".sidebar-submenu")
                    .slideDown(200);
                $(this)
                    .parent()
                    .addClass("active");
            }
        });

        $("#close-sidebar").on('click', function () {
            $(".page-wrapper").removeClass("toggled");
        });
        $("#show-sidebar").on('click', function () {
            $(".page-wrapper").addClass("toggled");
        });
    });
}*/

function showResult(result, message) {
    /**
     * show alert with message returned from server
     */
    if (result === 'success')
        alert(message);
    else if (result === 'fail')
        alert(message)

}

function deleteEmployee(id){
    /**
     * for form with ID ==> action, set value of hidden input field with
     * name="controllerAction" to "Staff.deleteEmployee" and set value of hidden input field (of show-employee.jsp)
     * with name="employeeID" to parameter id
     */
    /* Setto la controllerAction usando il form con id="action" che è proprio della show-employee.jsp */
    let formEmployeeID = document.getElementById('action');
    formEmployeeID.elements['controllerAction'].value = 'Staff.deleteEmployee';
    formEmployeeID.elements['employeeID'].value = id;
    formEmployeeID.submit();

}

function editEmployee(id){
    /**
     * for form with ID ==> action, set value of hidden input field with
     * name="controllerAction" to "Staff.editEmployee" and set value of hidden input field (of show-employee.jsp)
     * with name="employeeID" to parameter id
     */
    /* Setto la controllerAction usando il form con id="action" che è proprio della show-employee.jsp */
    let formEmployeeID = document.getElementById('action');
    formEmployeeID.elements['controllerAction'].value = 'Staff.showFormEditEmployee';
    formEmployeeID.elements['employeeID'].value = id;
    formEmployeeID.submit();

}

function toUpperCase(element) {
    /**
     * set the value of input selected  by ID to upperCase
     */
    element.value = element.value.toUpperCase();
}
/*




/!*ALL FUNCTIONS ABOUT STAFF SECTION*!/
function showFormNewEmployee(xhttp) {
    if (xhttp.status >= 200 && xhttp.status < 400) {
        document.getElementById('to-fill').innerHTML = xhttp.responseText;
    }
}

/!*FUNCTION TO SHOW ERROR WITH AJAX*!/
function showAjaxError(xhttp) {
    if (xhttp.status >= 200 && this.status < 400) {
        document.getElementById('to-fill').innerHTML = this.responseText;
    }
}

/!***********************************!/

function fetchStaffSection(event) {

    let xhttp = new XMLHttpRequest();

    if (event.target.id === 'staff_new_employee') {
        /!*Show form to insert a new employee into the DB*!/
        xhttp.open('GET', 'templates/add-employee.html', true);
        xhttp.onload = function () {
            showFormNewEmployee(this);
        };
    } else if (event.target.id === 'staff_edit_employee') {

    } else if (event.target.id === 'staff_show_employee') {

    } else if (event.target.id === 'staff_del_employee') {

    } else {
        /!*Show an error message*!/
        xhttp.open('GET', 'templates/errors/AjaxError.html', true);
        xhttp.onload = function () {
            showAjaxError(this);
        };
    }
    xhttp.setRequestHeader("Cache-Control", "max-age=0"); /!*after open xhttp in the if*!/
    xhttp.send();
}

function addOnClickListenerBtnSidebar() {
    /!* * * * * * Staff * * * * * *!/
    /!*1)Listener on click for: Add new employee*!/

    document.getElementById('staff_new_employee').addEventListener('click', addNewEmployee);

    /!*2)Listener on click for: Edit existing employee*!/
    document.getElementById('staff_edit_employee').addEventListener('click', fetchStaffSection);
}

function addNewEmployee() {
    let xhttp = new XMLHttpRequest();
    /!*Show form to insert a new employee into the DB*!/
    xhttp.open('GET', '/manage?controllerAction=Staff.addEmployee', true);
    xhttp.onload = function () {
        showFormNewEmployee(this);

    }
}*/
