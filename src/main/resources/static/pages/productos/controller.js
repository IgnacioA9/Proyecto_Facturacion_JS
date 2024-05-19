var api=backend+'/productos';

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

    // Asingar el nombre del proveedor a la tabla
    const proveedorSpan = document.getElementById("proveedor");
    proveedorSpan.textContent = " " + loginstate.user.id;

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
    searchBtn.addEventListener("click", () => {
        const searchTerm = document.getElementById("busqueda").value;
        search(searchTerm);
    });

    /*
    // Funcion para guardar producto cuando se hace click en el boton guardar
    saveBtn.addEventListener("click",addProduct);
    */
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
        state.productos = data;
        render_list();
        console.log(state.productos);
    })();
}

function render_list() {
    var listado = document.querySelector('#listaProductos tbody');
    listado.innerHTML = "";
    state.productos.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    var tr = document.createElement("tr");
    tr.innerHTML = `
    <td class='codigo'>${item.codigo}</td>
    <td class='nombre'>${item.nombre}</td>
    <td class='precio'>${item.precio}</td>
    <td class='edit'><img src='/images/edit.png'></td>    
    <td class='remove'><img src='/images/delete.png'></td>
    `;
    // Asignar eventos a los botones de editar y eliminar
    tr.querySelector(".edit").addEventListener("click", function () {
        showFormEdit(item);
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
        body: JSON.stringify(state.producto)});
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
    load_item();
    if(!validate_item()) return;
    let request = new Request(api+`/edit`, {method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify(state.producto)});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        NoshowForm();
        fetchAndList();
    })();
}

function remove(id) {
    let request = new Request(api+`/delete/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    (async () => {
        try {
            const response = await fetch(request);
            if (!response.ok) {
                errorMessage(response.status);
                return;
            }
            fetchAndList(); // Asume que esta función actualiza la lista de productos
        } catch (error) {
            console.error('Error al eliminar el producto:', error);
            errorMessage('Error al eliminar el producto'); // Muestra un mensaje de error apropiado
        }
    })();
}

function search(searchTerm) {
    var filteredProducts = state.productos.filter(producto =>
        producto.nombre.toLowerCase().includes(searchTerm.toLowerCase())
    );
    render_filtered_list(filteredProducts);
}

function render_filtered_list(filteredProducts) {
    var listado = document.querySelector('#listaProductos tbody');
    listado.innerHTML = "";
    filteredProducts.forEach(item => renderListItem(listado, item));
}


//Validaciones

function validate_item(){
    var error=false;
    document.querySelectorAll('input').forEach( (i)=> {i.classList.remove("invalid");});

    if (state.producto.codigo.length===0){
        document.querySelector("#codigo").classList.add("invalid");
        error=true;
    }
    if (state.producto.nombre.length===0){
        document.querySelector("#nombre").classList.add("invalid");
        error=true;
    }
    if (state.producto.precio.length===0){
        document.querySelector("#precio").classList.add("invalid");
        error=true;
    }
    return !error;
}

function load_item(){
    state.producto={
        codigo:document.getElementById("codigo").value,
        nombre:document.getElementById("nombre").value,
        precio: document.getElementById("precio").value
    };
}

//Cambios en el html

function showForm() {
    //Hacer que el state.producto este vacio
    state.producto={codigo:"", nombre:"",precio:""};

    // Mostrar el popup
    const popup = document.querySelector(".popup");
    popup.classList.add("active");

    //Hacer que los campos esten vacios
    document.getElementById('codigo').value = '';
    document.getElementById('codigo').readOnly = false;
    document.getElementById('nombre').value = '';
    document.getElementById('precio').value = '';

    // Mostrar el botón de guardar y ocultar el de editar
    document.getElementById('editarProductoBtn').classList.add('invisible');
    document.getElementById('guardarProductoBtn').classList.remove('invisible');
}

function showFormEdit(item) {
    // Mostrar el popup
    const popup = document.querySelector(".popup");
    popup.classList.add("active");

    // Cargar los valores del producto en el formulario
    document.getElementById('codigo').value = item.codigo;
    document.getElementById('codigo').readOnly = true;
    document.getElementById('nombre').value = item.nombre;
    document.getElementById('precio').value = item.precio;

    // Mostrar el botón de editar y ocultar el de guardar
    document.getElementById('guardarProductoBtn').classList.add('invisible');
    document.getElementById('editarProductoBtn').classList.remove('invisible');
}

function NoshowForm() {
    const popup = document.querySelector(".popup");
    popup.classList.remove("active");
}
