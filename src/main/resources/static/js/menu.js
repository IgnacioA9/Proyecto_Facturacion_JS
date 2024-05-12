var backend="http://localhost:8080/api";
var api_login=backend+'/login';

var loginstate ={
    logged: false,
    user : {id:"", rol:""}
}

async function checkuser(){
    let request = new Request(api_login+'/current-user', {method: 'GET'});
    const response = await fetch(request);
    if (response.ok) {
        loginstate.logged = true;
        loginstate.user = await response.json();
    }
    else {
        loginstate.logged = false;
    }
}

async function menu(){
    await checkuser();
    if (!loginstate.logged
        && document.location.pathname != "/pages/login/view.html") {
        document.location = "/pages/login/view.html";
        throw new Error("Usuario no autorizado");
    }
    render_menu();
}

function render_menu() {
    if (!loginstate.logged) {
        html = `
           <img src="/images/logo.png" class="logo"> 
           <div class="navbar"> 
           <div class="dropdown"> 
            <a id="loginlink" href="#">Login</a>
           </div>  
           <div class="dropdown">
            <a href="#">Acerca de</a>
           </div>
            </div>
        `;
        document.querySelector('#menu').innerHTML = html;
        document.querySelector("#menu #loginlink").addEventListener('click', ask);

        //render_loginoverlay();
        //render_loginview();
    }
    else {
        if (loginstate.user.rol === 'ADM'){
            html = `
            <img src="/images/logo.png" class="logo">
            <div class="navbar"> 
                <div class="dropdown">
                    <a id="facturarlink" href="#">Facturar</a>
                </div>
                <div class="dropdown">
                    <a id="clienteslink" href="#">Clientes</a>
                </div>
                <div class="dropdown">
                    <a id="productoslink" href="#">Productos</a>
                </div>
                <div class="dropdown">
                    <a id="facturaslink" href="#">Facturas</a>
                </div>
                <div class="dropdown">
                    <p>&nbsp &nbsp ${loginstate.user.id}</p>
                    <div class="dropdown-content">
                        <a id="profilelink" href="#">Personalizar perfil</a>
                        <a id="logoutlink" href="#">Cerrar Sesion</a>
                    </div>
                </div>
            </div>
            `;
        }else {
            html = `
            <img src="/images/logo.png" class="logo">
            <div class="navbar"> 
               <div class="dropdown">
                  <a id="proveedoreslink" href="#">Proveedores</a>
               </div>
               <div class="dropdown">
                  <a id="solicitudeslink" href="#">Solicitudes</a>
               </div>
               <div class="dropdown">
                    <p>&nbsp &nbsp ${loginstate.user.id}</p>
                    <div class="dropdown-content">
                        <a id="profilelink" href="#">Personalizar perfil</a>
                        <a id="logoutlink" href="#">Cerrar Sesion</a>
                    </div>
                </div>
            </div>
            `;
        }
        document.querySelector('#menu').innerHTML = html;
        document.querySelector("#menu #logoutlink").addEventListener('click', logout);
        document.querySelector("#menu #profilelink").addEventListener('click', e => {
            document.location = "/pages/personalizar/View.html";
        });

        //links de proveedores
        document.querySelector("#menu #facturarlink").addEventListener('click', e => {
            document.location = "/pages/facturar/View.html";
        });
        document.querySelector("#menu #clienteslink").addEventListener('click', e => {
            document.location = "/pages/clientes/View.html";
        });
        document.querySelector("#menu #productoslink").addEventListener('click', e => {
            document.location = "/pages/productos/View.html";
        });
        document.querySelector("#menu #facturaslink").addEventListener('click', e => {
            document.location = "/pages/facturas/View.html";
        });

        //links de admin
        document.querySelector("#menu #proveedoreslink").addEventListener('click', e => {
            document.location = "/pages/proveedores/View.html";
        });
        document.querySelector("#menu #solicitudeslink").addEventListener('click', e => {
            document.location = "/pages/solicitudes/View.html";
        });
    }
}

function ask(event){
    event.preventDefault();
    showLogin();
}

function login(){
    let user={id:document.getElementById("id").value,
        password:document.getElementById("password").value
    };
    let request = new Request(api_login+'/login', {method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify(user)});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        document.location="/pages/facturar/view.html";
    })();
}

function logout(event){
    event.preventDefault();
    let request = new Request(api_login+'/logout', {method: 'POST'});
    (async ()=>{
        const response = await fetch(request);
        if (!response.ok) {errorMessage(response.status);return;}
        document.location="/pages/login/view.html";
    })();
}

function errorMessage(status,place){
    switch(status){
        case 404: error= "Registro no encontrado"; break;
        case 409: error="Registro duplicado"; break;
        case 401: error="Usuario no autorizado"; break;
        case 403: error="Usuario no tiene derechos"; break;
    }
    window.alert(error);
}