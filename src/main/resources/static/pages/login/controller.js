document.addEventListener("DOMContentLoaded",loaded);

async function loaded(event) {
    try{ await menu();} catch(error){return;}
}

function showLogin(){
    document.querySelector(".popup").classList.add("active");

    document.querySelector(".popup .close-btn").addEventListener("click",function(){
        document.querySelector(".popup").classList.remove("active");
    });
    document.getElementById('login').addEventListener('click',login);
}