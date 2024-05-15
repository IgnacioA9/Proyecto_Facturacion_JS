document.addEventListener("DOMContentLoaded",loaded);

async function loaded(event) {
    try{ await menu();} catch(error){return;}
}

function showLogin(){
    var registerLink = document.getElementById("registerlink");
    document.querySelector(".popup").classList.add("active");

    document.querySelector(".popup .close-btn").addEventListener("click",function(){
        document.querySelector(".popup").classList.remove("active");
    });
    document.getElementById('login').addEventListener('click',login);

    registerLink.addEventListener("click", function(event) {
        // Prevenimos el comportamiento predeterminado del enlace
        event.preventDefault();
        // Redirigimos a la nueva URL
        document.location = "/pages/create/View.html";
    });
}