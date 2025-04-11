const API_URL = 'http://localhost:8080/api/medicamentos';
const form = document.getElementById('medicamento-form');
const cancelButton = document.getElementById('cancelar-edicion');
const tableBody = document.getElementById('medicamento-table-body');
const token = localStorage.getItem("token");

if (!token) {
    alert('Token no encontrado. Por favor, inicia sesión nuevamente.');
    window.location.href = '/index.html';
}

let editandoId = null;

document.addEventListener('DOMContentLoaded', cargarMedicamentos);

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const medicamento = {
        nombre: form.nombre.value,
        precio: parseFloat(form.precio.value),
        tipo: form.tipo.value
    };

    try {
        let response;
        if (editandoId) {
            response = await fetch(`${API_URL}/${editandoId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(medicamento)
            });
        } else {
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(medicamento)
            });
        }

        if (response.ok) {
            form.reset();
            cancelButton.style.display = 'none';
            editandoId = null;
            document.getElementById('form-title').textContent = 'Agregar Medicamento';
            cargarMedicamentos();
        } else {
            alert('Error al procesar la solicitud.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al procesar la solicitud.');
    }
});

cancelButton.addEventListener('click', () => {
    form.reset();
    cancelButton.style.display = 'none';
    editandoId = null;
    document.getElementById('form-title').textContent = 'Agregar Medicamento';
});

async function cargarMedicamentos() {
    try {
        const res = await fetch(API_URL, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const medicamentos = await res.json();

        tableBody.innerHTML = '';
        medicamentos.forEach(m => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${m.id}</td>
                <td>${m.nombre}</td>
                <td>${m.precio.toFixed(2)}</td>
                <td>${m.tipo}</td>
                <td>
                    <button class="edit-btn" onclick="editarMedicamento(${m.id})">Editar</button>
                    <button class="delete-btn" onclick="eliminarMedicamento(${m.id})">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(fila);
        });
    } catch (error) {
        console.error('Error al cargar medicamentos:', error);
        alert('Error al cargar medicamentos.');
    }
}

async function editarMedicamento(id) {
    try {
        const res = await fetch(`${API_URL}/${id}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const m = await res.json();

        form.nombre.value = m.nombre;
        form.precio.value = m.precio;
        form.tipo.value = m.tipo;
        editandoId = m.id;

        cancelButton.style.display = 'inline-block';
        document.getElementById('form-title').textContent = 'Editar Medicamento';
    } catch (error) {
        console.error('Error al editar medicamento:', error);
        alert('Error al obtener los datos del medicamento.');
    }
}

async function eliminarMedicamento(id) {
    if (!confirm('¿Estás seguro de eliminar este medicamento?')) return;

    try {
        await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        cargarMedicamentos();
    } catch (error) {
        console.error('Error al eliminar medicamento:', error);
        alert('Error al eliminar el medicamento.');
    }
}
