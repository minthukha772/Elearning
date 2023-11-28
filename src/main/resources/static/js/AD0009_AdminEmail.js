let inquiry_message_card = document.querySelectorAll('#see_more');
let message_box_container = document.querySelector('#message_box_container');
let reply_box_container = document.querySelector('#reply_box_container');
let clc_btn = document.querySelector('#message_clc_btn');
let reply = document.querySelectorAll('#reply_btn');


document.addEventListener('click',(e)=>{let id = e.target; console.log(id)})
inquiry_message_card.forEach(card=>{
    card.addEventListener('click',()=>message_box_container.classList.remove('d-none'))
})

reply.forEach(reply_msg=>{
    reply_msg.addEventListener('click',()=>reply_box_container.classList.remove('d-none'))
})

const msg_clc_btn = () => {
    message_box_container.classList.add('d-none')
}

const reply_clc_btn = () => {
    reply_box_container.classList.add('d-none')
}