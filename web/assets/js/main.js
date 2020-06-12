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

function checkUncheckAll(ele, nameGroup) {
    /**
     * Allows you to select or deselect a list of checkboxes with the same name
     */
    let checkboxes = document.querySelectorAll('input[name="' + nameGroup + '"]');

    if (ele.checked) {
        for (let i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = true;
        }
    } else {
        for (let i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = false;
        }
    }

    /* sono state modificate le checkbox devo rifare il calcolo del totale */
    modifyTotalPriceAndSaving(nameGroup);

}

function modifyTotalPriceAndSaving(nameGroup) {
    let totalSavedSpan = document.getElementById("totalSavedSpan");
    let totalSavedInputHidden = document.getElementById("totalSaved");
    let totalPriceSpan = document.getElementById("totalPriceSpan");
    let totalPriceInputHidden = document.getElementById("totalPrice");
    let checkboxes = document.querySelectorAll('input[name="' + nameGroup + '"]');
    let checkboxes_checked = [];
    let eachOriginalPrice;
    let eachFinalPrice;
    let desiredQuantity;
    let eachDiscount;
    let totalPrice = 0;
    let totalSaved = 0;

    /* vedo quali checkbox sono stati checkati */

    checkboxes.forEach(function (item) {
        if(item.checked === true){
            checkboxes_checked.push(item);
        }
    })

    /* per ogni checkbox checkata sommo il prezzo singolo moltiplicandolo per l'attuale quantità */
    checkboxes_checked.forEach(function (item) {
        eachFinalPrice = document.getElementById('eachFinalPrice_' + item.value).value;
        eachOriginalPrice = document.getElementById('eachOriginalPrice_' + item.value).value;
        desiredQuantity = document.getElementById('quantity_' + item.value).value;
        eachDiscount = document.getElementById('eachDiscount_' + item.value).value;

        /* calcolo il prezzo totale */
        totalPrice += parseFloat(eachFinalPrice) * parseInt(desiredQuantity);

        /* calcolo il risparmio parziale e lo aggiungo al totale solo se lo sconto è diverso da 0 */
        if(eachDiscount !== 0){
            totalSaved = parseFloat(totalSaved) + ( parseInt(desiredQuantity) * ( parseFloat(eachOriginalPrice) * ( parseInt(eachDiscount) / 100.00 ).toPrecision(2) ) );
            /*                                    |                                             risparmio parziale                                                   |        */
        }
    })

    totalPriceSpan.textContent = totalPrice.toFixed(2) + '\u20AC';
    totalPriceInputHidden.value = totalPrice.toFixed(2);
    totalSavedSpan.textContent = totalSaved.toFixed(2) + '\u20AC';
    totalSavedInputHidden.value = totalSaved.toFixed(2);

}

