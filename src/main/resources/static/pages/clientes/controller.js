var api=backend+'/clientes';

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
        render_list();
    }
}

async function unloaded(event){
    if (document.visibilityState==="hidden" && loginstate.logged){
        sessionStorage.setItem("variables",JSON.stringify(state));
    }
}

function setupEventListeners() {
    // Obtener referencia al botón btnCreate y al popup
    const btnCreate = document.getElementById("btnCreate");
    const popup = document.querySelector(".popup");
    const closeBtn = document.querySelector(".close-btn");
    const saveBtn = document.getElementById("guardarProductoBtn");
    const editBtn = document.getElementById("editarProductoBtn");
    const searchBtn = document.getElementById("btnBuscar");

    // Agregar evento click al boton btnCreate
    btnCreate.addEventListener("click",showForm);

    // Funcion para cerrar el popup cuando se hace click en el boton de cerrar
    closeBtn.addEventListener("click", function() {
        popup.classList.remove("active");
    });



    // Funcion para guardar producto cuando se hace click en el boton guardar
    saveBtn.addEventListener("click",add);
    // Funcion para editar producto cuando se hace click en el boton editar
    editBtn.addEventListener("click", edit);
    // Funcion para buscar producto cuando se hace click en el boton buscar
    searchBtn.addEventListener("click",search);

    /*
    // Funcion para guardar producto cuando se hace click en el boton guardar
    saveBtn.addEventListener("click",addProduct);
    // Funcion para editar producto cuando se hace click en el boton editar
    //editBtn.addEventListener("click", saveEdit);*/

    fetchAndList();
}

//Cargar la lista y renderizarla

function fetchAndList(){
    const request = new Request(api+"/cargar", {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        const data = await response.json();
        console.log(data); // Verificar la respuesta
        state.clientes = data;
        render_list();
        console.log(state.clientes);
    })();
}

function render_list() {
    var listado = document.querySelector('#listaClientes tbody');
    listado.innerHTML = "";
    state.clientes.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    var tr = document.createElement("tr");
    tr.innerHTML = `
    <td class='cedula'>${item.cedula}</td>
    <td class='nombre'>${item.nombre}</td>
    <td class='correo'>${item.correo}</td>
    <td class='telefono'>${item.telefono}</td>
    <td class='edit'><img src='/images/edit.png'></td>    
    <td class='remove'><img src='/images/delete.png'></td>
    `;
    // Asignar eventos a los botones de editar y eliminar
    tr.querySelector(".edit").addEventListener("click", function () {
        showFormEdit();
    });
    tr.querySelector(".remove").addEventListener("click", function () {
        remove(item.codigo);
    });
    // Agregar el <tr> al listado
    listado.appendChild(tr);
}

//Funciones del CRUD con Servidor

function add(){
    load_item();
    if(!validate_item()) return;
    let request = new Request(api + "/create", {method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify(state.cliente)});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        NoshowForm();
        await fetchAndList();
        console.log("Contenido de la lista:");
        console.log(state.productos);
    })();
}

function edit(){
    //load_item();
    if(!validate_item()) return;
    let request = new Request(api+`/edit/${state.cliente.codigo}`, {method: 'POST',
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
    let request = new Request(backend+`/delete/${id}`,
        {method: 'DELETE', headers: {}});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        fetchAndList();
    })();
}

function search(){
    nombreBusqueda = document.getElementById("busqueda").value;
    const request = new Request(api+`/search?nombre=${nombreBusqueda}`,
        {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        state.list = await response.json();
        render_list();
    })();
}

//Funciones del CRUD con JS

function addProduct() {
    // Obtener los valores del formulario
    var nuevoCedula = document.getElementById('cedula').value;
    var nuevoNombre = document.getElementById('nombre').value;
    var nuevoCorreo = document.getElementById('correo').value;
    var nuevoTelefono = document.getElementById('telefono').value;

    // Crear un nuevo producto
    var nuevoCliente = {
        cedula: nuevoCedula,
        nombre: nuevoNombre,
        correo:nuevoCorreo,
        telefono:nuevoTelefono
    };

    // Agregar el nuevo producto a la lista de productos
    state.clientes.push(nuevoCliente);

    // Cerrar el formulario y volver a renderizar la lista
    NoshowForm();
    render_list();
}

function removeFromList(cedula) {
    state.clientes = state.clientes.filter(cliente => cliente.cedula !== cedula);
    render_list();
}


//Validaciones

function validate_item(){
    var error=false;
    document.querySelectorAll('input').forEach( (i)=> {i.classList.remove("invalid");});

    if (state.cliente.cedula.length===0){
        document.querySelector("#cedula").classList.add("invalid");
        error=true;
    }
    if (state.cliente.nombre.length===0){
        document.querySelector("#nombre").classList.add("invalid");
        error=true;
    }
    if (state.cliente.correo.length===0){
        document.querySelector("#correo").classList.add("invalid");
        error=true;
    }
    if (state.cliente.telefono.length===0){
        document.querySelector("#telefono").classList.add("invalid");
        error=true;
    }
    return !error;
}


function load_item(){
    state.cliente={
        cedula:document.getElementById("cedula").value,
        nombre:document.getElementById("nombre").value,
        correo: document.getElementById("correo").value,
        telefono: document.getElementById("telefono").value
    };
}

//Cambios en el html

function showForm() {
    //Hacer que el state.producto este vacio
    state.cliente={cedula:"", nombre:"",correo:"",telefono:""};

    // Mostrar el popup
    const popup = document.querySelector(".popup");
    popup.classList.add("active");

    //Hacer que los campos esten vacios
    document.getElementById('cedula').value = '';
    document.getElementById('cedula').readOnly = false;
    document.getElementById('nombre').value = '';
    document.getElementById('correo').value = '';
    document.getElementById('telefono').value = '';

    // Mostrar el botón de guardar y ocultar el de editar
    document.getElementById('editarProductoBtn').classList.add('invisible');
    document.getElementById('guardarProductoBtn').classList.remove('invisible');
}

function showFormEdit() {
    // Mostrar el popup
    const popup = document.querySelector(".popup");
    popup.classList.add("active");

    // Cargar los valores del producto en el formulario
    document.getElementById('cedula').value = item.cedula;
    document.getElementById('cedula').readOnly = true;
    document.getElementById('nombre').value = item.nombre;
    document.getElementById('correo').value = item.correo;
    document.getElementById('telefono').value = item.telefono;

    // Mostrar el botón de editar y ocultar el de guardar
    document.getElementById('guardarProductoBtn').classList.add('invisible');
    document.getElementById('editarProductoBtn').classList.remove('invisible');
}

function NoshowForm() {
    const popup = document.querySelector(".popup");
    popup.classList.remove("active");
}
