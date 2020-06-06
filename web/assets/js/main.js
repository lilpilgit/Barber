/* GLOBAL VARIABLE */

var RESULT_CHANGE_DESIRED_QTY = false; /* variabile globale per gestire il risultato tornato dalla request AJAX */

/* *************** */

function setButtonActive(id) {
    /**
     * Add the class "active2" to the button of navbar based on actual active page
     * */
    let button2active = document.getElementById(id);
    button2active.classList.add("active2");
}

function showMessage(msg) {
    /**
     * Show message with simple alert TODO:migliorare la grafica anche nella funzione omologa in admin.js
     */
    alert(msg);
}

function toUpperCase(element) {
    /**
     * set the value of input selected  by ID to upperCase
     */
    element.value = element.value.toUpperCase();
}

function handlerCounterQtaProduct(id_minus, id_qta, id_plus, max_qta) {
    /**
     * Realize the counter of quantity
     * Parameter in order: id of div in which to insert note of danger, id of button to decrease, id of quantity text, id of button to increase, max quantity available to select
     * Set the quantity text on click of buttons.
     * Return true if operation of '+' or '-' is allowed otherwise return false.
     */
    let error_present = false;
    max_qta = parseInt(max_qta);
    // let div_father = document.getElementById(id_father);
    let minus_btn = document.getElementById(id_minus);
    let qta_field = document.getElementById(id_qta);
    let plus_btn = document.getElementById(id_plus);
    // let div_max_qta = document.createElement("div");
    // div_max_qta.classList.add("max-qta-reached");
    // div_max_qta.innerHTML = "<p><strong>Warning!</strong>Maximum quantity reached.</p>";


    minus_btn.addEventListener("click", () => {
        if (error_present) {
            // div_father.removeChild(div_max_qta);
            error_present = false;
        }
        if (parseInt(qta_field.value) !== 1) {
            /*i can decrease the quantity*/
            qta_field.value -= 1;
        }
    });


    plus_btn.addEventListener("click", () => {
        if (parseInt(qta_field.value) !== max_qta) {
            /*i can increase the quantity*/
            qta_field.value = parseInt(qta_field.value) + 1;
        } else {
            /*show popup next to the counter to alert customer */
            error_present = true;
            // div_father.appendChild(div_max_qta);
        }
    });


}

function setProductForm(idProduct) {
    /**
     * Set id of product to show inside input hidden >idProduct< into form with id >showProductForm<
     */

    let productForm = document.getElementById('showProductForm');

    /*setto il value del campo idProduct del prodotto scelto da visualizzare */
    productForm.elements['idProduct'].value = idProduct;

    /*submit della form verso il dispatcher*/

    productForm.submit();
}

function setFilterForm(id_select_of_category, id_select_of_brand, old_category_chosen, old_brand_chosen) {
    /*
    /* BISOGNA PASSARE LE ATTUALI CATEGORIE E BRAND SELEZIONATI PER EVITARE CHE VENGA RICARICATO LA CATEGORIA "All"
    *  Aggiunge "selected" all'opzione dinamicamente quando l'utente la seleziona.
    */

    let filterForm = document.getElementById('filterForm');

    /* Prendo il valore della categoria selezionata non appena cambia nel dropdown */
    let select_for_category = document.getElementById(id_select_of_category);
    let category_chosen = old_category_chosen; /*   = "All"; BUG!!!!!*/


    select_for_category.addEventListener("change", () => {
        category_chosen = select_for_category.options[select_for_category.selectedIndex].text;
        /* cambio il value del relativo campo nella form */
        filterForm.elements['category'].value = category_chosen;
        filterForm.elements['filter'].value = '1';

        console.log("category_chosen:" + category_chosen);
        console.log("brand_chosen:" + brand_chosen);
    });

    /* Prendo il valore del brand selezionato non appena cambia nel dropdown */
    let select_for_brand = document.getElementById(id_select_of_brand);
    let brand_chosen = old_brand_chosen;/*   = "All"; BUG!!!!!*/
    select_for_brand.addEventListener("change", () => {
        brand_chosen = select_for_brand.options[select_for_brand.selectedIndex].text;
        /* cambio il value del relativo campo nella form */
        filterForm.elements['brand'].value = brand_chosen;
        filterForm.elements['filter'].value = '1';
        console.log("category_chosen:" + category_chosen);
        console.log("brand_chosen:" + brand_chosen);
    });

    console.log("category_chosen:" + category_chosen);
    console.log("brand_chosen:" + brand_chosen);

}

