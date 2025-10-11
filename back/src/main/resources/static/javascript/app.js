const API = `${window.location.origin}/api/citas`;
let todasLasCitas = [];

document.addEventListener("DOMContentLoaded", () => {
  listarCitas();
  document.getElementById("citaForm").addEventListener("submit", guardarCita);
});

function mostrarSeccion(id) {
  document.querySelectorAll(".nav-item").forEach(btn => btn.classList.remove("active"));
  document.querySelectorAll(".content section").forEach(sec => sec.classList.add("hidden"));
  document.querySelector(`#${id}`).classList.remove("hidden");

  const boton = Array.from(document.querySelectorAll(".nav-item"))
    .find(b => b.textContent.includes(id === "panel" ? "Panel" : "Cita"));
  if (boton) boton.classList.add("active");
}

async function listarCitas() {
  const res = await fetch(API);
  todasLasCitas = await res.json();
  renderizarCitas(todasLasCitas);
}

function renderizarCitas(citas) {
  const container = document.getElementById("citasContainer");
  container.innerHTML = "";

  if (citas.length === 0) {
    container.innerHTML = `<p style="color:gray;text-align:center;">No hay citas registradas.</p>`;
    return;
  }

  citas.forEach(cita => {
    const card = document.createElement("div");
    card.classList.add("cita-card");
    card.innerHTML = `
      <div class="cita-header">
        <h3>${cita.paciente}</h3>
        <small>#${cita.id}</small>
      </div>
      <div class="cita-info">
        <p><b>Especialidad:</b> ${cita.especialidad}</p>
        <p><b>Fecha:</b> ${cita.fechaHora}</p>
        <p><b>Motivo:</b> ${cita.motivo}</p>
      </div>
      <div class="cita-actions">
        <button class="btn-icon" onclick="editarCita(${cita.id})">✏️</button>
        <button class="btn-icon" onclick="eliminarCita(${cita.id})">🗑️</button>
      </div>
    `;
    container.appendChild(card);
  });
}

async function guardarCita(e) {
  e.preventDefault();
  const id = document.getElementById("id").value;
  const paciente = document.getElementById("paciente").value.trim();
  const especialidad = document.getElementById("especialidad").value.trim();
  const fechaHora = document.getElementById("fechaHora").value.trim();
  const motivo = document.getElementById("motivo").value.trim();

  const cita = { paciente, especialidad, fechaHora, motivo };

  if (id) {
    await fetch(`${API}/${id}`, { method: "PUT", headers: { "Content-Type": "application/json" }, body: JSON.stringify(cita) });
  } else {
    await fetch(API, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(cita) });
  }

  limpiarFormulario();
  listarCitas();
  mostrarSeccion("panel");
}

async function editarCita(id) {
  const res = await fetch(`${API}/${id}`);
  const cita = await res.json();

  document.getElementById("id").value = cita.id;
  document.getElementById("paciente").value = cita.paciente;
  document.getElementById("especialidad").value = cita.especialidad;
  document.getElementById("fechaHora").value = cita.fechaHora;
  document.getElementById("motivo").value = cita.motivo;

  mostrarSeccion("formulario");
}

async function eliminarCita(id) {
  if (confirm("¿Eliminar esta cita?")) {
    await fetch(`${API}/${id}`, { method: "DELETE" });
    listarCitas();
  }
}

function limpiarFormulario() {
  document.getElementById("id").value = "";
  document.getElementById("citaForm").reset();
}

function filtrarCitas() {
  const busqueda = document.getElementById("busqueda").value.toLowerCase();
  const filtroEsp = document.getElementById("filtroEspecialidad").value.toLowerCase();
  const filtroFecha = document.getElementById("filtroFecha").value;

  let filtradas = todasLasCitas.filter(c =>
    c.paciente.toLowerCase().includes(busqueda) &&
    (filtroEsp === "" || c.especialidad.toLowerCase() === filtroEsp) &&
    (filtroFecha === "" || c.fechaHora.startsWith(filtroFecha))
  );

  renderizarCitas(filtradas);
}
