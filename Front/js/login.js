async function login() {
    const nombreUsuario = document.getElementById('icon_user').value
    const password = document.getElementById('icon_pass').value
    console.log(nombreUsuario,password)
    try {
      const response = await fetch('http://localhost:8080/user/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          nombreUsuario: nombreUsuario,
          password: password
        })
      });
      console.log(response)
      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
  
      const data = await response.text();
      console.log('Login exitoso:', data);
      alert(`Login exitoso: ${data}`);
      window.location='menu.html'
      
      /*if (data.token) {
        localStorage.setItem('token', data.token);
      }*/
      
      return data;
    } catch (error) {
      console.error('Error durante el login:', error);
      throw error;
    }
  }