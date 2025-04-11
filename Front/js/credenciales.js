const API_URL = 'http://localhost:8080/credencial';
const form = document.getElementById('credencial-form');
const cancelButton = document.getElementById('cancelar-edicion');
const tableBody = document.getElementById('credencial-table-body');
const token = localStorage.getItem("token");
if (!token) {
    alert('Token no encontrado. Por favor, inicia sesión nuevamente.');
    window.location.href = '/index.html';
}

let edit = null;

document.addEventListener('DOMContentLoaded', cargarCredenciales);

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const res = await fetch(`http://localhost:8080/usuarios/list/${form.usuarioId.value}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    const user = await res.json();

    try {
        let response;
        if (edit) {
            const credencial = {
                credencialId: edit,
                usuario: user,
                usuarioNombre: form.username.value,
                contrasena: form.password.value
            };
            response = await fetch(`${API_URL}/`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(credencial)
            });
        } else {
            const credencial = {
                usuario: user,
                usuarioNombre: form.username.value,
                contrasena: form.password.value
            };

            response = await fetch(`${API_URL}/`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(credencial)
            });
        }

        const result = await response.json();

        if (response.ok) {
            form.reset();
            cancelButton.style.display = 'none';
            edit = null;
            document.getElementById('form-title').textContent = 'Agregar Credencial';
            cargarCredenciales();
        } else {
            alert('Error al procesar la solicitud.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error enviando credencial. Intenta más tarde.');
    }
});

cancelButton.addEventListener('click', () => {
    form.reset();
    cancelButton.style.display = 'none';
    edit = null;
    document.getElementById('form-title').textContent = 'Agregar Credencial';
});

async function cargarCredenciales() {
    try {
        const res = await fetch(`${API_URL}/list`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const credenciales = await res.json();

        tableBody.innerHTML = '';
        credenciales.forEach(c => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${c.credencialId}</td>
                <td>${c.usuario.id}</td>
                <td>${c.usuarioNombre}</td>
                <td>${c.contrasena}</td>
                <td>${c.fechaCreacion}</td>
                <td>
                    <button class="edit-btn" onclick="editarCredencial(${c.credencialId})">Editar</button>
                    <button class="delete-btn" onclick="eliminarCredencial(${c.credencialId})">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(fila);
        });
    } catch (error) {
        console.error('Error cargando credenciales:', error);
    }
}

async function editarCredencial(id) {
    try {
        const res = await fetch(`${API_URL}/list/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const c = await res.json();
        form.username.value = c.usuarioNombre;
        form.password.value = c.contrasena;
        form.usuarioId.value = c.usuario.id;
        edit = c.credencialId;
        cancelButton.style.display = 'inline-block';
        document.getElementById('form-title').textContent = 'Editar Credencial';
    }
    catch (error) {
        console.error('Error cargando credenciales:', error);
    }

}

async function eliminarCredencial(id) {
    if (!confirm('¿Estás seguro de eliminar esta credencial?')) return;

    await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    cargarCredenciales();
}
function cerrarSesion() {
    localStorage.clear();
    window.location.href = 'index.html'; // o la página de login
}