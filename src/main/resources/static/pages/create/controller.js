var api=backend+'/usuarios';

var state ={
    item : {identificacion:"", contrasena: "", rol:""}
}

document.addEventListener("DOMContentLoaded",loaded);

async function loaded(event) {
    //try{ await menu();} catch(error){return;}
    showRegister();
}

function showRegister(){
    var loginLink = document.getElementById("loginlink");
    document.querySelector(".popup").classList.add("active");

    document.getElementById('create').addEventListener('click',add);

    loginLink.addEventListener("click", function(event) {
        // Prevenimos el comportamiento predeterminado del enlace
        event.preventDefault();
        // Redirigimos a la nueva URL
        document.location = "/pages/login/View.html";
    });
}

function add() {
    load_item();
    if (!validate_item()) return;
    let request = new Request(api, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(state.item)
    });
    (async () => {
        const response = await fetch(request);
        if (!response.ok) {
            errorMessage(response.status);
            return;
        }
        limpiarCampos();
        alert("Usuario ingresado exitosamente");
    })();
}

function load_item() {
    var selectedRole = document.querySelector('input[name="rol"]:checked');
    state.item = {
        identificacion: document.getElementById("cedula").value,
        contrasena: document.getElementById("password").value,
        rol: selectedRole ? selectedRole.value : ""
    };
}

function validate_item(){
    var error=false;

    document.querySelectorAll('input').forEach( (i)=> {i.classList.remove("invalid");});

    if (state.item.identificacion.length==0){
        document.querySelector("#id").classList.add("invalid");
        error=true;
    }

    if (state.item.contrasena.length==0){
        document.querySelector("#password").classList.add("invalid");
        error=true;
    }

    return !error;
}

function limpiarCampos(){
    var radios = document.getElementsByName("rol");
    document.getElementById("cedula").value = '';
    document.getElementById("password").value = '';
    for(var i = 0; i < radios.length; i++) {
        radios[i].checked = false;
    }
}