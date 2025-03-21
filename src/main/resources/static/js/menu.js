const backend = "http://localhost:8080/api";
const api_login = `${backend}/login`;

// Initialize menu on page load
document.addEventListener('DOMContentLoaded', menu);

const loginstate = {
    logged: false,
    user: { id: "", rol: "" },
};

var state = {
    proveedores: [],
    provedor: {cedula: "", nombre: "", correo: "", telefono: "", estado: ""},

    productos: [],
    producto: {codigo: "", nombre: "", precio: ""},

    clientes: [],
    cliente: {cedula: "", nombre: "", correo: "", telefono: ""},

    proveedorU: {cedula: "", nombre: "", correo: "", telefono: "", estado: ""},

    administrador: {nombre: ""},

    facturas: [],
    factura: { numero: "", cantidad: "", monto: "", fecha: "", contiene: [], cliente: { cedula: "", nombre: "", correo: "", telefono: "" }
    }
};

async function checkUser() {
    try {
        const request = new Request(`${api_login}/current-user`, { method: 'GET' });
        const response = await fetch(request);
        if (response.ok) {
            loginstate.logged = true;
            loginstate.user = await response.json();
        } else {
            loginstate.logged = false;
        }
    } catch (error) {
        console.error('Error checking user:', error);
    }
}

async function menu() {
    await checkUser();
    /*if (!loginstate.logged && document.location.pathname !== "/pages/login/view.html") {
        document.location = "/pages/login/view.html";
        throw new Error("Usuario no autorizado");
    }*/
    renderMenu();
}

function renderMenu() {
    let html = '';
    if (!loginstate.logged) {
        html = `
           <img src="/images/logo.png" class="logo"> 
           <div class="navbar"> 
               <div class="dropdown">
               <a id="homelink" href="#">Bienvenida</a>
                     <div class="dropdown-content">
                        <a id="loginlink" href="#">Login</a>        
                     </div>
               </div>
               <div class="dropdown"> 
                    <a id="registerlink" href="#">Registrarse</a>
               </div>  
            </div>
        `;
        document.querySelector('#menu').innerHTML = html;
        document.querySelector("#menu #loginlink").addEventListener('click', ask);
        document.querySelector("#menu #registerlink").addEventListener('click', e => {
            document.location = "/pages/create/View.html";
        });
        document.querySelector("#menu #homelink").addEventListener('click', e => {
            document.location = "/pages/login/View.html";
        });
    } else {
        if (loginstate.user.rol === 'PROVEE') {
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
                        <p>${loginstate.user.identificacion}</p>
                        <div class="dropdown-content">
                            <a id="profilelink" href="#">Perfil</a>
                            <a id="logoutlink" href="#">Logout</a>
                        </div>
                    </div>
                </div>
            `;
        } else {
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
                        <p>${loginstate.user.identificacion}</p>
                        <div class="dropdown-content">
                            <a id="profilelink" href="#">Perfil</a>
                            <a id="logoutlink" href="#">Logout</a>
                        </div>
                    </div>
                </div>
            `;
        }

        document.querySelector('#menu').innerHTML = html;
        document.querySelector("#menu #logoutlink").addEventListener('click', logout);
        document.querySelector("#menu #profilelink").addEventListener('click', () => {
            document.location = "/pages/personalizar/View.html";
        });

        if (loginstate.user.rol === 'PROVEE') {
            document.querySelector("#menu #facturarlink").addEventListener('click', () => {
                document.location = "/pages/facturar/View.html";
            });
            document.querySelector("#menu #clienteslink").addEventListener('click', () => {
                document.location = "/pages/clientes/View.html";
            });
            document.querySelector("#menu #productoslink").addEventListener('click', () => {
                document.location = "/pages/productos/View.html";
            });
            document.querySelector("#menu #facturaslink").addEventListener('click', () => {
                document.location = "/pages/facturas/View.html";
            });
        } else {
            document.querySelector("#menu #proveedoreslink").addEventListener('click', () => {
                document.location = "/pages/proveedores/View.html";
            });
            document.querySelector("#menu #solicitudeslink").addEventListener('click', () => {
                document.location = "/pages/solicitudes/View.html";
            });
        }
    }
}

function ask(event) {
    event.preventDefault();
    showLogin();
}

async function login() {
    const user = {
        identificacion: document.getElementById("id").value,
        contrasena: document.getElementById("password").value
    };

    if (!user.identificacion || !user.contrasena) {
        errorMessage('Please fill out both fields');
        return;
    }

    const request = new Request(`${api_login}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    });

    try {
        const response = await fetch(request);
        if (!response.ok) {
            errorMessage(response.status);
            return;
        }

        const data = await response.json();
        loginstate.logged = true;
        loginstate.user.id = user.identificacion;
        loginstate.user.rol = data.rol;

        switch (loginstate.user.rol) {
            case "ADMIN":
                document.location = "/pages/proveedores/View.html";
                break;
            case "PROVEE":
                document.location = "/pages/facturar/View.html";
                break;
            default:
                document.location = "/pages/login/view.html";
                break;
        }
    } catch (error) {
        console.error('Error en la autenticación:', error);
        errorMessage('Error en la autenticación');
    }
}

async function logout(event) {
    event.preventDefault();
    const request = new Request(`${api_login}/logout`, { method: 'POST' });
    try {
        const response = await fetch(request);
        if (!response.ok) {
            errorMessage(response.status);
            return;
        }
        sessionStorage.clear();
        loginstate.logged = false;
        document.location = "/pages/login/view.html";
    } catch (error) {
        console.error('Error logging out:', error);
        errorMessage('Error logging out');
    }
}

function errorMessage(status) {
    let error;
    switch (status) {
        case 404: error = "Registro no encontrado"; break;
        case 409: error = "Registro duplicado"; break;
        case 401: error = "Usuario no autorizado"; break;
        case 403: error = "Usuario no tiene derechos"; break;
        case 500: error = "Error al procesar la solicitud"; break;
        default: error = "Error desconocido"; break;
    }
    window.alert(error);
}
