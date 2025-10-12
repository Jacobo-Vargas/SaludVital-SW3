const API = `${window.location.origin}/api/citas`;
const API_RESULTADOS = `${window.location.origin}/resultados-medicos`;

let todasLasCitas = [];
let todosLosResultados = [];
let resultadosFiltrados = [];

document.addEventListener("DOMContentLoaded", () => {
  listarCitas();
  listarResultados();
  document.getElementById("citaForm").addEventListener("submit", guardarCita);
  document.getElementById("resultadoForm").addEventListener("submit", guardarResultado);
});

// ===== FUNCIONES DE NAVEGACIÓN =====
function mostrarSeccion(id) {
  document.querySelectorAll(".nav-item").forEach(btn => btn.classList.remove("active"));
  document.querySelectorAll(".content section").forEach(sec => sec.classList.add("hidden"));
  document.querySelector(`#${id}`).classList.remove("hidden");

  const mapeo = {
    "panel": "Panel",
    "formulario": "Cita",
    "resultados": "Resultados",
    "nuevoResultado": "Nuevo Resultado"
  };

  const boton = Array.from(document.querySelectorAll(".nav-item"))
    .find(b => b.textContent.includes(mapeo[id]));
  if (boton) boton.classList.add("active");
}

// ===== FUNCIONES DE CITAS (ORIGINAL) =====
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

// ===== FUNCIONES DE RESULTADOS MÉDICOS =====
async function listarResultados() {
  try {
    const res = await fetch(API_RESULTADOS);
    todosLosResultados = await res.json();
    resultadosFiltrados = todosLosResultados;
    renderizarResultados(resultadosFiltrados);
  } catch (error) {
    console.error("Error al cargar resultados:", error);
    document.getElementById("resultadosContainer").innerHTML = `<p style="color:red;text-align:center;">Error al cargar resultados médicos.</p>`;
  }
}

function renderizarResultados(resultados) {
  const container = document.getElementById("resultadosContainer");
  container.innerHTML = "";

  if (resultados.length === 0) {
    container.innerHTML = `<p style="color:gray;text-align:center;">No hay resultados médicos registrados.</p>`;
    return;
  }

  resultados.forEach(resultado => {
    const card = document.createElement("div");
    card.classList.add("resultado-card");
    card.innerHTML = `
      <div class="resultado-header">
        <h3>${resultado.paciente}</h3>
        <span class="estado-badge estado-${resultado.estado}">${resultado.estado}</span>
      </div>
      <div class="resultado-info">
        <p><strong>ID:</strong> #${resultado.id}</p>
        <p><strong>Tipo de Examen:</strong> ${resultado.tipoExamen}</p>
        <p><strong>Médico:</strong> ${resultado.medicoResponsable}</p>
        <p><strong>Fecha Examen:</strong> ${formatearFecha(resultado.fechaExamen)}</p>
      </div>
      <div class="resultado-actions">
        <button class="btn-icon" onclick="verDetalleResultado(${resultado.id})" title="Ver detalle">👁️</button>
        <button class="btn-icon" onclick="editarResultado(${resultado.id})" title="Editar">✏️</button>
        <button class="btn-icon" onclick="eliminarResultado(${resultado.id})" title="Eliminar">🗑️</button>
      </div>
    `;
    container.appendChild(card);
  });
}

async function guardarResultado(e) {
  e.preventDefault();
  const id = document.getElementById("resultadoId").value;
  
  const resultado = {
    paciente: document.getElementById("resultadoPaciente").value.trim(),
    tipoExamen: document.getElementById("resultadoTipoExamen").value.trim(),
    medicoResponsable: document.getElementById("resultadoMedicoResponsable").value.trim(),
    estado: document.getElementById("resultadoEstado").value,
    fechaExamen: document.getElementById("resultadoFechaExamen").value,
    fechaEmision: document.getElementById("resultadoFechaEmision").value || new Date().toISOString(),
    resultados: document.getElementById("resultadoResultados").value.trim(),
    observaciones: document.getElementById("resultadoObservaciones").value.trim()
  };

  try {
    if (id) {
      await fetch(`${API_RESULTADOS}/${id}`, { 
        method: "PUT", 
        headers: { "Content-Type": "application/json" }, 
        body: JSON.stringify(resultado) 
      });
      alert("Resultado actualizado correctamente");
    } else {
      await fetch(API_RESULTADOS, { 
        method: "POST", 
        headers: { "Content-Type": "application/json" }, 
        body: JSON.stringify(resultado) 
      });
      alert("Resultado creado correctamente");
    }

    limpiarFormularioResultado();
    listarResultados();
    mostrarSeccion("resultados");
  } catch (error) {
    console.error("Error al guardar resultado:", error);
    alert("Error al guardar el resultado");
  }
}

