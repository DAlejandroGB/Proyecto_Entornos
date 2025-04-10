const API_URL = 'http://localhost:8080/usuarios';
const form = document.getElementById('usuario-form');
const cancelButton = document.getElementById('cancelar-edicion');
const tableBody = document.getElementById('usuario-table-body');
const token = localStorage.getItem("token");
if (!token) {
    alert('Token no encontrado. Por favor, inicia sesión nuevamente.');
    window.location.href = '/index.html'; // redirige al login
}
console.log('Token:', token); // Esto debería imprimir el token o `null` si no está presente.


let editandoId = null;

document.addEventListener('DOMContentLoaded', cargarUsuarios);

form.addEventListener('submit', async (e) => {
    e.preventDefault();

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
        if (editandoId) {
            console.log('Editando usuario:', usuario);
            response= await fetch(`${API_URL}/${editandoId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(usuario)
            });
            const result = await response.json();
            console.log('Resultado de la edición:', result);

        } else {
            console.log('Agregando usuario:', usuario);
            response = await fetch(`${API_URL}/`,{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(usuario)
            });
        }

        // Verifica si la respuesta es correcta (status 2xx)
        if (response.ok) {
            const result = await response.json();
            console.log('Resultado de la solicitud:', result);
            form.reset();
            cancelButton.style.display = 'none';
            editandoId = null;
            document.getElementById('form-title').textContent = 'Agregar Usuario';
            cargarUsuarios(); // Refresca la lista
        } else {
            console.error('Error en la respuesta:', response.statusText);
            alert('Error al procesar la solicitud.');
        }

    } catch (error) {
        console.error('Error enviando usuario:', error);
        alert('Error enviando usuario. Por favor, inténtalo de nuevo más tarde.');
    }
});

cancelButton.addEventListener('click', () => {
    form.reset();
    cancelButton.style.display = 'none';
    editandoId = null;
    document.getElementById('form-title').textContent = 'Agregar Usuario';
});

async function cargarUsuarios() {
    try {
        const res = await fetch(`${API_URL}/list`,{
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const usuarios = await res.json();

        console.log('Usuarios:', usuarios);

        tableBody.innerHTML = '';
        usuarios.forEach(u => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
            <td>${u.id}</td>
            <td>${u.nombre}</td>
            <td>${u.apellido}</td>
            <td>${u.email}</td>
            <td>${u.telefono || ''}</td>
            <td>${u.direccion || ''}</td>
            <td>${u.rol}</td>
            <td>
                <button onclick="editarUsuario(${u.id})">Editar</button>
                <button onclick="eliminarUsuario(${u.id})" class="btn-danger">Eliminar</button>
            </td>
        `;
            tableBody.appendChild(fila);
        });
    } catch (error) {
        console.error('Error cargando usuarios:', error);
        alert('Error cargando usuarios. Por favor, inténtalo de nuevo más tarde.');
    }
}

async function editarUsuario(id) {
    const res = await fetch(`${API_URL}/list/${id}`,{
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    const u = await res.json();

    form.nombre.value = u.nombre;
    form.apellido.value = u.apellido;
    form.email.value = u.email;
    form.telefono.value = u.telefono;
    form.direccion.value = u.direccion;
    form.rol.value = u.rol;
    editandoId = u.id;

    cancelButton.style.display = 'inline-block';
    document.getElementById('form-title').textContent = 'Editar Usuario';
}

async function eliminarUsuario(id) {
    if (!confirm('¿Estás seguro de eliminar este usuario?')) return;

    await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    cargarUsuarios();
}
