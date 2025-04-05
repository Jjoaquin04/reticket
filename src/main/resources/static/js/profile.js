document.getElementById('profile-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Evita el envío del formulario por defecto

    const userId = document.getElementById('id-user').textContent; // Obtiene el ID del usuario

    const formData = new FormData(this); // Obtiene los datos del formulario
    const data = Object.fromEntries(formData.entries()); // Convierte FormData a un objeto

    const response = await fetch(`/users/${userId}`, {
        method: 'PATCH', // Método PATCH para actualizar
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data) // Convierte el objeto a JSON
    });

    const result = await response.json(); // Convierte la respuesta a JSON

    if (result) {
        alert('Perfil actualizado con éxito!'); // Muestra un mensaje de éxito
    } else {
        alert('Error al actualizar el perfil.'); // Muestra un mensaje de error
    }
});