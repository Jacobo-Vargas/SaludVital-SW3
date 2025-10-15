const APICITAS = `${window.location.origin}/api/citas`;
const APIRESULTADO = `${window.location.origin}/api/resultados-medicos`;
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
  const res = await fetch(APICITAS);
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
    await fetch(`${APICITAS}/${id}`, { method: "PUT", headers: { "Content-Type": "application/json" }, body: JSON.stringify(cita) });
  } else {
    await fetch(APICITAS, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(cita) });
  }

  limpiarFormulario();
  listarCitas();
  mostrarSeccion("panel");
}

async function editarCita(id) {
  const res = await fetch(`${APICITAS}/${id}`);
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
    await fetch(`${APICITAS}/${id}`, { method: "DELETE" });
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


async function registrarResultado(resultado) {
  console.log("Este es el reusltado",resultado);
  await fetch(APIRESULTADO, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(resultado) });
  document.getElementById("id").value = "";
  document.getElementById("resultadosForm").reset();
}


/* ==========================
   CRUD RESULTADOS MÉDICOS
========================== */

document.addEventListener("DOMContentLoaded", () => {
  listarResultados();
  document.getElementById("resultadosForm").addEventListener("submit", guardarResultado);
});

async function listarResultados() {
  const res = await fetch(APIRESULTADO);
  const resultados = await res.json();
  renderizarResultados(resultados);
}

function renderizarResultados(resultados) {
  const cont = document.getElementById("resultadosContainer");
  cont.innerHTML = "";

  if (resultados.length === 0) {
    cont.innerHTML = `<p style="color:gray;text-align:center;">No hay resultados registrados.</p>`;
    return;
  }

  resultados.forEach(r => {
    const card = document.createElement("div");
    card.classList.add("cita-card");
    card.innerHTML = `
      <div class="cita-header">
        <h3>${r.paciente}</h3>
        <small>${r.tipoExamen}</small>
      </div>
      <div class="cita-info">
        <p><b>Fecha examen:</b> ${r.fechaExamen ? r.fechaExamen.replace("T", " ") : "N/A"}</p>
        <p><b>Médico:</b> ${r.medicoResponsable}</p>
        <p><b>Estado:</b> ${r.estado}</p>
        <p><b>Resultados:</b> ${r.resultados || "Sin detalles"}</p>
      </div>
      <div class="cita-actions">
        <button class="btn-icon" onclick="editarResultado(${r.id})">✏️</button>
        <button class="btn-icon" onclick="eliminarResultado(${r.id})">🗑️</button>
      </div>
    `;
    cont.appendChild(card);
  });
}

async function guardarResultado(e) {
  e.preventDefault();

  const id = document.getElementById("resultadoId").value;
  const dto = {
    paciente: document.getElementById("resultadoPaciente").value,
    tipoExamen: document.getElementById("resultadoTipoExamen").value,
    resultados: document.getElementById("resultadoTexto").value,
    medicoResponsable: document.getElementById("resultadoMedico").value,
    fechaExamen: document.getElementById("resultadoFechaExamen").value,
    observaciones: document.getElementById("resultadoObservaciones").value,
    estado: document.getElementById("resultadoEstado").value,
    fechaEmision: new Date().toISOString()
  };

  const opciones = {
    method: id ? "PUT" : "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto)
  };

  const url = id ? `${APIRESULTADO}/${id}` : APIRESULTADO;
  const res = await fetch(url, opciones);

  if (res.ok) {
    limpiarFormularioResultado();
    listarResultados();
  } else {
    console.error("Error al guardar resultado:", res.status);
    alert(" Error al guardar el resultado. Ver consola para detalles.");
  }
}



async function editarResultado(id) {
  const res = await fetch(`${APIRESULTADO}/${id}`);
  const r = await res.json();

  document.getElementById("resultadoId").value = r.id;
  document.getElementById("resultadoPaciente").value = r.paciente;
  document.getElementById("resultadoTipoExamen").value = r.tipoExamen;
  document.getElementById("resultadoTexto").value = r.resultados;
  document.getElementById("resultadoMedico").value = r.medicoResponsable;
  document.getElementById("resultadoFechaExamen").value = r.fechaExamen ? r.fechaExamen.substring(0, 16) : "";
  document.getElementById("resultadoObservaciones").value = r.observaciones;
  document.getElementById("resultadoEstado").value = r.estado || "PENDIENTE";


  document.getElementById("resultadosForm").scrollIntoView({ behavior: "smooth", block: "start" });
}


async function eliminarResultado(id) {
  if (confirm("¿Eliminar este resultado médico?")) {
    await fetch(`${APIRESULTADO}/${id}`, { method: "DELETE" });
    listarResultados();
  }
}

function limpiarFormularioResultado() {
  document.getElementById("resultadoId").value = "";
  document.getElementById("resultadosForm").reset();
}

