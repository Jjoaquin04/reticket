//guardar los datos originales del formulario
let originalData = {};

document.addEventListener('DOMContentLoaded', function() {
    // Obtener los datos originales del formulario al cargar la página
    const form = document.getElementById('profile-form');
    const inputs = form.querySelectorAll('input, select, textarea');
    
    inputs.forEach(input => {
        originalData[input.name] = input.value; // Guardar el valor original
    });
});

document.getElementById('profile-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Evita el envío del formulario por defecto

    const userId = this.getAttribute("data-user-id"); // Obtiene el ID del usuario

    // incluir solo los campos que han cambiado
    const changedData = {};
    const inputs = this.querySelectorAll('input, select, textarea');

    inputs.forEach(input => {
        if (originalData[input.name] !== input.value) {
            changedData[input.name] = input.value; // Solo incluir campos que han cambiado
        }
    });

    // Verificar si hay cambios antes de enviar la solicitud
    if (Object.keys(changedData).length === 0) {
        alert('No hay cambios para guardar.');
        return;
    }

    // Crear un objeto con los datos del formulario
    const data = {
        ...changedData, // Solo los campos que han cambiado
    };

    // Enviar la solicitud PATCH al servidor
    try{
        const response = await fetch(`/users/${userId}`, {
            method: 'PATCH', // Método PATCH para actualizar
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data) // Convierte el objeto a JSON
        });
    
        const result = await response.json(); // Convierte la respuesta a JSON
    
        if (response.ok) {
            alert('Perfil actualizado con éxito!');
            
        } else {
            alert(result.error || 'Error al actualizar el perfil.'); // Muestra un mensaje de error
        }
    }catch(error) {
        console.error('Error:', error); // Manejo de errores
        alert('Error al procesar la actualización.'); // Muestra un mensaje de error
    }
    window.location.href = "/acounts/profile";
});