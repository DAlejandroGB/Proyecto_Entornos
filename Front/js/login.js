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