function changeQuantityProductInCart(operation, id_qta, max_qta, idProduct , nameGroup) {

    /**
     * Send AJAX request with POST method to controller to increase the desired quantity into CART table for specified
     * loggedUser and product with ID = idProduct
     *
     * Parameters required by the controller: operation ==> {"increase" increase desired quantity, "decrease" decrease desired quantity, }
     *                                        id_qta : id of quantity field
     *                                        max_qta : max quantity reachable for this product
     *                                        idProduct : id of product that is being edited
     *                                        nameGroup : name of checkbox group in order to change the total price and savings
     *
     * @type {XMLHttpRequest}
     */
    max_qta = parseInt(max_qta);
    let qta_field = document.getElementById(id_qta);
    let result = "fail";
    let allowAJAX = false; /* mi consente di sapere se posso usare AJAX */
    let xhttp = new XMLHttpRequest();

    if (operation === 'increase' && parseInt(qta_field.value) !== max_qta) {
        allowAJAX = true;
    } else if (operation === 'decrease' && parseInt(qta_field.value) !== 1) {
        allowAJAX = true;
    } else {
        alert("CHIAMATO SENZA PARAMETRO operation OPPURE non rispetti i range!!");
        allowAJAX = false;
    }
    if (allowAJAX) {
        /* la incremento sul db */
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                result = JSON.parse(this.responseText).result;
                if (result === "success") {
                    alert("Modificato!");
                    if (operation === 'increase') {
                        /*i can increase the quantity*/
                        qta_field.value = parseInt(qta_field.value) + 1;

                    } else if (operation === 'decrease') {
                        /*i can decrease the quantity*/
                        qta_field.value -= 1;
                    }
                    /* devo ricalcolare prezzo totale e risparmio solo dopo aver modificato i text field relativi alla quantità  */
                    modifyTotalPriceAndSaving(nameGroup);
                } else if (result === "fail") {
                    alert("Non è stato modificato!");
                } else {
                    alert("ERRORE NEL BACKEND!");
                }
            }

        };

        xhttp.open("POST", "app", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("controllerAction=home.Cart.changeDesiredQuantity&idProduct=" + idProduct + "&operation=" + operation);

    } else {
        console.log("NON SI PUÒ MODIFICARE SEI FUORI RANGE!");
    }

}

function findSlot(idStructure, pickedDate) {
    /**
     * Send AJAX request with POST method to controller to find the reserved time for a specific selectedDate
     *
     *
     * @type {string}
     */

    let result = "fail";
    let availableTimes = [];
    let obj = null;
    let xhttp = new XMLHttpRequest();

    function addOption(freeTime) {
        let time = document.getElementById("time");
        let option = document.createElement("option");
        option.text = (freeTime);
        time.add(option);
    }

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            obj = JSON.parse(this.responseText);
            if (obj.result === "success") {
                console.log("result: " + obj.result);

                /* Prima di tutto rimuovo tutti gli elementi dalla select */
                let sel = document.getElementById('time');
                for (let i = sel.length - 1; i >= 0; i--) {
                    sel.remove(i);
                }

                /* Inserisco gli orari disponibili all'interno della select */
                for (let i = 0; i < obj.availableTimes.length; i++) {
                    console.log(obj.availableTimes[i]);
                    addOption(obj.availableTimes[i]);
                }

                if (obj.availableTimes.length === 0)
                    addOption("Change date :(");

            } else if (obj.result === "fail") {
                alert("ERRORE NEL BACKEND!!");
            } else alert("Valore di result sconosciuto nel JSON");
        }
    };

    xhttp.open("POST", "app", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("controllerAction=home.Book.reservedSlot&idStructure=" + idStructure + "&pickedDate=" + pickedDate);
    console.log("The structure open at:" + idStructure);
    console.log("Customer selected date:" + pickedDate);

}

function findBooking(idStructure, idCustomer) {
    /**
     * Send AJAX request with POST method to controller to find the reserved booking for the logged User
     *
     *
     * @type {string}
     */

    console.log("nella findBooking")
    let result = "fail";
    let obj = null;
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            obj = JSON.parse(this.responseText);
            if (obj.result === "success") {
                console.log("result: " + obj.result);

            } else if (obj.result === "fail") {
                alert("ERRORE NEL BACKEND!!");
            } else alert("Valore di result sconosciuto nel JSON");
        }
    };

    xhttp.open("POST", "app", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    // xhttp.send("controllerAction=home.Book.getBooking&idStructure=" + idStructure + "&idCustomer=" + idCustomer);
    console.log("The current structure is: " + idStructure);
    console.log("The logged user is: " + idCustomer);
}


