var api=backend+'/clientes';

var state ={
    list: new Array(),
    item : {cedula:"", nombre:"",correo:"", telefono:""},
    mode: "" // ADD, EDIT
}

document.addEventListener("DOMContentLoaded",loaded);

async function loaded(event){
    try{ await menu();} catch(error){return;}
    fetchAndList();
}