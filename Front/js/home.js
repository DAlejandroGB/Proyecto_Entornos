let token = null;

document.addEventListener("DOMContentLoaded", () => {
    token = localStorage.getItem("token");
    if (token === null) {
         alert("Debes estar logeado para acceder a esta seccion");
        window.location.href = "index.html";
        return;
     }
    cargarMedicamentos();
});

let carrito = [];

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
                const tarjeta = document.createElement("div");
                tarjeta.className = "product-card";
                tarjeta.innerHTML = `<img src="images/6408427.png" alt="${medicamento.nombre}" />
                <h3>${medicamento.nombre}</h3>
                <p class="price">$${medicamento.precio}</p>
                <button class="botonMedicamento" data-id="${medicamento.medicamentos_id}" data-precio="${medicamento.precio}">
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

function eventoBotonMedicamento(evento) {
    if (evento.target.classList.contains("botonMedicamento")) {
        const botonMedicamento = evento.target;
        const idMedicamento = botonMedicamento.dataset.id;
        const tarjeta = botonMedicamento.closest(".product-card");
        const nombre = tarjeta.querySelector("h3").innerText;
        const precioTexto = tarjeta.querySelector("p:nth-child(3)").innerText;
        const precio = parseInt(precioTexto.replace(/[^\d]/g, ""));
        const imgSrc = tarjeta.querySelector("img").src;

        // Agregar al arreglo
        carrito.push({
            id: idMedicamento,
            nombre: nombre,
            precio: precio,
            img: imgSrc
        });

        // Mostrar en el HTML del carrito (parte derecha)
        const lista = document.getElementById("lista-carrito");
        const item = document.createElement("li");
        item.classList.add("item-carrito");
        item.innerHTML = `
            <div class="info">
                <strong>${nombre}</strong>
                <span class="precio">$${precio.toLocaleString("es-CO")}</span>
            </div>
            <button class="boton-eliminar" data-id="${idMedicamento}">Eliminar</button>
        `;

        lista.appendChild(item);
        // Agregar evento al botón eliminar
        item.querySelector(".boton-eliminar").addEventListener("click", () => {
            // Quitar del arreglo
            const index = carrito.findIndex(m => m.id === idMedicamento);
            if (index !== -1) {
                carrito.splice(index, 1);
            }

            // Quitar del DOM
            item.remove();
            actualizarTotal();
        });

        // Actualiza el total
        actualizarTotal();
    }
}



function actualizarTotal() {
    const total = document.getElementById("total-carrito");
    if (total) {
        const totalValor = carrito.reduce((acum, elemento) => acum + parseInt(elemento.precio), 0);
        total.innerText = totalValor;
        total.innerText = totalValor.toLocaleString("es-CO");
    } else {
        console.warn("Elemento con id 'total-carrito' no encontrado.");
    }
}

function agregarMedicamentoCarrito(evento) {
    const botonMedicamento = evento.target;
    const idMedicamento = botonMedicamento.dataset.id;
    const precioMedicamento = botonMedicamento.parentElement.querySelector("p:nth-child(3)").innerHTML;
    carrito.push({
        id: idMedicamento,
        precio: precioMedicamento
    });
    actualizarTotal();
}

function removerMedicamentoCarrito(evento) {
    const botonMedicamento = evento.target;
    const idMedicamento = botonMedicamento.dataset.id;
    const index = carrito.findIndex(elemento => elemento.id === idMedicamento);
    carrito.splice(index, 1);
    actualizarTotal();
}
function cerrarSesion() {
    localStorage.clear();
    window.location.href = 'index.html'; // o la página de login
}