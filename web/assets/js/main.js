function setButtonActive(id) {
    /**
     * Add the class "active2" to the button of navbar based on actual active page
     * */
    let button2active = document.getElementById(id);
    button2active.classList.add("active2");
}

function showResult(result, message) {
    /**
     * show modal with message returned from server
     */
    if (result === 'success') {
        document.getElementById("applicationMessage").style.backgroundColor = "rgba(43,226,125,0.48)";
        document.getElementById("spanMessage").innerHTML = "";
        document.getElementById('spanMessage').innerHTML = '<i class="far fa-check-circle" style="color: #43a33b"></i>&nbsp;&nbsp;&nbsp;' + message;
        $('#appMessage').modal("show");
    } else if (result === 'fail') {
        document.getElementById("applicationMessage").style.backgroundColor = "rgba(232,91,91,0.48)";
        document.getElementById("spanMessage").innerHTML = "";
        document.getElementById('spanMessage').innerHTML = '<i class="fas fa-exclamation-circle" style="color: rgba(163,35,0,0.94)"></i>&nbsp;&nbsp;&nbsp;' + message;
        $('#appMessage').modal("show");
    }
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

    let minus_btn = document.getElementById(id_minus);
    let qta_field = document.getElementById(id_qta);
    let plus_btn = document.getElementById(id_plus);

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

function setSelectedAttribute(id_select, valueFromDb, deselect = false) {

    /* prima controllo se si vuole selezionare o deselezionare l'opzione già presente */
    if (deselect === true) {
        document.getElementById(id_select).selectedIndex = "0";
    } else {

        let select = document.getElementById(id_select).options;

        for (let option = 0; option < select.length; option++) {
            if (select[option].text === valueFromDb) {
                select[option].selected = true;
                break;
            }
        }
    }
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
    let checkboxes_checked = []; /* conterrà le checkboxes che sono state flaggate */
    let eachOriginalPrice;
    let eachFinalPrice;
    let desiredQuantity;
    let eachDiscount;
    let totalPrice = 0;
    let totalSaved = 0;

    /* vedo quali checkbox sono stati checkati */
    checkboxes.forEach(function (checkbox) {
        if (checkbox.checked === true) {
            checkboxes_checked.push(checkbox);
        }
    })

    /* per ogni checkbox checkata sommo il prezzo singolo moltiplicandolo per l'attuale quantità */
    checkboxes_checked.forEach(function (checkbox) {
        /* checkbox.value rappresenta l'id del prodotto checkato, eachFinalPrice_IDPRODOTTO */
        eachFinalPrice = document.getElementById('eachFinalPrice_' + checkbox.value).value;
        eachOriginalPrice = document.getElementById('eachOriginalPrice_' + checkbox.value).value;
        desiredQuantity = document.getElementById('quantity_' + checkbox.value).value;
        eachDiscount = document.getElementById('eachDiscount_' + checkbox.value).value;

        /* calcolo il prezzo totale */
        totalPrice += parseFloat(eachFinalPrice) * parseInt(desiredQuantity);

        /* calcolo il risparmio parziale e lo aggiungo al totale solo se lo sconto è diverso da 0 */
        if (eachDiscount !== 0) {
            totalSaved = parseFloat(totalSaved) + (parseInt(desiredQuantity) * (parseFloat(eachOriginalPrice) * (parseInt(eachDiscount) / 100.00).toPrecision(2)));
            /*                                    |                                             risparmio parziale                                                   |        */
        }
    })

    totalPriceSpan.textContent = totalPrice.toFixed(2) + '\u20AC';
    totalPriceInputHidden.value = totalPrice.toFixed(2);
    totalSavedSpan.textContent = totalSaved.toFixed(2) + '\u20AC';
    totalSavedInputHidden.value = totalSaved.toFixed(2);

}

/** Send AJAX request with POST method to controller to increase the desired quantity into CART table for specified
 * loggedUser and product with ID = idProduct
 *
 * @param  operation : string {"increase" increase desired quantity, "decrease" decrease desired quantity}
 * @param id_qta : string {id of quantity field}
 * @param max_qta : number {quantity reachable for this product}
 * @param idProduct : string {id of product that is being edited}
 * @param nameGroup : string {name of checkbox group in order to change the total price and savings}
 * @type {XMLHttpRequest}
 */
function changeQuantityProductInCart(operation, id_qta, max_qta, idProduct, nameGroup) {

    max_qta = parseInt(max_qta);
    let qta_field = document.getElementById(id_qta);
    let qta_field_value = parseInt(qta_field.value);
    let result = 'fail';
    let allowAJAX = false; /* mi consente di sapere se posso usare AJAX */
    let xhttp = new XMLHttpRequest();

    if ((operation === 'increase' && qta_field_value !== max_qta) || (operation === 'decrease' && qta_field_value !== 1)) {
        allowAJAX = true;
    } else {
        allowAJAX = false;
    }
    if (allowAJAX) {
        /* la incremento sul db */
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                result = JSON.parse(this.responseText).result;
                if (result === "success") {
                    if (operation === 'increase') {
                        /*i can increase the quantity*/
                        qta_field.value = qta_field_value + 1;
                    } else if (operation === 'decrease') {
                        /*i can decrease the quantity*/
                        qta_field.value -= 1; /* non esiste un operazione di '-' tra stringhe... */
                    }
                    /* devo ricalcolare prezzo totale e risparmio totale solo dopo aver modificato i text field relativi alla quantità  */
                    modifyTotalPriceAndSaving(nameGroup);
                } else if (result === 'fail') {
                    showResult('fail',"The quantity could not be changed!");
                } else {
                    showResult('fail',"ERROR ON BACKEND!");
                }
            }
        };

        xhttp.open("POST", "app", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("controllerAction=home.Cart.changeDesiredQuantity&idProduct=" + idProduct + "&operation=" + operation + "&isAjax=true");

    } else {
        console.log("NON SI PUÒ MODIFICARE SEI FUORI RANGE!");
    }
}

