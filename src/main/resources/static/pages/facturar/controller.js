var api = backend + '/facturas';

document.addEventListener("DOMContentLoaded", loaded);
document.addEventListener('visibilitychange', unloaded);

async function loaded(event) {
    try {
        await menu();
        setupEventListeners();
    } catch (error) {
        console.error('Error loading menu:', error);
        return;
    }
    state_json = sessionStorage.getItem("variables")
    if (!state_json) {
        setupEventListeners();
    } else {
        state = JSON.parse(state_json);
        render_list();
    }
}

async function unloaded(event) {
    if (document.visibilityState === "hidden" && loginstate.logged) {
        sessionStorage.setItem("variables", JSON.stringify(state));
    }
}

function setupEventListeners() {
    const proveedorSpan = document.getElementById("proveedor");
    proveedorSpan.textContent = " " + loginstate.user.id;

    const clienteSpan = document.getElementById("clienteFactura");
    clienteSpan.textContent = state.factura.cliente.nombre;

    const saveBtn = document.getElementById("save");
    // Funcion para guardar factura cuando se hace click en el boton guardar
    saveBtn.addEventListener("click",addFactura);

    // Asigna los href a los enlaces
    document.getElementById("linkListaClientes").href = "/pages/clientes/View.html";
    document.getElementById("linkListaProductos").href = "/pages/productos/View.html";
    document.getElementById("linkBuscarProducto").href = "/pages/buscarProducto/View.html";

    // Asignar funciones a los botones de búsqueda
    document.getElementById("linkBuscarClientes").addEventListener("click", addCliente);
    document.getElementById("linkBuscarProducto").addEventListener("click", addProductos);

    fetchClientes();
    fetchProductos();
}

// Si se intenta buscar un producto o cliente y las variables no están cargadas se llamarán estos métodos

function fetchClientes() {
    const request = new Request(backend + "/clientes/cargar", { method: 'GET', headers: {} });
    (async () => {
        const response = await fetch(request);
        if (!response.ok) { errorMessage(response.status); return; }
        const data = await response.json();
        state.clientes = data;
    })();
}

function fetchProductos() {
    const request = new Request(backend + "/productos/cargar", { method: 'GET', headers: {} });
    (async () => {
        const response = await fetch(request);
        if (!response.ok) { errorMessage(response.status); return; }
        const data = await response.json();
        state.productos = data;
    })();
}

function render_list() {
    const clienteSpan = document.getElementById("clienteFactura");
    const total = document.getElementById("total");
    const ctotal = document.getElementById("ctotal");
    var listado = document.querySelector('#listaFacturas tbody');

    clienteSpan.textContent = state.factura.cliente.nombre;
    total.textContent = state.factura.monto;
    ctotal.textContent = state.factura.cantidad;
    listado.innerHTML = "";


    state.factura.contiene.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    // Buscar el producto completo usando el código
    const producto = state.productos.find(prod => prod.codigo === item.codigo);

    if (producto) {
        var tr = document.createElement("tr");
        tr.innerHTML = `
        <td class='codigo'>${producto.codigo}</td>
        <td class='nombre'>${producto.nombre}</td>
        <td class='precio'>${producto.precio}</td>
        <td class='cantidad'>${item.cantidadP}</td>
        <td class='remove'><img src='/images/delete.png' data-codigo='${producto.codigo}'></td>
        `;
        // Asignar evento al botón eliminar
        tr.querySelector(".remove img").addEventListener("click", function () {
            removeFromFactura(producto.codigo);
        });
        // Agregar el <tr> al listado
        listado.appendChild(tr);
    }
}

function addFactura() {
    let fechaActual = new Date();
    let fechaFormateada = fechaActual.toISOString().split('T')[0];
    state.factura.fecha = fechaFormateada;

    // Construye el objeto DTO correctamente, asegurándote de que los nombres de los atributos coincidan
    let contieneArray = state.factura.contiene.map(item => {
        return {
            codigo: item.codigo,  // Verifica que aquí usas 'codigo' y no 'numeroprod'
            cantidadproducto: item.cantidadP  // Asegúrate de usar 'cantidadproducto'
        };
    });

    let facturaClienteDTO = {
        factura: state.factura,
        cliente: state.factura.cliente,
        contiene: contieneArray
    };

    console.log(JSON.stringify(facturaClienteDTO, null, 2)); // Imprime el objeto DTO para verificar

    let request = new Request(api + "/create", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(facturaClienteDTO)
    });

    (async () => {
        const response = await fetch(request);
        if (!response.ok) {
            errorMessage(response.status);
            return;
        }
    })();

    clearFactura();
    render_list();
}



function addCliente() {
    var cedula = document.getElementById("clienteFacturas").value;
    const cliente = state.clientes.find(cliente => cliente.cedula === cedula);

    if (cliente) {
        state.factura.cliente = { ...cliente };
        console.log(`Cliente agregado: ${JSON.stringify(cliente)}`);
        console.log(state.factura);
        document.getElementById("clienteFacturas").value = "";
        render_list();
    } else {
        console.log(`Cliente con cédula ${cedula} no encontrado.`);
        window.alert("Cliente no encontrado.");
    }
}

function addProductos() {
    var codigo = document.getElementById("productoFacturas").value;
    const producto = state.productos.find(producto => producto.codigo === codigo);

    if (producto) {
        const item = state.factura.contiene.find(item => item.codigo === codigo);

        if (item) {
            item.cantidadP += 1;
        } else {
            state.factura.contiene.push({ codigo: producto.codigo, cantidadP: 1 });
        }

        state.factura.cantidad = state.factura.contiene.reduce((total, item) => total + item.cantidadP, 0);

        // Actualizar el monto total de la factura
        state.factura.monto = state.factura.contiene.reduce((total, item) => {
            const prod = state.productos.find(prod => prod.codigo === item.codigo);
            return total + (prod.precio * item.cantidadP);
        }, 0).toFixed(2);

        console.log(`Producto agregado: ${JSON.stringify(producto)}`);
        console.log(`Factura actualizada: ${JSON.stringify(state.factura)}`);
        document.getElementById("productoFacturas").value = "";
        render_list();

    } else {
        console.log(`Producto con código ${codigo} no encontrado.`);
        window.alert("Producto no encontrado.");
    }
}

function removeFromFactura(codigo) {
    const index = state.factura.contiene.findIndex(item => item.codigo === codigo);

    if (index !== -1) {
        const cantidad = state.factura.contiene[index].cantidadp;
        const producto = state.productos.find(prod => prod.codigo === codigo);

        // Si la cantidad es mayor a 1, reducir la cantidad en 1
        if (cantidad > 1) {
            state.factura.contiene[index].cantidadP -= 1;
            state.factura.monto = (parseFloat(state.factura.monto) - parseFloat(producto.precio)).toFixed(2);
            state.factura.cantidad -= 1;
        } else {
            state.factura.contiene.splice(index, 1);
            state.factura.monto = (parseFloat(state.factura.monto) - (parseFloat(producto.precio) * cantidad)).toFixed(2);
            state.factura.cantidad -= cantidad;
        }
    }
    render_list();
}

function clearFactura() {
    state.factura = {
        numero: "",
        cantidad: "",
        monto: "",
        fecha: "",
        contiene: [],
        cliente: {
            cedula: "",
            nombre: "",
            correo: "",
            telefono: ""
        }
    };
}


