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
    const request = new Request(api + `/cargar`, {method: 'GET', headers: { }});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        const data = await response.json();
        console.log(data); // Verificar la respuesta

        // Aquí asumimos que `data` es un array de FacturaClienteDTO
        state.facturas = data.map(item => ({
            numero: item.factura.numero,
            cantidad: item.factura.cantidad,
            monto: item.factura.monto,
            fecha: item.factura.fecha,
            contiene: item.contiene || [],
            cliente: {
                cedula: item.cliente.cedula,
                nombre: item.cliente.nombre,
                correo: item.cliente.correo,
                telefono: item.cliente.telefono
            }
        }));
        render_list();
        console.log(state.facturas);
    })();
}

function render_list() {
    // Asingar el nombre del proveedor a la tabla
    const proveedorSpan = document.getElementById("proveedor");
    proveedorSpan.textContent = " " + loginstate.user.id;
    var listado = document.querySelector('#listaFacturas tbody');
    listado.innerHTML = "";
    state.facturas.forEach(item => renderListItem(listado, item));
}

function renderListItem(listado, item) {
    var tr = document.createElement("tr");
    tr.innerHTML = `
    <td class='numero'>${item.numero}</td>
    <td class='cantidadT'>${item.cantidad}</td>
    <td class='monto'>${item.monto}</td>
    <td class='fecha'>${item.fecha}</td>
    <td class='XML'><img src='/images/XML.png'></td>    
    <td class='PDF'><img src='/images/PDF.png'></td>
    `;
    // Asignar eventos a los botones de XML y PDF
    tr.querySelector(".XML").addEventListener("click", function () {
        //renderXML(item);
        downloadXML(item);  //El profe menciono que este metodo estaba mejor
    });
    tr.querySelector(".PDF").addEventListener("click", function () {
        downloadPDF(item);
    });
    // Agregar el <tr> al listado
    listado.appendChild(tr);
}

// Función XML
function downloadPDF(factura) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // Añadir el título
    doc.setFontSize(18);
    doc.text('Impresión de la Factura', 105, 10, null, null, 'center');

    // Añadir detalles de la factura
    doc.setFontSize(12);
    doc.text(`Factura Nº: ${factura.numero}`, 10, 20);
    doc.text(`Fecha: ${factura.fecha}`, 10, 30);
    doc.text(`Cliente: ${factura.cliente.nombre}`, 10, 40);
    doc.text(`Cédula: ${factura.cliente.cedula}`, 10, 50);
    doc.text(`Correo: ${factura.cliente.correo}`, 10, 60);
    doc.text(`Teléfono: ${factura.cliente.telefono}`, 10, 70);
    doc.text(`Monto Total: ${factura.monto}`, 10, 80);

    // Añadir encabezado de la tabla
    doc.setFontSize(14);
    doc.text('Productos', 10, 90);

    doc.setFontSize(12);
    doc.text('Código', 10, 100);
    doc.text('Cantidad', 60, 100);

    // Añadir productos como filas de la tabla
    let y = 110;
    factura.contiene.forEach(producto => {
        doc.text(producto.codigo, 10, y);
        doc.text(producto.cantidadproducto.toString(), 60, y);
        y += 10;
    });

    // Pie de página
    const pageCount = doc.internal.getNumberOfPages();
    for (let i = 1; i <= pageCount; i++) {
        doc.setPage(i);
        doc.setFontSize(10);
        doc.text(`Página ${i} de ${pageCount}`, doc.internal.pageSize.width - 20, doc.internal.pageSize.height - 10, null, null, 'right');
        doc.text('Facturación Electrónica', 10, doc.internal.pageSize.height - 10);
    }

    // Guardar el PDF
    doc.save(`Factura_${factura.numero}.pdf`);
}


/*function renderXML(factura) {
    let cliente = factura.cliente;
    let cantidadTotal = factura.cantidad;
    let productosXML = factura.contiene.map(producto => {
        return `\t<Producto>\n\t\t<codigo>${producto.codigo}</codigo>\n\t\t<cantidad>${producto.cantidadproducto}</cantidad>\n\t</Producto>`;
    }).join("\n");

    let contenido = `<Factura>\n\t<numero>${factura.numero}</numero>\n\t<cantidad>${cantidadTotal}</cantidad>\n\t<monto>${factura.monto}</monto>\n\t<fecha>${factura.fecha}</fecha>\n\t<Cliente>\n\t\t<cedula>${cliente.cedula}</cedula>\n\t\t<nombre>${cliente.nombre}</nombre>\n\t\t<correo>${cliente.correo}</correo>\n\t\t<telefono>${cliente.telefono}</telefono>\n\t</Cliente>\n\t<Productos>\n${productosXML}\n\t</Productos>\n</Factura>`;

    // Obtener el elemento pre
    let preElement = document.getElementById("xmlframe");
    // Asignar el contenido XML al elemento pre
    preElement.textContent = contenido;
    // Mostrar la vista XML
    showXMLView();
}*/

function downloadXML(factura) {
    let cliente = factura.cliente;
    let cantidadTotal = factura.cantidad; // Usar la propiedad 'cantidad' de la factura
    let productosXML = factura.contiene.map(producto => {
        return `\t<Producto>\n\t\t<codigo>${producto.codigo}</codigo>\n\t\t<cantidad>${producto.cantidadproducto}</cantidad>\n\t</Producto>`;
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

