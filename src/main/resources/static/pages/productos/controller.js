var api=backend+'/productos';

var state ={
    list: new Array(),
    item : {codigo:"", nombre:"",precio:""},
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

function showForm() {
    document.querySelector(".popup").classList.add("active");
    document.querySelector(".popup .close-btn").addEventListener("click", function() {
        document.querySelector(".popup").classList.remove("active");
    });
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
    var listado=document.getElementById("list");
    listado.innerHTML="";
    state.list.forEach( item=>render_list_item(listado,item));
}

function render_list_item(listado,item){
    var tbody = listado.getElementsByTagName('tbody')[0];

    state.list.forEach(function(producto) {
        var row = document.createElement('tr');

        var codigoCell = document.createElement('td');
        codigoCell.textContent = producto.codigo;

        var nombreCell = document.createElement('td');
        nombreCell.textContent = producto.nombre;

        var precioCell = document.createElement('td');
        precioCell.textContent = producto.precio;

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
            ocultarBotonGuardar();
            mostrarBotonEditar();
            editarProducto(fila);
        });
        editCell.appendChild(editButton);

        row.appendChild(codigoCell);
        row.appendChild(nombreCell);
        row.appendChild(precioCell);
        row.appendChild(editCell);
        row.appendChild(deleteCell);

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
        render_list();
    })();
}

function ask(){
    empty_item();
    showForm();
    state.mode="ADD";
    render_item()
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

function add(){
    load_item();
    if(!validate_item()) return;
    let request = new Request(api, {method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify(state.item)});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        toggle_itemview();
        fetchAndList();
    })();
}

function load_item(){
    state.item={
        codigo:document.getElementById("codigo").value,
        nombre:document.getElementById("nombre").value,
        precio: document.getElementById("precio").value
    };
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

function edit(id){
    let request = new Request(backend+`/productos/${id}`,
        {method: 'GET', headers: {}});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        state.item = await response.json();
        toggle_itemview();
        state.mode="EDIT";
        render_item();
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