/**
 * Send AJAX request with POST method to controller to find the reserved time for a specific selectedDate
 *
 *
 * @type {string}
 */
function findSlot(idStructure, pickedDate) {

    let currentTime = getCurrentTime();
    let currentDate = getCurrentDate();

    let obj = null;
    let xhttp = new XMLHttpRequest();

    /* Funzione esclusiva per inserire le option nella select di book.jsp */
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
                console.log(obj);

                /* Prima di tutto rimuovo tutti gli elementi dalla select */
                let sel = document.getElementById('time');
                for (let i = sel.length - 1; i >= 0; i--) {
                    sel.remove(i);
                }

                /* Inserisco gli orari disponibili che trovo nel Json all'interno della select */
                for (let i = 0; i < obj.availableTimes.length; i++)
                    if (obj.availableTimes[i])
                        addOption(obj.availableTimes[i]);

                if (obj.availableTimes.length === 0)
                    addOption("Change date :(");

            } else if (obj.result === 'fail') {
                showResult('fail',"ERRORE NEL BACKEND!!");
            } else showResult('fail',"Valore di result sconosciuto nel JSON");
        }
    };

    xhttp.open("POST", "app", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("controllerAction=home.Book.reservedSlot&idStructure=" + idStructure + "&pickedDate=" + pickedDate + "&currentTime=" + currentTime + "&currentDate=" + currentDate + "&isAjax=true");
    console.log("Customer selected date:" + pickedDate);
}

function findBooking(idCustomer) {
    /**
     * Send AJAX request with POST method to controller to find the reserved booking for the logged User
     *
     *
     * @type {string}
     */

    let result = 'fail';
    let obj = null;
    let time = null;
    let deletedReason = "";
    let xhttp = new XMLHttpRequest();
    let bookedDate = document.getElementById("booked-date");
    let bookedTime = document.getElementById("booked-time");
    let bookingId = document.getElementById("booking-id");
    let notBookedYet = document.getElementById("not-booked-yet");
    let deleteBookBtn = document.getElementById("delete-book-btn");
    let bookTableResult = document.getElementById("book-table-result");

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            obj = JSON.parse(this.responseText);
            if (obj.result === "success") {
                console.log(obj);
                /* Se non e' presente la deletedReason nel db, nel Json trovero' deletedReason = "null" ma essendo stringa
                *  devo intercettare il valore ed assegnarci un valore null che non sia di tipo stringa */
                if (obj.deletedReason === "null")
                    deletedReason = null;
                else deletedReason = obj.deletedReason;

                if (obj.alreadyBooked === "true") {
                    /* CASO "GIA' PRENOTATO" */
                    bookedDate.innerText = obj.date;
                    time = obj.hourStart.split(':');
                    bookedTime.innerText = (time[0] + ":" + time[1]);
                    bookingId.value = obj.idBooking;
                    if (notBookedYet != null)
                        notBookedYet.remove();
                } else if (obj.alreadyBooked === "false" && obj.deletedReason !== undefined) {
                    /* CASO "CANCELLATO DALL'ADMIN"
                    Se e' presente la deletedReason, significa che l'admin ha cancellato l'appuntamento
                    e quindi visualizzo la deletedReason al cliente. AlreadyBooked rimane a false perche' il cliente
                    ha la possibilita' di prenotare un nuovo appuntamento. */
                    document.getElementById("ModalLabelBook").innerHTML = '<span ' +
                        'class="text-danger">BOOKING REJECTED BY THE ADMINISTRATOR</span>';
                    bookedDate.innerText = obj.date;
                    time = obj.hourStart.split(':');
                    bookedTime.innerText = (time[0] + ":" + time[1]);
                    bookingId.value = obj.idBooking;
                    if (notBookedYet != null)
                        notBookedYet.remove();
                    deleteBookBtn.remove();
                    document.getElementById('booking-text-area').innerHTML = '<textarea style="width: 100%;' +
                        'resize: none" rows="5" name="deletedReason" readonly>Deleted reason: ' + deletedReason + '</textarea>' +
                        '<button class="btn btnheader active2 mt-3" type="button" id="showBookNow"\n' +
                        '                            onclick=setNavFormHome("home.Book.showBook")>\n' +
                        '                        Book now!\n' +
                        '                    </button>';
                } else if (obj.alreadyBooked === "false") {
                    /* CASO: "NON PRENOTATO" */
                    document.getElementById("ModalLabelBook").innerHTML = "";
                    if (bookTableResult != null) {
                        bookTableResult.remove();
                    }
                }
            } else if (obj.result === 'fail') {
                showResult('fail',"ERRORE NEL BACKEND!!");
            } else showResult('fail',"Valore di result sconosciuto nel JSON");
        }
    };

    xhttp.open("POST", "app", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("controllerAction=home.Book.getBooking&idCustomer=" + idCustomer + "&isAjax=true");
}

