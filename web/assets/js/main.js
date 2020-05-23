/*Add the class "active2" to the button of navbar based on actual active page*/
function setButtonActive(id) {
    let button2active = document.getElementById(id);
    button2active.classList.add("active2");
}

/*
* Realize the counter of quantity for the product page
* Parameter in order: id of div in which to insert note of danger, id of button to decrease, id of quantity text, id of button to increase
* Set the quantity text on click of buttons
*/
function handlerCounterQtaProduct(id_father, id_minus, id_qta, id_plus, max_qta) {
    let error_present = false;
    max_qta = parseInt(max_qta);
    let div_father = document.getElementById(id_father);
    let minus_btn = document.getElementById(id_minus);
    let qta_field = document.getElementById(id_qta);
    let plus_btn = document.getElementById(id_plus);
    let div_max_qta = document.createElement("div");
    div_max_qta.classList.add("max-qta-reached");
    div_max_qta.innerHTML = "<p><strong>Warning!</strong>Maximum quantity reached.</p>";


    minus_btn.addEventListener("click", () => {
        if (error_present) {
            div_father.removeChild(div_max_qta);
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
            div_father.appendChild(div_max_qta);
        }
    });

}

/*
* Add "selected" to the option dynamically, when the user select an option.
*/

/*BISOGNA PASSARE LE ATTUALI CATEGORIE E BRAND SELEZIONATI PER EVITARE CHE VENGA RICARICATO LA CATEGORIA "All"*/
function setUrlFiltered(id_select_of_category, id_select_of_brand, old_category_chosen, old_brand_chosen, id_filter_btn) {
    /*get selected category on change*/
    let url_parameters = "";
    let value_filter_param = 0;
    let select_for_category = document.getElementById(id_select_of_category);
    let category_chosen = old_category_chosen; /*   = "All"; BUG!!!!!*/
    select_for_category.addEventListener("change", () => {
        category_chosen = select_for_category.options[select_for_category.selectedIndex].text;
    });

    /*get selected brand on change*/
    let select_for_brand = document.getElementById(id_select_of_brand);
    let brand_chosen = old_brand_chosen;/*   = "All"; BUG!!!!!*/
    select_for_brand.addEventListener("change", () => {
        brand_chosen = select_for_brand.options[select_for_brand.selectedIndex].text;
    });

    let a = document.getElementById(id_filter_btn);


    a.addEventListener("click", () => {
        /*If all parameter are on value default not send "filter" parameter*/
        if (category_chosen !== "All" || brand_chosen !== "All") {
            value_filter_param = 1;
            url_parameters = getPageName() + "?" + "filter=" + value_filter_param + "&" + "category=" + category_chosen + "&" + "brand=" + brand_chosen;
        } else {
            url_parameters = getPageName();
        }
        /*IL BUG ERA NELLA POSIZIONE DELLA STRINGA QUI SOTTO CHE INVECE DEVE STARE NELL'IF POCO SOPRA*/
        /*url_parameters = "shop.jsp?" + "filter=" + value_filter_param + "&" + "category=" + category_chosen + "&" + "brand=" + brand_chosen;*/
        a.setAttribute("href", url_parameters);
    });

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


