var api = backend + '/proveedores';

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
        renderList();
    }

}

async function unloaded(event){
    if (document.visibilityState==="hidden" && loginstate.logged){
        sessionStorage.setItem("variables",JSON.stringify(state));
    }
}

function setupEventListeners() {
    fetchAndList();
}

function fetchAndList() {
    //Quitar comentarios en caso de que se quiera probar con el servidor
    const request = new Request(api+"/cargar/rechazados", {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        const data = await response.json();
        console.log(data); // Verificar la respuesta
        state.proveedores = data;
        renderList();
    })();
}

function renderList() {
    var listado = document.querySelector('#listaSolicitudes tbody');
    listado.innerHTML = "";
    state.proveedores.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    if (!item.estado) {
        var tr = document.createElement("tr");
        tr.innerHTML = `
        <td class='cedula'>${item.cedula}</td>
        <td class='nombre'>${item.nombre}</td>
        <td class='estado'>Rechazado</td></td>
        <td class='rechazar'><img src='/images/delete.png'></td>    `;
        tr.querySelector(".rechazar").addEventListener("click", function () {
            aceptar(item, tr);
        });
        listado.appendChild(tr);
    }
    /*  Si se desea agregar, yo lo quite para que no se ve tan grande, si se agrega tambien hacerlo en el HTML
    <td className='correo'>${item.correo}</td>
    <td className='telefono'>${item.telefono}</td>*/
}


function aceptar(proveedor, row) {
    //proveedor.estado = false; esto se cambia en el server
    let request = new Request(api + `/aceptar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(proveedor)
    });
    (async () => {
        const response = await fetch(request);
        if (!response.ok) {
            errorMessage(response.status);
            return;
        }
        alert("Proveedor " + proveedor.cedula +" aceptado exitosamente");
        fetchAndList();
    })();
}

function errorMessage(status) {
    let error = "";
    switch (status) {
        case 404: error = "Registro no encontrado"; break;
        case 409: error = "Registro duplicado"; break;
        case 401: error = "Usuario no autorizado"; break;
        case 403: error = "Usuario no tiene derechos"; break;
        case 500: error = "Error al procesar la solicitud"; break;
        default: error = "Error desconocido"; break;
    }
    window.alert(error);
}


