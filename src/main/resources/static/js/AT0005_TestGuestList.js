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

let guest_search_icon = document.querySelector('#guest_search_icon');
let guest_search = document.querySelector('#guest_search');

guest_search_icon.addEventListener('click', () => guest_search.focus())
