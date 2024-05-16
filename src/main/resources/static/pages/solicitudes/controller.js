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

    /*const request = new Request(api, { method: 'GET', headers: {} });
    (async () => {
        const response = await fetch(request);
        if (!response.ok) {
            errorMessage(response.status);
            return;
        }
        const data = await response.json();
        console.log(data); // Verificar la respuesta
        state.list = data;
        renderList();
        console.log(state.list);
    })();*/
    renderList();
}

function renderList() {
    var listado = document.querySelector('#listaSolicitudes tbody');
    listado.innerHTML = "";
    state.personas.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    if(!item.estado){
      var tr = document.createElement("tr");
    var estadoTexto = item.estado ? "Aceptado" : "Rechazado";
    tr.innerHTML = `
        <td class='cedula'>${item.cedula}</td>
        <td class='nombre'>${item.nombre}</td>
        <td class='estado'>${estadoTexto}</td>
        <td class='aprobar'><img src='/images/accept.png'></td>
    `;
    tr.querySelector(".aprobar").addEventListener("click", function() {
        aprobar(item, tr);
    });
    listado.appendChild(tr);
    }
}

/*function aprobar(item, row) {
    //item.estado = true; esto se cambia en el server
    let request = new Request(api + `/aceptar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(item)
    });
    (async () => {
        const response = await fetch(request);
        if (!response.ok) {
            errorMessage(response.status);
            return;
        }
        fetchAndList();
    })();
}*/

//Metodos creados para probarlo con el JS

/*function renderListItem(listado, item) {
    if (!item.estado) {  // Solo agregar si el estado es rechazado (false)
        var tr = document.createElement("tr");
        var estadoTexto = item.estado ? "Aceptado" : "Rechazado";
        tr.innerHTML = `
            <td class='cedula'>${item.cedula}</td>
            <td class='nombre'>${item.nombre}</td>
            <td class='estado'>${estadoTexto}</td>
            <td class='aprobar'><img src='/images/accept.png'></td>
        `;
        tr.querySelector(".aprobar").addEventListener("click", function() {
            aprobar(item, tr);
        });
        listado.appendChild(tr);
    }
}*/

function aprobar(item, row) {
    item.estado = true;
    renderList();
}


function load_item(){
    state.item={
        cedula:document.getElementById("cedula").value,
        nombre:document.getElementById("nombre").value,
        correo: document.getElementById("correo").value,
        telefono: document.getElementById("telefono").value
    };
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


