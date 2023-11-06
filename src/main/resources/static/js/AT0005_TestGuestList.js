// add modal
let add_single_guest = document.querySelector('#add_single_guest');
let add_single_guest_box = document.querySelector('.add_single_guest_box');
let add_guest_cancel = document.querySelector('#add_guest_cancel');

// Clear the input fields
add_single_guest.addEventListener('click', () => {
    var guestName = document.getElementById("guest_name");
    var guestNameWarning = document.getElementById("guest_name_warning");
    var guestEmail = document.getElementById("guest_email");
    var guestEmailWarning = document.getElementById("guest_email_warning");
    var guestPhoneNumber = document.getElementById("guest_ph_no");
    var guestPhoneNumberWarning = document.getElementById("guest_ph_no_warning");

    add_single_guest_box.classList.remove('d-none');

    guestName.value = "";
    guestName.classList.remove("is-invalid");
    guestNameWarning.style.display = "none";

    guestEmail.value = "";
    guestEmail.classList.remove("is-invalid");
    guestEmailWarning.style.display = "none";

    guestPhoneNumber.value = "";
    guestPhoneNumber.classList.remove("is-invalid");
    guestPhoneNumberWarning.style.display = "none";

});
add_guest_cancel.addEventListener('click', () => add_single_guest_box.classList.add('d-none'));

// Add Multiple Guest Dialog start
let add_multiple_guest = document.querySelector('#add_multiple_guest');
let add_multiple_guest_box = document.querySelector('.add-multiple-guest-box');
let add_multiple_guest_cancel = document.querySelector('#add_multiple_guest_cancel');

add_multiple_guest.addEventListener('click', () => {
    add_multiple_guest_box.classList.add('animate__fadeIn');
    add_multiple_guest_box.classList.remove('d-none', 'animate__fadeOut');
})

add_multiple_guest_cancel.addEventListener('click', () => {
    add_multiple_guest_box.classList.remove('animate__fadeIn');
    add_multiple_guest_box.classList.add('animate__fadeOut');
    setTimeout(() => {
        add_multiple_guest_box.classList.add('d-none');
        removeCsvErrorMsg();
    }, 1000);
})
// Add Multiple Guest Dialog end

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function removeCsvErrorMsg() {
    const noError = csvErrorMsgBox.classList.contains("d-none");
    if (!noError) {
        csvErrorMsgBox.classList.replace("d-flex", "d-none");
        if (csvErrorMsgBox.classList.contains("border-danger")) {
            csvErrorMsgBox.classList.remove("border-danger");
        }
        if (csvErrorMsgBox.classList.contains("border-success")) {
            csvErrorMsgBox.classList.remove("border-success");
        }
        removeAllChildNodes(csvErrorMsgBox);
    }
}

// CSV filename show start
let csv_upload = document.querySelector('#csvUpload');
const input = document.querySelector("#fileDialog");
const preview = document.querySelector(".preview");

const openFileDialog = () => {
    input.click();
    removeCsvErrorMsg();
}
csv_upload.addEventListener('click', openFileDialog)
input.addEventListener("change", showFileName);

function showFileName() {
    while (preview.firstChild) {
        preview.removeChild(preview.firstChild);
    }

    const curFiles = input.files;
    if (curFiles.length === 0) {
        const para = document.createElement("p");
        para.textContent = "...";
        preview.appendChild(para);
    } else {
        const para = document.createElement("p");
        preview.appendChild(para);

        for (const file of curFiles) {
            para.textContent = file.name;
        }
    }
}
// CSV filename show end

let guest_search_icon = document.querySelector('#guest_search_icon');
let guest_search = document.querySelector('#guest_search');

guest_search_icon.addEventListener('click', () => guest_search.focus())
