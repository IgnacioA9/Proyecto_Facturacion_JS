var api=backend+'/proveedores';


document.addEventListener("DOMContentLoaded",loaded);

async function loaded(event){
    //try{ await menu();} catch(error){return;}

    renderEdit();
}

function renderEdit() {
    var divPer = document.getElementById("personalizarDiv");
    var divPer2 = document.getElementById("personalizarDiv2");

    //Agregar elementos al divPer
    if (loginstate.user.rol === 'ADM') {
        divPer.innerHTML = `
            <h2>Personalizar Perfil</h2>
            <div class="input-personalizar">
                <p>Nombre: </p>
                <input type="text" name="nombre" id="nombre">
                <i class='bx bxs-face'></i>
                <br>
            </div>
            <div class="input-personalizar">
                <p>Correo: </p>
                <input type="text" name="correo" id="correo">
                <i class='bx bxs-envelope'></i>
                <br>
            </div>
            <div class="input-personalizar">
                <p>Telefono: </p>
                <input type="text" name="telefono" id="telefono">
                <i class='bx bxs-phone'></i>
            </div>
            <button type="submit" value="Guardar" class="btn">Guardar</button>
        `;
    }else {
        divPer.innerHTML = `
            <h2>Personalizar Perfil</h2>
            <div class="input-personalizar">
                <p>Nombre: </p>
                <input type="text" name="nombre" id="nombreAdm">
            </div>
            <button type="submit" value="Guardar" class="btn">Guardar</button>
        `;
    }
    //Agregar elementos al divPer2
    if (loginstate.user.rol === 'ADM') {
        divPer2.innerHTML = `
        <h2>Perfil Actual</h2>
            <div class="input-personalizar">
                <p>Nombre: </p>
                <input type="text" name="nombre" id="nombreProvee">
                <i class='bx bxs-face'></i>
                <br>
            </div>
            <div class="input-personalizar">
                <p>Correo: </p><input type="text" name="correo" id="correoProvee">
                <i class='bx bxs-envelope' ></i>
                <br>
            </div>
            <div class="input-personalizar">
                <p>Telefono: </p><input type="text" name="telefono" id="telefonoProvee">
                <i class='bx bxs-phone'></i>
            </div>
        `;
    }else {
        divPer2.innerHTML = `
            <h2>Perfil Actual</h2>
            <div class="input-personalizar">
                <p>Nombre: </p><input type="text" name="nombre" id="nombrepAdmin">
            </div>
        `;
    }
}


