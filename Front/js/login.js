const API_URL = 'http://localhost:8080';

async function register(event) {
  event.preventDefault();

  const form = document.getElementById('usuario-form');

  const usuario = {
    nombre: form.nombre.value,
    apellido: form.apellido.value,
    email: form.email.value,
    telefono: form.telefono.value,
    direccion: form.direccion.value,
    rol: form.rol.value
  };

  console.log('Datos enviados:', usuario);

  try {
    let response;
    let result;

    console.log('Agregando usuario:', usuario);
    response = await fetch(`${API_URL}/usuarios/`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(usuario)
    });

    result = await response.json();
    console.log('Resultado de la solicitud:', result);

    // Verifica si la respuesta es correcta (status 2xx)
    if (response.ok) {
      console.log("Usuario Registrado con Exito: ", response.body)
      form.reset();
      localStorage.setItem("usuario", JSON.stringify(result));

      const modal1 = bootstrap.Modal.getInstance(document.getElementById('exampleModalToggle'));
      modal1.hide();

      setTimeout(() => {
        const modal2 = new bootstrap.Modal(document.getElementById('exampleModalToggle2'));
        modal2.show();
      }, 500);
    } else {
      console.error('Error en la respuesta:', response.statusText);
      alert('Error al procesar la solicitud.');
    }

  } catch (error) {
    console.error('Error enviando usuario:', error);
    alert('Error enviando usuario. Por favor, inténtalo de nuevo más tarde.');
  }
}

async function login() {
  const btn = document.getElementById('btnLogin')
  const nombreUsuario = document.getElementById('nombreUsuario').value.trim()
  const password = document.getElementById('password').value.trim()
  if (!nombreUsuario || !password) {
    alert('Completa todos los campos');
    return;
  }
  //Desactivar el boiton para evitar multiples clicks
  btn.disabled = true
  //Pendiente para habilitar login con email o nombre
  const esEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(nombreUsuario);
  try {
    const response = await fetch('http://localhost:8080/usuarios/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        usuarioNombre: nombreUsuario,
        contrasena: password
      })
    });
    console.log(response)
    if (!response.ok) {
      alert(`Login incorrecto: Credenciales no validas `);
      window.location = 'index.html'
      throw new Error(`Error: ${response.status}`);
      
    }

    const data = await response.text();
    console.log('Login exitoso:', data);
    try {
      localStorage.setItem('token', data);
      console.log('Token guardado en localStorage');
    } catch (err) {
      console.error('No se pudo guardar el token:', err);
    }
    alert(`Login exitoso: ${nombreUsuario}`);
    window.location = 'home.html'



  } catch (error) {
    console.error('Error durante el login:', error);
    throw error;
  }
}

async function registrarCredencial(event) {
  event.preventDefault();

  // Obtener el usuario guardado desde localStorage
  const usuarioGuardado = localStorage.getItem("usuario");
  if (!usuarioGuardado) {
    alert("Error: No se encontró el usuario registrado.");
    return;
  }

  const usuario = JSON.parse(usuarioGuardado);

  const credencial = {
    usuario: { id: usuario.id },
    usuarioNombre: document.getElementById("usuarioNombre").value.trim(),
    contrasena: document.getElementById("contrasena").value.trim()
  };

  console.log("Enviando credencial:", credencial);

  try {
    const response = await fetch(`${API_URL}/credencial/`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credencial)
    });

    if (response.ok) {
      console.log("Credencial guardada con éxito.");
      document.getElementById("credencial-form").reset();
      localStorage.removeItem("usuario");

      const modal2 = bootstrap.Modal.getInstance(document.getElementById("exampleModalToggle2"));
      modal2.hide();
    } else {
      console.error("Error al guardar credencial:", await response.text());
      alert("No se pudo guardar la credencial.");
    }

  } catch (error) {
    console.error("Error de red:", error);
    alert("Error al enviar credencial. Intenta de nuevo.");
  }
}

async function deleteIncompleteUser() {
  const usuarioGuardado = localStorage.getItem("usuario");
  if (!usuarioGuardado) {
    console.warn("No hay usuario en localStorage para eliminar.");
    return;
  }

  const usuario = JSON.parse(usuarioGuardado);

  try {
    const response = await fetch(`${API_URL}/usuarios/${usuario.id}`, {
      method: 'DELETE'
    });

    if (response.ok) {
      console.log(`Usuario con ID ${usuario.id} eliminado correctamente.`);
      localStorage.removeItem("usuario");
    } else {
      const errorText = await response.text();
      console.error(`Error al eliminar usuario: ${response.status}`, errorText);
    }
  } catch (error) {
    console.error("Error de red al intentar eliminar usuario:", error);
  }
}
document.addEventListener('DOMContentLoaded', () => {
  setTimeout(() => {
    const form = document.getElementById('usuario-form');
    if (form) {
      form.addEventListener('submit', register);
    } else {
      console.warn('Formulario no encontrado incluso tras DOMContentLoaded + timeout');
    }
  }, 500); // Aumentá el tiempo si sigue fallando
});