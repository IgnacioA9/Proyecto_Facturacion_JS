var api=backend+'/facturas';

document.addEventListener("DOMContentLoaded", loaded);
document.addEventListener('visibilitychange',unloaded);

async function loaded(event) {
    try {
        await menu();
        fetchAndList();
    } catch (error) {
        console.error('Error loading menu:', error);
        return;
    }
    state_json = sessionStorage.getItem("variables")
    if (!state_json){
        fetchAndList();
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
//Cargar la lista y renderizarla

function fetchAndList(){
    /*const request = new Request(api, {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        const data = await response.json();
        console.log(data); // Verificar la respuesta
        state.list = data;
        renderList();
        console.log(state.list);
    })();*/
    render_list();
}

function render_list() {
    var listado = document.querySelector('#listaFacturas tbody');
    listado.innerHTML = "";
    state.facturas.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    var tr = document.createElement("tr");
    tr.innerHTML = `
    <td class='numero'>${item.numero}</td>
    <td class='cantidadT'>${item.cantidadT}</td>
    <td class='monto'>${item.monto}</td>
    <td class='fecha'>${item.fecha}</td>
    <td class='XML'><img src='/images/XML.png'></td>    
    <td class='PDF'><img src='/images/PDF.png'></td>
    `;
    // Asignar eventos a los botones de XML y PDF
    tr.querySelector(".XML").addEventListener("click", function () {
        renderXML(item);
        //downloadXML(item);
    });
    tr.querySelector(".PDF").addEventListener("click", function () {
        console.log("GENERANDO PDF")
    });
    // Agregar el <tr> al listado
    listado.appendChild(tr);
}

// Función XML
function renderXML(factura) {
    let cliente = factura.cliente;
    let cantidadTotal = factura.cantidadT;
    let productosXML = factura.contiene.map(producto => {
        return `\t<Producto>\n\t\t<codigo>${producto.codigo}</codigo>\n\t\t<cantidad>${producto.cantidad}</cantidad>\n\t</Producto>`;
    }).join("\n");

    let contenido = `<Factura>\n\t<numero>${factura.numero}</numero>\n\t<cantidad>${cantidadTotal}</cantidad>\n\t<monto>${factura.monto}</monto>\n\t<fecha>${factura.fecha}</fecha>\n\t<Cliente>\n\t\t<cedula>${cliente.cedula}</cedula>\n\t\t<nombre>${cliente.nombre}</nombre>\n\t\t<correo>${cliente.correo}</correo>\n\t\t<telefono>${cliente.telefono}</telefono>\n\t</Cliente>\n\t<Productos>\n${productosXML}\n\t</Productos>\n</Factura>`;

    // Obtener el elemento pre
    let preElement = document.getElementById("xmlframe");
    // Asignar el contenido XML al elemento pre
    preElement.textContent = contenido;
    // Mostrar la vista XML
    showXMLView();
}

function downloadXML(factura) {
    let cliente = factura.cliente;
    let cantidadTotal = factura.cantidadT;
    let productosXML = factura.contiene.map(producto => {
        return `\t<Producto>\n\t\t<codigo>${producto.codigo}</codigo>\n\t\t<cantidad>${producto.cantidad}</cantidad>\n\t</Producto>`;
    }).join("\n");

    let contenido = `<?xml version="1.0" encoding="UTF-8"?>\n<Factura>\n\t<numero>${factura.numero}</numero>\n\t<cantidad>${cantidadTotal}</cantidad>\n\t<monto>${factura.monto}</monto>\n\t<fecha>${factura.fecha}</fecha>\n\t<Cliente>\n\t\t<cedula>${cliente.cedula}</cedula>\n\t\t<nombre>${cliente.nombre}</nombre>\n\t\t<correo>${cliente.correo}</correo>\n\t\t<telefono>${cliente.telefono}</telefono>\n\t</Cliente>\n\t<Productos>\n${productosXML}\n\t</Productos>\n</Factura>`;

    // Crear un objeto Blob con el contenido XML
    let blob = new Blob([contenido], { type: 'text/xml' });

    // Crear un objeto URL del Blob
    let url = URL.createObjectURL(blob);

    // Crear un elemento 'a' para simular el clic y la descarga
    let link = document.createElement('a');
    link.href = url;
    link.download = 'Factura_'+ factura.numero +'.xml';

    // Simular el clic en el enlace para iniciar la descarga
    link.click();

    // Liberar el objeto URL
    URL.revokeObjectURL(url);
}

function showXMLView() {
    const xml = document.querySelector(".xml");
    const closeBtn = document.querySelector(".close-btn");
    
    xml.classList.add("active");
    // Funcion para cerrar el popup cuando se hace click en el boton de cerrar
    closeBtn.addEventListener("click", function() {
        xml.classList.remove("active");
    });
}