async function editarResultado(id) {
  try {
    const res = await fetch(`${API_RESULTADOS}/${id}`);
    const resultado = await res.json();

    document.getElementById("resultadoId").value = resultado.id;
    document.getElementById("resultadoPaciente").value = resultado.paciente;
    document.getElementById("resultadoTipoExamen").value = resultado.tipoExamen;
    document.getElementById("resultadoMedicoResponsable").value = resultado.medicoResponsable;
    document.getElementById("resultadoEstado").value = resultado.estado;
    document.getElementById("resultadoFechaExamen").value = formatearFechaInput(resultado.fechaExamen);
    document.getElementById("resultadoFechaEmision").value = formatearFechaInput(resultado.fechaEmision);
    document.getElementById("resultadoResultados").value = resultado.resultados;
    document.getElementById("resultadoObservaciones").value = resultado.observaciones || "";

    mostrarSeccion("nuevoResultado");
  } catch (error) {
    console.error("Error al cargar resultado:", error);
    alert("Error al cargar el resultado");
  }
}

async function eliminarResultado(id) {
  if (confirm("¿Está seguro de que desea eliminar este resultado médico?")) {
    try {
      await fetch(`${API_RESULTADOS}/${id}`, { method: "DELETE" });
      alert("Resultado eliminado correctamente");
      listarResultados();
    } catch (error) {
      console.error("Error al eliminar resultado:", error);
      alert("Error al eliminar el resultado");
    }
  }
}

async function verDetalleResultado(id) {
  try {
    const res = await fetch(`${API_RESULTADOS}/${id}`);
    const resultado = await res.json();

    const contenido = `
      <div class="detalle-item">
        <strong>ID:</strong>
        <p>#${resultado.id}</p>
      </div>
      <div class="detalle-item">
        <strong>Estado:</strong>
        <p><span class="estado-badge estado-${resultado.estado}">${resultado.estado}</span></p>
      </div>
      <div class="detalle-item">
        <strong>Paciente:</strong>
        <p>${resultado.paciente}</p>
      </div>
      <div class="detalle-item">
        <strong>Tipo de Examen:</strong>
        <p>${resultado.tipoExamen}</p>
      </div>
      <div class="detalle-item">
        <strong>Médico Responsable:</strong>
        <p>${resultado.medicoResponsable}</p>
      </div>
      <div class="detalle-item">
        <strong>Fecha de Examen:</strong>
        <p>${formatearFecha(resultado.fechaExamen)}</p>
      </div>
      <div class="detalle-item">
        <strong>Fecha de Emisión:</strong>
        <p>${formatearFecha(resultado.fechaEmision)}</p>
      </div>
      <div class="detalle-item">
        <strong>Resultados:</strong>
        <p>${resultado.resultados}</p>
      </div>
      ${resultado.observaciones ? `
      <div class="detalle-item
      <div class="detalle-item">
        <strong>Observaciones:</strong>
        <p>${resultado.observaciones}</p>
      </div>
      ` : ''}
    `;

    document.getElementById("modalBody").innerHTML = contenido;
    document.getElementById("modalDetalle").classList.remove("hidden");
  } catch (error) {
    console.error("Error al cargar detalle:", error);
    alert("Error al cargar el detalle del resultado");
  }
}

function cerrarModal() {
  document.getElementById("modalDetalle").classList.add("hidden");
}

function limpiarFormularioResultado() {
  document.getElementById("resultadoId").value = "";
  document.getElementById("resultadoForm").reset();
}

function filtrarResultados() {
  const busqueda = document.getElementById("busquedaResultado").value.toLowerCase();
  const filtroTipo = document.getElementById("filtroTipoExamen").value.toLowerCase();
  const filtroEstado = document.getElementById("filtroEstadoResultado").value;

  resultadosFiltrados = todosLosResultados.filter(r =>
    r.paciente.toLowerCase().includes(busqueda) &&
    r.tipoExamen.toLowerCase().includes(filtroTipo) &&
    (filtroEstado === "" || r.estado === filtroEstado)
  );

  renderizarResultados(resultadosFiltrados);
}

function limpiarFiltrosResultados() {
  document.getElementById("busquedaResultado").value = "";
  document.getElementById("filtroTipoExamen").value = "";
  document.getElementById("filtroEstadoResultado").value = "";
  resultadosFiltrados = todosLosResultados;
  renderizarResultados(resultadosFiltrados);
}

// ===== UTILIDADES =====
function formatearFecha(fecha) {
  if (!fecha) return '-';
  const date = new Date(fecha);
  return date.toLocaleString('es-ES', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function formatearFechaInput(fecha) {
  if (!fecha) return '';
  const date = new Date(fecha);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day}T${hours}:${minutes}`;
}

// Cerrar modal al hacer clic fuera del contenido
document.addEventListener('click', function(e) {
  const modal = document.getElementById('modalDetalle');
  if (e.target === modal) {
    cerrarModal();
  }
});