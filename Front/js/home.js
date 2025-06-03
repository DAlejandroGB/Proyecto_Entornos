let token = null;
let userId = null; // Variable para el ID del usuario
let idOrdenActual = null; // Variable para almacenar el ID de la orden actual
let carrito = [];

document.addEventListener("DOMContentLoaded", () => {
    token = localStorage.getItem("token");
    userId = parseInt(localStorage.getItem("idUsuario")); // ID de usuario fijo

    if (token === null) {
        alert("Debes estar logeado para acceder a esta seccion");
        window.location.href = "index.html";
        return;
    }
    cargarMedicamentos();
    cargarOrdenPendiente();
});


function cargarMedicamentos() {
    fetch("http://localhost:8080/api/medicamentos", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("No se pudo cargar los medicamentos");
            }
            return response.json();
        })
        .then(medicamentos => {
            const listaMedicamentos = document.getElementById("medicamentos-container");
            listaMedicamentos.innerHTML = "";
            medicamentos.forEach(medicamento => {
                console.log(medicamento)
                const tarjeta = document.createElement("div");
                tarjeta.className = "product-card";
                tarjeta.innerHTML = `<img src="images/6408427.png" alt="${medicamento.nombre}" />
            <h3>${medicamento.nombre}</h3>
            <p class="price">$${medicamento.precio}</p>
            <button class="botonMedicamento" data-id="${medicamento.id}" data-precio="${medicamento.precio}">
                Añadir al carrito    
            </button>`;
                listaMedicamentos.appendChild(tarjeta);
            });

            listaMedicamentos.addEventListener("click", eventoBotonMedicamento);
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Hubo un error al cargar los medicamentos.");
        });
}

