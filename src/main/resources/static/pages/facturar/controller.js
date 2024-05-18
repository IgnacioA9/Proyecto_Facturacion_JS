var api=backend+'/facturar';

document.addEventListener("DOMContentLoaded", loaded);
document.addEventListener('visibilitychange',unloaded);

async function loaded(event) {
    try {
        await menu();
        setupEventListeners();
    } catch (error) {
        console.error('Error loading menu:', error);
        return;
    }
    state_json = sessionStorage.getItem("variables")
    if (!state_json){
        setupEventListeners();
    }else{
        state = JSON.parse(state_json);
    }
}

async function unloaded(event){
    if (document.visibilityState==="hidden" && loginstate.logged){
        sessionStorage.setItem("variables",JSON.stringify(state));
    }
}

function setupEventListeners(){
    const proveedorSpan = document.getElementById("proveedor");
    proveedorSpan.textContent = " " + loginstate.user.id;

    // Asigna los href a los enlaces
    document.getElementById("linkListaClientes").href = "/pages/clientes/View.html";
    document.getElementById("linkListaProductos").href = "/pages/productos/View.html";
    document.getElementById("linkBuscarProducto").href = "/pages/buscarProducto/View.html";
}