function setSelectedFilter(id_select, filter) {
    let select = document.getElementById(id_select).options;
    if (filter !== "All") {
        for (let option = 0; option < select.length; option++) {
            if (select[option].text === filter) {
                select[option].selected = true;
                break;
            }
        }
    }
}

function setSelectedAttribute(id_select, valueFromDb) {
    let select = document.getElementById(id_select).options;

    for (let option = 0; option < select.length; option++) {
        if (select[option].text === valueFromDb) {
            select[option].selected = true;
            break;
        }

    }
}

function getPageName() {
    let path = window.location.pathname;
    let page_name = path.split("/").pop();

    return page_name;
}

function sumTime() {
    let time1 = "01:00:00";
    let time2 = "01:30:00";
    let time3 = "00:30:00";

    let hour = 0;
    let minute = 0;
    let second = 0;

    let splitTime1 = time1.split(':');
    let splitTime2 = time2.split(':');
    let splitTime3 = time3.split(':');

    hour = parseInt(splitTime1[0]) + parseInt(splitTime2[0]) + parseInt(splitTime3[0]);
    minute = parseInt(splitTime1[1]) + parseInt(splitTime2[1]) + parseInt(splitTime3[1]);
    hour = hour + minute / 60;
    minute = minute % 60;
    second = parseInt(splitTime1[2]) + parseInt(splitTime2[2]) + parseInt(splitTime3[2]);
    minute = minute + second / 60;
    second = second % 60;

    alert('sum of above time= ' + hour + ':' + minute + ':' + second);
}

function getSelectedDateOfBooking(date_field) {
    let input = this.value;
    let dateEntered = new Date(input);
    console.log(input); //e.g. 2015-11-13
    console.log(dateEntered); //e.g. Fri Nov 13 2015 00:00:00 GMT+0000 (GMT Standard Time)

}

function showResult(result, message) {
    /**
     * show alert with message returned from server
     */
    if (result === 'success')
        alert(message);
    else if (result === 'fail')
        alert(message)

}

function addProductToCart(id) {
    /**
     * for form with ID ==> action_product, set value of hidden input field with
     * name="controllerAction" to "home.Cart.addToCart" and set value of hidden input field (of product.jsp)
     * with name="idProduct" to parameter id
     */
    /* Setto la controllerAction usando il form con id="action" che è proprio della product.jsp */
    let form = document.getElementById('action_product');
    form.elements['controllerAction'].value = 'home.Cart.addToCart';
    form.elements['idProduct'].value = id;
    form.submit();
}

function removeProductFromCart(id) {
    /**
     * for form with ID ==> action_product, set value of hidden input field with
     * name="controllerAction" to "home.Cart.removeFromCart" and set value of hidden input field (of cart.jsp)
     * with name="idProduct" to parameter id
     */
    /* Setto la controllerAction usando il form con id="action" che è proprio della cart.jsp */
    let form = document.getElementById('action_product');
    form.elements['controllerAction'].value = 'home.Cart.removeFromCart';
    form.elements['idProduct'].value = id;
    form.submit();
}

function addProductToWishlist(id) {
    /**
     * for form with ID ==> action_product, set value of hidden input field with
     * name="controllerAction" to "home.Wishlist.addToWishlist" and set value of hidden input field (of product.jsp)
     * with name="idProduct" to parameter id
     */
    /* Setto la controllerAction usando il form con id="action" che è proprio della product.jsp */
    let form = document.getElementById('action_product');
    form.elements['controllerAction'].value = 'home.Wishlist.addToWishlist';
    form.elements['idProduct'].value = id;
    form.submit();
}

