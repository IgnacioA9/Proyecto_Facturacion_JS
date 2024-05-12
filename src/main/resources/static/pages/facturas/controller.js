var api=backend+'/facturas';

var state ={
    list: new Array(),
    item : {codigo:"", nombre:"",precio:""},
    mode: "" // ADD, EDIT
}

document.addEventListener("DOMContentLoaded",loaded);

async function loaded(event){
    try{ await menu();} catch(error){return;}
    fetchAndList();
}