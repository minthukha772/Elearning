
// alert countdown 
var deadline = new Date("Mar 14, 2023 15:37:25").getTime();
var x = setInterval(function () {
  var now = new Date().getTime();
  var t = deadline - now;
  var days = Math.floor(t / (1000 * 60 * 60 * 24));
  var hours = Math.floor((t % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  var minutes = Math.floor((t % (1000 * 60 * 60)) / (1000 * 60));
  var seconds = Math.floor((t % (1000 * 60)) / 1000);
  document.querySelector("#displayDiv").innerHTML =
  `<small class="text-white fw-bold">${days}d ${hours}h ${minutes}m ${seconds}s</small>`;
  if (t < 0) {
    clearInterval(x);
    document.querySelector("#displayDiv").innerHTML = `<small class="text-white fw-bold">EXPIRED</small>`;
  }
}, 1000);

// let noti;
// function audioPlay () {
//   noti = document.querySelector('.notification-sound');
//   noti.play();
// }


document.querySelector('#create_question').addEventListener('click',() => document.querySelector(".modalBox").classList.add('d-none'))
setTimeout(
  () => {
    document.querySelector("#displayDiv").classList.remove("hidden");
    document.querySelector(".modalBox").classList.remove("d-none");
  },
  5000
);

// question No 
let questionBoxLength = document.getElementsByClassName("question").length;
let questionNo = document.querySelector(".question-no");
let okBtn = document.querySelector("#create_question");
questionNo.innerText = `${questionBoxLength} Questions`;