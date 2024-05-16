var api=backend+'/proveedores';


document.addEventListener("DOMContentLoaded",loaded);
document.addEventListener('visibilitychange',unloaded);

/*async function loaded(event){
    try{ await menu();} catch(error){return;}
    renderEdit();
}*/

async function loaded(event) {
    try {
        await menu();
        renderEdit();
    } catch (error) {
        console.error('Error loading menu:', error);
        return;
    }
    state_json = sessionStorage.getItem("variables")
    if (!state_json){
        renderEdit();
    }else{
        state = JSON.parse(state_json);
        renderEdit();
    }
}

async function unloaded(event){
    if (document.visibilityState==="hidden" && loginstate.logged){
        sessionStorage.setItem("variables",JSON.stringify(state));
    }
}


function renderEdit() {
    var divPer = document.getElementById("personalizarDiv");
    var divPer2 = document.getElementById("personalizarDiv2");

    //Agregar elementos al divPer
    if (loginstate.user.rol === "PROVEE") {
        divPer.innerHTML = `
            <h2>Personalizar Perfil Proveedor</h2>
            <div class="input-personalizar">
                <input type="text" name="nombre" id="nombre" placeholder="Nombre" required>
                <i class='bx bxs-face'></i>
            </div>
            <div class="input-personalizar">
                <p> </p>
                <input type="text" name="correo" id="correo" placeholder="Correo" required>
                <i class='bx bxs-envelope'></i>
                <br>
            </div>
            <div class="input-personalizar">
                <input type="text" name="telefono" id="telefono" placeholder="Telefono" required>
                <i class='bx bxs-phone'></i>
            </div>
            <button type="submit" id="guardarBtn" value="Guardar" class="btn">Guardar</button>
        `;
        document.getElementById('guardarBtn').addEventListener('click', function() {
            editarProveedor();
        });
    }else {
        divPer.innerHTML = `
            <h2>Personalizar Perfil Admin</h2>
            <div class="input-personalizar">
                <input type="text" name="nombre" id="nombreAdm" placeholder="Nombre" required>
                <i class='bx bxs-face'></i>
            </div>
            <button type="submit" id="guardarBtn" value="Guardar" class="btn">Guardar</button>
        `;
        document.getElementById('guardarBtn').addEventListener('click', function() {
            editarAdministrador();
        });
    }
    //Agregar elementos al divPer2
    if (loginstate.user.rol === 'PROVEE') {
        divPer2.innerHTML = `
        <h2>Perfil Actual</h2>
            <div class="input-personalizar">
                <input type="text" name="nombre" id="nombreProvee" placeholder="Nombre" value="${state.proveedorU.nombre}" readonly>
                <i class='bx bxs-face'></i>
            </div>
            <div class="input-personalizar">
                <input type="text" name="correo" id="correoProvee" placeholder="Correo" value="${state.proveedorU.correo}" readonly>
                <i class='bx bxs-envelope' ></i>
            </div>
            <div class="input-personalizar">
                <input type="text" name="telefono" id="telefonoProvee" value="${state.proveedorU.telefono}" readonly>
                <i class='bx bxs-phone'></i>
            </div>
        `;
    }else {
        divPer2.innerHTML = `
            <h2>Perfil Actual</h2>
            <div class="input-personalizar">
                <input type="text" name="nombre" id="nombrepAdmin" value="${state.administrador.nombre}" placeholder="Nombre" readonly>
                <i class='bx bxs-face'></i>
            </div>
        `;
    }
}

/*function editarProvedor() {
    loaditem();
    //item.estado = true; esto se cambia en el server
    let request = new Request(api , {
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

function editarProveedor() {
    state.proveedorU.nombre = document.getElementById("nombre").value,
    state.proveedorU.correo = document.getElementById("correo").value,
    state.proveedorU.telefono = document.getElementById("telefono").value
    renderEdit();
}


function editarAdministrador() {
    state.administrador.nombre = document.getElementById("nombreAdm").value;
    renderEdit();
}
