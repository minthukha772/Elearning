
// let searchIcon = document.querySelector('#searchIcon');
// let filter = document.getElementById('filter');
// searchIcon.addEventListener('click',() => filter.focus() )

// add modal 
let add_student = document.querySelector('#add_student');
let add_student_box = document.querySelector('.add-student-box');
let add_student_cancel = document.querySelector('#add_student_cancel');

add_student.addEventListener('click', () => add_student_box.classList.remove('d-none'))
add_student_cancel.addEventListener('click', () => add_student_box.classList.add('d-none'))

let add_student_search_icon = document.querySelector('#add_student_search_icon');
let add_student_search = document.querySelector('#add_student_search');

add_student_search_icon.addEventListener('click', () => add_student_search.focus())

// // Add Multiple Guest Dialog start
// let add_multiple_guest = document.querySelector('#add_multiple_guest');
// let add_multiple_guest_box = document.querySelector('.add-multiple-guest-box');
// let add_multiple_guest_cancel = document.querySelector('#add_multiple_guest_cancel');

// add_multiple_guest.addEventListener('click', () => {
//   add_multiple_guest_box.classList.add('animate__fadeIn');
//   add_multiple_guest_box.classList.remove('d-none', 'animate__fadeOut');
// })

// add_multiple_guest_cancel.addEventListener('click', () => {
//   add_multiple_guest_box.classList.remove('animate__fadeIn');
//   add_multiple_guest_box.classList.add('animate__fadeOut');
//   setTimeout(() => {
//     add_multiple_guest_box.classList.add('d-none');
//     removeCsvErrorMsg();
//   }, 1000);
// })
// // Add Multiple Guest Dialog end

// function removeAllChildNodes(parent) {
//   while (parent.firstChild) {
//     parent.removeChild(parent.firstChild);
//   }
// }

// function removeCsvErrorMsg() {
//   const noError = csvErrorMsgBox.classList.contains("d-none");
//   if (!noError) {
//     csvErrorMsgBox.classList.replace("d-flex", "d-none");
//     if (csvErrorMsgBox.classList.contains("border-danger")) {
//       csvErrorMsgBox.classList.remove("border-danger");
//     }
//     if (csvErrorMsgBox.classList.contains("border-success")) {
//       csvErrorMsgBox.classList.remove("border-success");
//     }
//     removeAllChildNodes(csvErrorMsgBox);
//   }
// }

// // CSV filename show start
// let csv_upload = document.querySelector('#csvUpload');
// const input = document.querySelector("#fileDialog");
// const preview = document.querySelector(".preview");

// const openFileDialog = () => {
//   input.click();
//   removeCsvErrorMsg();
// }
// csv_upload.addEventListener('click', openFileDialog)
// input.addEventListener("change", showFileName);

// function showFileName() {
//   while (preview.firstChild) {
//     preview.removeChild(preview.firstChild);
//   }

//   const curFiles = input.files;
//   if (curFiles.length === 0) {
//     const para = document.createElement("p");
//     para.textContent = "...";
//     preview.appendChild(para);
//   } else {
//     const para = document.createElement("p");
//     preview.appendChild(para);

//     for (const file of curFiles) {
//       para.textContent = file.name;
//     }
//   }
// }
// // CSV filename show end

let student_search_icon = document.querySelector('#student_search_icon');
let student_search = document.querySelector('#student_search');

student_search_icon.addEventListener('click', () => student_search.focus())