function bookNow(loggedUserId, selectedTime, selectedDate) {
    let form = document.getElementById('action_book');
    let selectedOptionTime = document.getElementById(selectedTime);
    let date = document.getElementById(selectedDate).value;
    let now = new Date(); /* now rappresenta il momento in cui ho cliccato su book now! */
    let time = selectedOptionTime.options[selectedOptionTime.selectedIndex].text;

    /* Se ho ricevuto un'orario conforme */
    if (time !== "Change date :(") {
        /* Se ricevo un tempo, devo aggiungere i secondi per poterlo rendere compatibile con il formato TIME di sql */
        let formattedTime = time + ":00";
        /* creo un oggetto Date da confrontare con now */
        let dateTimeObj = getDateTimeObj(formattedTime, date);
        /* Per risolvere il problema del thinking time, verifico che l'ora per cui sto prenotando, non sia gia' passata */
        if (now < dateTimeObj) {
            /* Se ci sono orari disponibili riferiti alla data <date>, allora preparo la form */
            form.elements['controllerAction'].value = 'home.Book.bookAppointment';
            form.elements['idCustomer'].value = loggedUserId;
            form.elements['selected_date'].value = date;
            form.elements['selected_time'].value = formattedTime;
            console.log("Selected time: " + formattedTime + " Selected date: " + date + " Id User: " + loggedUserId);
            form.submit();
        } else {
            /* Nel caso l'ora per cui si vuole prenotare sia gia' opa */
            showResult('fail',"The time you chose has already passed!\n" +
                "The page will be reloaded automatically with the new updated data.");
            window.setTimeout("setNavFormHome('home.Book.showBook')", 3000); /* necessario per evitare il refresh veloce della pagina */
        }
    } else if (time === "Change date :(") {
        /* Qualora venisse cliccato su Book now! e non ci fossero orari disponibili, mando un alert */
        showResult('fail',"There are no appointments available on this date :(\n" + "Please try to change the date.");
    }
}