function removeProductFromWishlist(id) {
    /**
     * for form with ID ==> action_product, set value of hidden input field with
     * name="controllerAction" to "home.Wishlist.removeFromWishlist" and set value of hidden input field
     * with name="idProduct" to parameter id
     */
    /* Setto la controllerAction usando il form con id="action" che è proprio della wishlist.jsp */
    let form = document.getElementById('action_product');
    form.elements['controllerAction'].value = 'home.Wishlist.removeFromWishlist';
    form.elements['idProduct'].value = id;
    form.submit();
}

function checkUncheckAll(nameGroup) {
    /**
     * Allows you to select or deselect a list of checkboxes with the same name
     */
    let checkboxes = document.querySelectorAll('input[name="' + nameGroup + '"]');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = !checkbox.checked;
    })
}

function handleCounterCart(id_minus, id_qta, id_plus, max_qta, idProduct) {
    /**
     * Realize the counter of quantity in page of cart
     * Parameter in order: id of div in which to insert note of danger, id of button to decrease, id of quantity text, id of button to increase, max quantity available to select
     * Set the quantity text on click of buttons.
     * Return true if operation of '+' or '-' is allowed otherwise return false.
     */
    let error_present = false;
    max_qta = parseInt(max_qta);
    // let div_father = document.getElementById(id_father);
    let minus_btn = document.getElementById(id_minus);
    let qta_field = document.getElementById(id_qta);
    let plus_btn = document.getElementById(id_plus);
    // let div_max_qta = document.createElement("div");
    // div_max_qta.classList.add("max-qta-reached");
    // div_max_qta.innerHTML = "<p><strong>Warning!</strong>Maximum quantity reached.</p>";


    minus_btn.addEventListener("click", () => {
        if (error_present) {
            // div_father.removeChild(div_max_qta);
            error_present = false;
        }
        if (parseInt(qta_field.value) !== 1) {
            /* la decremento sul db */
            changeQuantityProductInCart(idProduct, 'decrease');
            if (RESULT_CHANGE_DESIRED_QTY === true) {
                /*i can decrease the quantity*/
                qta_field.value -= 1;
                RESULT_CHANGE_DESIRED_QTY = false; /* IMPORTANTE! resettarlo a false subito dopo */
            }
        }
    });


    plus_btn.addEventListener("click", () => {
        if (parseInt(qta_field.value) !== max_qta) {
            /* la incremento sul db */
            changeQuantityProductInCart(idProduct, 'increase');
            if (RESULT_CHANGE_DESIRED_QTY === true) {
                /*i can increase the quantity*/
                qta_field.value = parseInt(qta_field.value) + 1;
                RESULT_CHANGE_DESIRED_QTY = false; /* IMPORTANTE! resettarlo a false subito dopo */
            }
        } else {
            /*show popup next to the counter to alert customer */
            error_present = true;
            // div_father.appendChild(div_max_qta);
        }
    });

}

function changeQuantityProductInCart(idProduct, operation) {

    /**
     * Send AJAX request with POST method to controller to increase the desired quantity into CART table for specified
     * loggedUser and product with ID = idProduct
     *
     * Parameters required by the controller: idProduct
     *                                        operation ==> {"increase" increase desired quantity, "decrease" decrease desired quantity, }
     * @type {XMLHttpRequest}
     */
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            alert("Modificato!");
            RESULT_CHANGE_DESIRED_QTY = true;
        } else if (this.readyState === 4 && this.status === 418) {
            alert("ERRORE!!!!");
            RESULT_CHANGE_DESIRED_QTY = false;
        }
    };

    xhttp.open("GET", "", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("controllerAction=home.Cart.changeDesiredQuantity&idProduct=" + idProduct + "&operation=" + operation);

}