function cargarOrdenPendiente() {
    fetch("http://localhost:8080/api/orden/ordenPendiente", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
            "idUsuario": userId
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("No hay orden pendiente para el usuario seleccionado");
            }
            return response.json();
        })
        .then(ordenMedicamento => {
            const listaMedicamentos = ordenMedicamento.medicamentos;

            idOrdenActual = ordenMedicamento.idOrden;

            listaMedicamentos.forEach(medicamento => {
                console.log(medicamento)

                const lista = document.getElementById("lista-carrito");
                const item = document.createElement("li");
                item.classList.add("item-carrito");
                item.innerHTML = `
                <div class="info">
                    <strong>${medicamento.nombreMedicamento}</strong>
                    <span class="precio">$${(medicamento.precioMedicamento * medicamento.cantidad).toLocaleString("es-CO")}</span>
                    <p>Cantidad: ${medicamento.cantidad}</p>
                </div>
                <button class="boton-eliminar" data-id="${medicamento.idMedicamento}">Eliminar</button>

            `;

                lista.appendChild(item);

                carrito.push({
                    id: medicamento.idMedicamento,
                    nombre: medicamento.nombreMedicamento,
                    precio: medicamento.precioMedicamento,
                    img: medicamento.imagen,
                    cantidad: medicamento.cantidad
                });

                addActionToDelete(item, medicamento.idMedicamento, medicamento.cantidad);
                actualizarTotal();
            });

        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function eventoBotonMedicamento(evento) {
    if (evento.target.classList.contains("botonMedicamento")) {
        console.log(evento)
        const botonMedicamento = evento.target;
        const idMedicamento = botonMedicamento.dataset.id;
        const tarjeta = botonMedicamento.closest(".product-card");
        const nombre = tarjeta.querySelector("h3").innerText;
        const precioTexto = tarjeta.querySelector("p:nth-child(3)").innerText;
        const precio = parseInt(precioTexto.replace(/[^\d]/g, ""));
        const imgSrc = tarjeta.querySelector("img").src;

        console.log(parseInt(idMedicamento), nombre)
        console.log(carrito)

        // Llamar al servicio único que crea la orden (si no existe) y agrega el medicamento
        agregarMedicamentoAOrden(parseInt(idMedicamento))
            .then(respuesta => {
                // Guardar el ID de la orden para uso futuro en eliminaciones
                if (respuesta && respuesta.idOrden) {
                    idOrdenActual = respuesta.idOrden;
                    console.log("Orden actual ID:", idOrdenActual);
                }


                const existente = carrito.find(m => m.id === parseInt(idMedicamento));
                const indexExistente = carrito.findIndex(m => m.id === parseInt(idMedicamento));
                console.log(carrito, "carro")

                if (existente) {
                    // Si ya está, aumenta la cantidad
                    existente.cantidad += 1;
                    carrito[indexExistente] = existente;

                    // Actualiza la cantidad en el DOM
                    const itemsCarrito = document.querySelectorAll(".item-carrito");
                    itemsCarrito.forEach(item => {
                        if (item.querySelector(".boton-eliminar").dataset.id === idMedicamento) {
                            const cantidadTexto = item.querySelector("p");
                            const precio = item.querySelector(".precio");
                            precio.textContent = `$${(existente.precio * existente.cantidad).toLocaleString("es-CO")}`;
                            cantidadTexto.textContent = `Cantidad: ${existente.cantidad}`;
                            addActionToDelete(item, idMedicamento, existente.cantidad)

                            // Actualiza el total
                            actualizarTotal();
                        }

                    });

                } else {
                    // Si no está, agrégalo al arreglo local
                    carrito.push({
                        id: parseInt(idMedicamento),
                        nombre: nombre,
                        precio: precio,
                        img: imgSrc,
                        cantidad: 1
                    });

                    // Crear el item en el DOM
                    const lista = document.getElementById("lista-carrito");
                    const item = document.createElement("li");
                    item.classList.add("item-carrito");
                    item.innerHTML = `
                    <div class="info">
                        <strong>${nombre}</strong>
                        <span class="precio">$${(precio).toLocaleString("es-CO")}</span>
                        <p>Cantidad: 1</p>
                    </div>
                    <button class="boton-eliminar" data-id="${idMedicamento}">Eliminar</button>
                `;

                    lista.appendChild(item);
                    addActionToDelete(item, idMedicamento, 1)
                    actualizarTotal();
                }

            })
            .catch(error => {
                console.error("Error al agregar el medicamento a la orden:", error);
                alert("Hubo un error al agregar el medicamento a la orden");
            });
    }
}

function addActionToDelete(item, idMedicamento, cantidad) {
    const deleteButton = item.querySelector(".boton-eliminar");

    // Reemplaza el botón por un clon para remover listeners previos
    const newDeleteButton = deleteButton.cloneNode(true);
    deleteButton.parentNode.replaceChild(newDeleteButton, deleteButton);

    newDeleteButton.addEventListener("click", () => {
        if (!idOrdenActual) {
            console.error("No hay una orden activa para eliminar el medicamento");
            alert("Error: No se puede eliminar el medicamento porque no hay una orden activa");
            return;
        }

        const index = carrito.findIndex(m => m.id === parseInt(idMedicamento));
        console.log(carrito, "Antes de eliminar")
        if (index !== -1) {
            carrito.splice(index, 1);
            console.log(carrito, "después de eliminar")
            actualizarTotal();
        }

        eliminarMedicamentoDeOrden(idMedicamento, idOrdenActual, cantidad)
            .then(() => {
                item.remove();
                actualizarTotal();
            })
            .catch(error => {
                console.error("Error al eliminar el medicamento de la orden:", error);
                alert("Hubo un error al eliminar el medicamento de la orden");
            });
    });
}

// Función para agregar un medicamento a la orden (el servicio maneja la creación de la orden si no existe)
function agregarMedicamentoAOrden(idMedicamento) {
    return fetch("http://localhost:8080/api/orden/addMedicamento", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
            "idUsuario": userId  // Header con el ID de usuario
        },
        body: JSON.stringify({
            idOrden: idOrdenActual || null, // Usar ID actual si existe, si no null
            nombreMedicamento: null,
            idMedicamento: idMedicamento,
            cantidad: 1 // Asumimos cantidad 1 por defecto
        })
    })
        .then(response => {
            if (!response.ok) {
                console.log(response)
                throw new Error("No se pudo agregar el medicamento a la orden");

            }
            return response.json();
        });
}

// Función para eliminar un medicamento de la orden
function eliminarMedicamentoDeOrden(idMedicamento, idOrden, cantidad) {
    return fetch(`http://localhost:8080/api/orden/deleteMedicamento`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            idOrden: idOrden,
            nombreMedicamento: null,
            idMedicamento: idMedicamento,
            cantidad: cantidad
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("No se pudo eliminar el medicamento de la orden");
            }
            return "OK";
        });
}

function actualizarTotal() {
    const total = document.getElementById("total-carrito");
    if (total) {
        let total2 = 0;
        carrito.forEach(item => total2 = total2 + parseInt(item.precio * item.cantidad))

        total.innerText = total2.toLocaleString("es-CO");
    } else {
        console.warn("Elemento con id 'total-carrito' no encontrado.");
    }
} 
function cerrarSesion() {
    localStorage.clear();
    window.location.href = 'index.html';
}