function showOrRemoveTextArea(option) {
    if (option === "show") {
        document.getElementById("delete-book-btn").remove();
        document.getElementById('booking-text-area').innerHTML = '<form method="post" id="deleteBookingForm">\n' +
            '                        <textarea style="width: 100%;resize: none" rows="5" required="" placeholder="Because..." name="deletedReason"></textarea>\n' +
            '\n' +
            '                        <div class="modal-footer">\n' +
            '                            <button type="button" class="btn btn-secondary" title="abort-operation" onclick=showOrRemoveTextArea("remove")>Cancel</button>\n' +
            '                            <button type="submit" id="ultimateBtnDel" class="btn btn-primary" style="background-color: rgba(255,5,3,0.66)">Delete booking\n' +
            '                            </button>\n' +
            '                            <input type="hidden" name="controllerAction" value="home.Book.deleteBooking">\n' +
            '                            <input type="hidden" class="form-control" name="bookingID" value=' + document.getElementById("booking-id").value + '>\n' +
            '                        </div>\n' +
            '                    </form>';
    } else if (option === "remove") {
        document.getElementById("deleteBookingForm").remove();
        document.getElementById("td-delete-book-btn").innerHTML = '<button type="button" class="trashbutton" id="delete-book-btn"\n' +
            '                                    title="Delete appointment"\n' +
            '                                    data-target="#alert"\n' +
            '                                    data-toggle="modal"\n' +
            '                                    onclick=showOrRemoveTextArea(\'show\')>\n' +
            '                                <i class="far fa-trash-alt"></i>\n' +
            '                            </button>';
    }
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
        showResult('fail',"Select at least one product for checkout.");
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

function setTmpId(id, hiddenDrawer) {
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

    let today = getCurrentDate();
    let maxfield = new Date();

    maxfield.setDate(maxfield.getDate() + 7);
    let dd = maxfield.getDate();
    let mm = maxfield.getMonth() + 1; //January is 0!
    let yyyy = maxfield.getFullYear();

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

function autoFillShippingAddress(checkbox, addressDB) {
    let statusCheckbox = checkbox.checked;
    if (statusCheckbox === true) {
        setSelectedAttribute("State", addressDB[0]);
        setSelectedAttribute("Region", addressDB[1]);
    } else {
        setSelectedAttribute("State", "", true);
        setSelectedAttribute("Region", "", true);
    }

    let city_field = document.getElementById("City");
    let cap_field = document.getElementById("Cap");
    let street_field = document.getElementById("Street");
    let houseNumber_field = document.getElementById("House-number");
    let selects = $("#State, #Region"); //JQUERY
    /*----------------------------------------------------------------*/
    this.value = (statusCheckbox === true) ? "same" : "different";
    /*----------------------------------------------------------------*/
    $(selects).prop("readonly", statusCheckbox);
    $(selects).toggleClass("not-selectable", statusCheckbox);
    $(selects).find("option").prop("hidden", statusCheckbox);
    /*----------------------------------------------------------------*/
    city_field.readOnly = statusCheckbox;
    city_field.value = (statusCheckbox === true) ? addressDB[2] : "";
    /*----------------------------------------------------------------*/
    cap_field.value = (statusCheckbox === true) ? addressDB[3] : "";
    cap_field.readOnly = statusCheckbox;
    /*----------------------------------------------------------------*/
    street_field.value = (statusCheckbox === true) ? addressDB[4] : "";
    street_field.readOnly = statusCheckbox;
    /*----------------------------------------------------------------*/
    houseNumber_field.value = (statusCheckbox === true) ? addressDB[5] : "";
    houseNumber_field.readOnly = statusCheckbox;
}

function getCurrentTime() {
    /**
     * Return String currentTime in the format HH:MM
     * @type {Date}
     */
    let today = new Date();
    let currentTime;
    let HH = today.getHours();
    let MM = today.getMinutes();

    /* Devo formattare il currentTime in modo da ottenere il seguente formato HH:MM */
    if (HH < 10) {
        HH = '0' + HH;
    }

    if (MM < 10) {
        MM = '0' + MM;
    }

    currentTime = HH + ":" + MM;
    return currentTime;
}

function setCurrentDate(id) {
    /**
     * Return current date in format gg-mm-aaaa and set it on a specific elementID
     * */

    let today = new Date();
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
    console.log(today);
    document.getElementById(id).value = today;
}

function getDateTimeObj(hhmmss, yyyymmdd) {
    /**
     * Transform a string format HH:MM:SS and a string format YYYY-MM-DD into a Date format
     *
     * @type {Date}
     */

    /* Creo un nuovo oggetto Date dove imposto gia' anno-mese-giorno */
    let dateTime = new Date(yyyymmdd);
    let splittedTime = hhmmss.split(":");
    dateTime.setHours(splittedTime[0]);
    dateTime.setMinutes(splittedTime[1]);
    dateTime.setSeconds(splittedTime[2]);

    return dateTime;
}

function getCurrentDate() {
    /**
     * Return String currentDate in the format YYYY-MM-DD
     * @type {Date}
     */
    let today = new Date();
    let currentDate;
    let dd = today.getDate();
    let mm = today.getMonth() + 1; //January is 0!
    let yyyy = today.getFullYear();

    /* Devo formattare la data odierna today in modo da ottenere YYYY-MM-DD */
    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }

    currentDate = yyyy + '-' + mm + '-' + dd;
    return currentDate;
}

function removeProductFromShowcase(idProduct) {
    /**
     * WARNING!!!! ONLY FOR ADMIN!!
     * Create the form necessary to remove a product from the showcase
     */

    let productForm = document.getElementById('showProductForm');

    /*setto il value del campo idProduct del prodotto scelto da visualizzare */
    productForm.elements['ProductID'].value = idProduct;
    productForm.elements['ProductStatus'].value = true;
    productForm.elements['fromHome'].value = true;
    console.log(productForm);
    /*submit della form verso il dispatcher*/
    productForm.submit();
}

