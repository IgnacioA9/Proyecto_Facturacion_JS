var api=backend+'/clientes';

var state ={
    list: new Array(),
    item : {cedula:"", nombre:"",correo:"", telefono:""},
    mode: "" // ADD, EDIT
}

document.addEventListener("DOMContentLoaded", loaded);

async function loaded(event) {
    try {
        await menu();
    } catch (error) {
        return;
    }

    // Obtener referencia al botón btnCreate y al popup
    const btnCreate = document.getElementById("btnCreate");
    const popup = document.querySelector(".popup");

    // Agregar evento click al botón btnCreate
    btnCreate.addEventListener("click", function() {
        // Agregar la clase .active al popup
        popup.classList.add("active");
    });

    // Función para cerrar el popup cuando se hace clic en el botón de cerrar
    const closeBtn = document.querySelector(".close-btn");
    closeBtn.addEventListener("click", function() {
        popup.classList.remove("active");
    });
    fetchAndList();
}