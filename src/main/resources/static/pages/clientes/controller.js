var api=backend+'/clientes';

var state ={
    list: new Array(),
    item : {cedula:"", nombre:"",correo:"", telefono:""},
    mode: "" // ADD, EDIT
}

document.addEventListener("DOMContentLoaded", loaded);

async function loaded(event) {
    /*try {
        await menu();
    } catch (error) {
        return;
    }
    */
    // Obtener referencia al botón btnCreate y al popup
    const btnCreate = document.getElementById("btnCreate");
    const popup = document.querySelector(".popup");
    const closeBtn = document.querySelector(".close-btn");
    const saveBtn = document.getElementById("guardarProductoBtn");
    const editBtn = document.getElementById("editarProductoBtn");

    // Agregar evento click al boton btnCreate
    btnCreate.addEventListener("click",ask);

    // Funcion para cerrar el popup cuando se hace click en el boton de cerrar
    closeBtn.addEventListener("click", function() {
        popup.classList.remove("active");
    });

    // Funcion para guardar producto cuando se hace click en el boton guardar
    saveBtn.addEventListener("click",add);
    // Funcion para editar producto cuando se hace click en el boton editar
    editBtn.addEventListener("click", () => edit(state.item.codigo));

    fetchAndList();
}

function showForm() {
    const popup = document.querySelector(".popup");
    popup.classList.add("active");
}

function NoshowForm() {
    const popup = document.querySelector(".popup");
    popup.classList.remove("active");
}

function fetchAndList(){
    const request = new Request(api, {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        state.list = await response.json();
        render_list_item();
    })();
}

function render_list(){
    var lista = document.getElementById('listaProductos');
    var tbody = lista.getElementsByTagName('tbody')[0];

    // Limpiar la lista antes de renderizarla nuevamente
    tbody.innerHTML = "";

    // Iterar sobre cada elemento en state.list y llamar a render_list_item
    state.list.forEach(item => render_list_item(item, tbody));
}

function render_list_item(){
    var lista = document.getElementById('listaClientes');
    var tbody = lista.getElementsByTagName('tbody')[0];

    state.list.forEach(function (item){
        var row = document.createElement('tr');

        var cedulaCell = document.createElement('td');
        cedulaCell.textContent = item.cedula;

        var nombreCell = document.createElement('td');
        nombreCell.textContent = item.nombre;

        var correoCell = document.createElement('td');
        correoCell.textContent = item.correo;

        var telefonoCell = document.createElement('td');
        telefonoCell.textContent = item.telefono;

        var deleteCell = document.createElement('td');
        var deleteButton = document.createElement('button');
        deleteButton.textContent = 'Eliminar';
        deleteButton.classList.add("btnAction");
        deleteButton.addEventListener('click', function() {
            remove(item.codigo);
        });
        deleteCell.appendChild(deleteButton);

        var editCell = document.createElement('td');
        var editButton = document.createElement('button');
        editButton.textContent = "Editar";
        editButton.classList.add("btnAction");
        editButton.addEventListener('click', function() {
            var fila = this.parentNode.parentNode;
            llenarCampos(fila);
            showForm();
            ocultarBotonGuardar();
            mostrarBotonEditar();
        });
        editCell.appendChild(editButton);

        row.appendChild(cedulaCell);
        row.appendChild(nombreCell);
        row.appendChild(correoCell);
        row.appendChild(telefonoCell);
        row.appendChild(editCell);
        row.appendChild(deleteCell);

        // Agregar la fila a tbody
        tbody.appendChild(row);
    });
}


function search(){
    nombreBusqueda = document.getElementById("busqueda").value;
    const request = new Request(api+`/search?nombre=${nombreBusqueda}`,
        {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        state.list = await response.json();
        render_list_item();
    })();
}

function ask(){
    limpiarCampos();
    empty_item();
    showForm();
    state.mode="ADD";
    ocultarBotonEditar();
    mostrarBotonGuardar();
}

function empty_item(){
    state.item={cedula:"", nombre:"",correo:"",telefono:""};
}

function load_item(){
    state.item={
        cedula:document.getElementById("cedula").value,
        nombre:document.getElementById("nombre").value,
        correo: document.getElementById("correo").value,
        telefono: document.getElementById("telefono").value
    };
}

function validate_item(){
    var error=false;

    document.querySelectorAll('input').forEach( (i)=> {i.classList.remove("invalid");});

    if (state.item.cedula.length==0){
        document.querySelector("#cedula").classList.add("invalid");
        error=true;
    }

    if (state.item.nombre.length==0){
        document.querySelector("#nombre").classList.add("invalid");
        error=true;
    }

    if (state.item.correo.length==0){
        document.querySelector("#correo").classList.add("invalid");
        error=true;
    }

    if (state.item.telefono.length==0){
        document.querySelector("#telefono").classList.add("invalid");
        error=true;
    }

    return !error;
}

function add(){
    load_item();
    if(!validate_item()) return;
    let request = new Request(api, {method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify(state.item)});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        NoshowForm();
        fetchAndList();
    })();
}

function edit(){
    load_item();
    if(!validate_item()) return;
    let request = new Request(api+`/edit`, {method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify(state.item)});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        NoshowForm();
        fetchAndList();
    })();
}

function remove(id){
    let request = new Request(backend+`/clientes/${id}`,
        {method: 'DELETE', headers: {}});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        fetchAndList();
    })();
}

function ocultarBotonEditar() {
    var botonEditar = document.getElementById("editarProductoBtn");
    botonEditar.classList.add("invisible");
}

function mostrarBotonEditar() {
    var botonEditar = document.getElementById("editarProductoBtn");
    botonEditar.classList.remove("invisible");
}

function ocultarBotonGuardar() {
    var botonGuardar = document.getElementById("guardarProductoBtn");
    botonGuardar.classList.add("invisible");
}

function mostrarBotonGuardar() {
    var botonGuardar = document.getElementById("guardarProductoBtn");
    botonGuardar.classList.remove("invisible");
}

function deshabilitarCodigo() {
    var campoCedula = document.getElementById('cedula');
    campoCedula.readOnly = true;
}

function habilitarCodigo() {
    var campoCedula = document.getElementById('cedula');
    campoCedula.readOnly = false;
}

function llenarCampos(fila){
    deshabilitarCodigo();
    var cedula = fila.querySelector('td:nth-child(1)').textContent;
    var nombre = fila.querySelector('td:nth-child(2)').textContent;
    var correo = fila.querySelector('td:nth-child(3)').textContent;
    var telefono = fila.querySelector('td:nth-child(4)').textContent;

    document.getElementById('cedula').value = cedula;
    document.getElementById('nombre').value = nombre;
    document.getElementById('correo').value = correo;
    document.getElementById('telefono').value = telefono;
}

function limpiarCampos(){
    document.getElementById('cedula').value = '';
    document.getElementById('nombre').value = '';
    document.getElementById('correo').value = '';
    document.getElementById('telefono').value = '';
}