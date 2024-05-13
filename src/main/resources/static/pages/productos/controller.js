var api=backend+'/productos';

var state ={
    list: new Array(),
    item : {codigo:"", nombre:"",precio:""},
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

function fetchAndList(){
    const request = new Request(api, {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        state.list = await response.json();
        render_list();
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

function render_list_item(item, tbody){
    var row = document.createElement('tr');

    var codigoCell = document.createElement('td');
    codigoCell.textContent = item.codigo;

    var nombreCell = document.createElement('td');
    nombreCell.textContent = item.nombre;

    var precioCell = document.createElement('td');
    precioCell.textContent = item.precio;

    var deleteCell = document.createElement('td');
    var deleteButton = document.createElement('button');
    deleteButton.textContent = 'Eliminar';
    deleteButton.classList.add("btnAction");
    deleteButton.addEventListener('click', function () {
        row.remove();
    });
    deleteCell.appendChild(deleteButton);

    var editCell = document.createElement('td');
    var editButton = document.createElement('button');
    editButton.textContent = "Editar";
    editButton.classList.add("btnAction");
    editButton.addEventListener('click', function() {
        var fila = this.parentNode.parentNode;
        llenarCampos(fila);
        load_item();
        showForm();
        ocultarBotonGuardar();
        mostrarBotonEditar();
    });
    editCell.appendChild(editButton);

    row.appendChild(codigoCell);
    row.appendChild(nombreCell);
    row.appendChild(precioCell);
    row.appendChild(editCell);
    row.appendChild(deleteCell);

    // Agregar la fila a tbody
    tbody.appendChild(row);
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

function ask(){
    limpiarCampos();
    empty_item();
    showForm();
    state.mode="ADD";
    ocultarBotonEditar();
    mostrarBotonGuardar();
}

function toggle_itemview(){
    document.getElementById("itemoverlay").classList.toggle("active");
    document.getElementById("itemview").classList.toggle("active");
}

function empty_item(){
    state.item={codigo:"", nombre:"",precio:""};
}

function render_item(){
    document.querySelectorAll('#itemview input').forEach( (i)=> {i.classList.remove("invalid");});
    document.getElementById("codigo").value = state.item.cedula;
    document.getElementById("nombre").value = state.item.nombre;
    document.getElementById("precio").value = state.item.precio;

    if(state.mode=="ADD"){
        document.querySelector("#itemview #registrar").hidden=false;
    }
    else{
        document.querySelector("#itemview #registrar").hidden=true;
    }
}

function load_item(){
    state.item={
        codigo:document.getElementById("codigo").value,
        nombre:document.getElementById("nombre").value,
        precio: document.getElementById("precio").value
    };
    console.log("Valores del objeto state.item:");
    console.log("Código:", state.item.codigo);
    console.log("Nombre:", state.item.nombre);
    console.log("Precio:", state.item.precio);
}

function validate_item(){
    var error=false;

    document.querySelectorAll('input').forEach( (i)=> {i.classList.remove("invalid");});

    if (state.item.codigo.length==0){
        document.querySelector("#codigo").classList.add("invalid");
        error=true;
    }

    if (state.item.nombre.length==0){
        document.querySelector("#nombre").classList.add("invalid");
        error=true;
    }

    if (state.item.precio.length==0){
        document.querySelector("#precio").classList.add("invalid");
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
        fetchAndList();
    })();
}

function edit(id){
    let request = new Request(backend+`/productos/${id}`,
        {method: 'GET', headers: {}});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        state.item = await response.json();
        state.mode="EDIT";
        //render_item();
    })();
}

function remove(id){
    let request = new Request(backend+`/productos/${id}`,
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
    var campoCodigo = document.getElementById('codigo');
    campoCodigo.readOnly = true;
}

function habilitarCodigo() {
    var campoCodigo = document.getElementById('codigo');
    campoCodigo.readOnly = false;
}

function llenarCampos(fila){
    deshabilitarCodigo();
    var codigo = fila.querySelector('td:nth-child(1)').textContent;
    var nombre = fila.querySelector('td:nth-child(2)').textContent;
    var precio = fila.querySelector('td:nth-child(3)').textContent;

    document.getElementById('codigo').value = codigo;
    document.getElementById('nombre').value = nombre;
    document.getElementById('precio').value = precio;
}

function limpiarCampos(){
    document.getElementById('codigo').value = '';
    document.getElementById('nombre').value = '';
    document.getElementById('precio').value = '';
}