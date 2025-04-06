document.getElementById('profile-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Evita el envío del formulario por defecto

    const userId = this.getAttribute("data-user-id"); // Obtiene el ID del usuario

    const formData = new FormData(this); // Obtiene los datos del formulario
    const data = Object.fromEntries(formData.entries()); // Convierte FormData a un objeto
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
        window.location.href = "/acounts/profile";
    }catch(error) {
        console.error('Error:', error); // Manejo de errores
        alert('Error al procesar la actualización.'); // Muestra un mensaje de error
        window.location.href = "/acounts/profile";
    }
    
});