function bookNow(loggedUserId, selectedTime, selectedDate) {
    let form = document.getElementById('action_book');
    let selectedOptionTime = document.getElementById(selectedTime);
    let date = document.getElementById(selectedDate).value;
    let time;
    let formattedTime;

    time = selectedOptionTime.options[selectedOptionTime.selectedIndex].text;

    formattedTime = time + ":00";

    form.elements['controllerAction'].value = 'home.Book.bookAppointment';
    form.elements['idCustomer'].value = loggedUserId;
    form.elements['selected_date'].value = date;
    form.elements['selected_time'].value = formattedTime;
    console.log("Selected time: "+ formattedTime + " Selected date: " + date + " Id User: " + loggedUserId);
    form.submit();
}

function goToCheckout(nameGroup) {

    let form = document.getElementById('action_checkout');
    let totalPrice = document.getElementById('totalPrice').value;
    let totalSaved = document.getElementById('totalSaved').value;
    let checkOutJson = {
        "totalPrice": totalPrice,
        "totalSaved": totalSaved,
        "productsToBuy": []
    };
    /* Fetch all checkboxes checked and the corresponding values */
    let checkboxes = document.querySelectorAll('input[name="' + nameGroup + '"]');
    let atLeastOne = false;
    checkboxes.forEach((checkbox) => {
        if (checkbox.checked === true) {
            atLeastOne = true;
            /* aggiungo al json la coppia id:quantità_desiderata */
            let desiredQuantity = document.getElementById('quantity_' + checkbox.value).value; /* per poter indirizzare l'id del tipo : quantity_idProdotto*/
            let productName = document.getElementById('productName_' + checkbox.value).value;
            let eachFinalPrice = document.getElementById('eachFinalPrice_' + checkbox.value).value;
            let objNewProduct = {};

            objNewProduct["ID"] = checkbox.value;
            objNewProduct["desiredQty"] = desiredQuantity;
            objNewProduct["name"] = productName;
            objNewProduct["eachFinalPrice"] = eachFinalPrice;

            checkOutJson["productsToBuy"].push(objNewProduct);
        }
    })
    if (atLeastOne === false) {
        //TODO: mostrare popover al posto di alert
        alert("Select at least one product for checkout.");
    } else {
        /* posso procedere al checkout */
        console.log(JSON.stringify(checkOutJson));
        let checkoutInfo = document.getElementById('checkoutInfo');
        checkoutInfo.value = JSON.stringify(checkOutJson);

        /* Faccio il submit della form */
        form.submit();
    }
}

function showProductFromOrder(idProduct) {
    /**
     * Set name of hidden input to idProduct and value to id of product to show and set value of controllerAction to home.Product.showProduct
     * inside page of orders
     */
    let form = document.getElementById('order_action');
    form.elements['controllerAction'].value = 'home.Product.showProduct';
    form.elements['idProduct'].value = idProduct;
    form.submit();
}

function setTmpId(id,hiddenDrawer) {
    /**
     * Set id of object to delete/insert/modify into a temporary "drawer" before confirm this action into the modal */
    document.getElementById(hiddenDrawer).value = id;
}

function cancelOrder(idOrder) {
    /**
     * Set name of hidden input to idProduct and value to id of product to show and set value of controllerAction to home.Product.showProduct
     * inside page of orders
     */
    let form = document.getElementById('order_action');
    form.elements['controllerAction'].value = 'home.Orders.cancelOrder';
    form.elements['idOrder'].value = idOrder;
    form.submit();
}

function setDateBook(id) {
    /**
     * Return current date in format gg-mm-aaaa
     * */

    let today = new Date();
    let maxfield = new Date();
    let dd = today.getDate();
    let mm = today.getMonth() + 1; //January is 0!
    let yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }

    today = yyyy + '-' + mm + '-' + dd;
    maxfield.setDate(maxfield.getDate() + 7);
    dd = maxfield.getDate();
    mm = maxfield.getMonth() + 1; //January is 0!
    yyyy = maxfield.getFullYear();

    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }

    maxfield = yyyy + '-' + mm + '-' + dd;

    document.getElementById(id).value = today;
    document.getElementById(id).setAttribute("min", today);
    document.getElementById(id).setAttribute("max", maxfield);
}
