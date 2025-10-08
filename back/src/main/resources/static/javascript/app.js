// Espera a que todo el contenido del DOM esté cargado
document.addEventListener('DOMContentLoaded', () => {

    console.log("VitalApp cargada y lista. 🚀");

    // Seleccionar el formulario de citas
    const citaForm = document.getElementById('cita-form');

    // Añadir un evento 'submit' al formulario
    citaForm.addEventListener('submit', (event) => {
        // Prevenir el comportamiento por defecto del formulario (que es recargar la página)
        event.preventDefault();

        // Obtener los valores de los campos del formulario
        const especialidad = document.getElementById('especialidad').value;
        const fecha = document.getElementById('fecha').value;
        const hora = document.getElementById('hora').value;

        // Validar que los campos no estén vacíos
        if (!especialidad || !fecha || !hora) {
            alert('Por favor, complete todos los campos para agendar la cita.');
            return;
        }

        // Crear un objeto con los datos de la cita
        const datosCita = {
            especialidad: especialidad,
            fecha: fecha,
            hora: hora,
        };

        // Simular el envío de datos al backend
        console.log("Enviando datos de la cita al backend (simulación):");
        console.log(datosCita);

        // Mostrar una alerta al usuario de que la cita fue "agendada"
        alert(`¡Cita agendada con éxito para ${especialidad} el día ${fecha} a las ${hora}!`);

        // Limpiar el formulario después del envío
        citaForm.reset();
    });

    //se debe adaptar para que consuma el servicio del back a medida que vayamos avanzando

});