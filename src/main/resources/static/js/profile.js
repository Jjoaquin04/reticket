document.getElementById('profile-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Evita el envío del formulario por defecto

    const userId = this.getAttribute("data-user-id"); // Obtiene el ID del usuario
    const inputs = this.querySelectorAll("input");
    const updates = {};
 
    inputs.forEach(input => {
        const originalValue = input.getAttribute("data-original");
        const newValue = input.value;
        if (originalValue !== newValue) {
            updates[input.name] = newValue; // Solo agrega los campos que han cambiado
        }
    });
    
    if (Object.keys(updates).length === 0) {
        alert('No se han detectado cambios para actualizar');
        return;
    }
    
    try {
        const response = await fetch(`/users/${userId}`, {
            method: 'PATCH', // Método PATCH para actualizar
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updates) // Convierte el objeto a JSON
        });
    
        const result = await response.json(); // Convierte la respuesta a JSON
    
        if (response.ok) {
            alert('Perfil actualizado con éxito!');
            
        } else {
            alert(result.error || 'Error al actualizar el perfil.');
        }
    } catch(error) {
        console.error('Error:', error); // Manejo de errores
        alert('Error al procesar la actualización.'); // Muestra un mensaje de error
    }
});