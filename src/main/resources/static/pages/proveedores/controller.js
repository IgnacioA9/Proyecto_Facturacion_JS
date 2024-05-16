var api = backend + '/proveedores';

var state = {
    list: [
        { cedula: "1234567890", nombre: "Juan Perez", correo: "juan.perez@example.com", telefono: "555-1234", estado: true },
        { cedula: "0987654321", nombre: "Maria Lopez", correo: "maria.lopez@example.com", telefono: "555-5678", estado: true },
        { cedula: "1122334455", nombre: "Carlos Ramirez", correo: "carlos.ramirez@example.com", telefono: "555-9101", estado: true },
        { cedula: "5566778899", nombre: "Ana Torres", correo: "ana.torres@example.com", telefono: "555-1122", estado: true }
    ],
    item: { cedula: "", nombre: "", correo: "", telefono: "", estado: "" },
}


document.addEventListener("DOMContentLoaded", loaded);

async function loaded(event) {
    try {
        await menu();
        setupEventListeners();
    } catch (error) {
        console.error('Error loading menu:', error);
        return;
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
    var listado = document.querySelector('#listaProveedores tbody');
    listado.innerHTML = "";
    state.list.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    if (item.estado) {
        var tr = document.createElement("tr");
        tr.innerHTML = `
        <td class='cedula'>${item.cedula}</td>
        <td class='nombre'>${item.nombre}</td>
        <td class='estado'>Aceptado</td></td>
        <td class='rechazar'><img src='/images/delete.png'></td>    `;
        tr.querySelector(".rechazar").addEventListener("click", function () {
            rechazar(item, tr);
        });
        listado.appendChild(tr);
    }
    /*  Si se desea agregar, yo lo quite para que no se ve tan grande, si se agrega tambien hacerlo en el HTML
    <td className='correo'>${item.correo}</td>
    <td className='telefono'>${item.telefono}</td>*/
}

function rechazar(item, row) {
    item.estado = false;
    renderList();
}

/*function rechazar(item, row) {
    //item.estado = false; esto se cambia en el server
    let request = new Request(api + `/rechazar`, {
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


