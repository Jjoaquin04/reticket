// Save the original form data
let originalData = {};

document.addEventListener('DOMContentLoaded', function() {
    // Get the original form data when the page loads
    const form = document.getElementById('profile-form');
    const inputs = form.querySelectorAll('input, select, textarea');
    
    inputs.forEach(input => {
        originalData[input.name] = input.value; // Save the original value
    });
});

document.getElementById('profile-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevent default form submission

    const userId = this.getAttribute("data-user-id"); // Get the user ID

    // Include only the fields that have changed
    const changedData = {};
    const inputs = this.querySelectorAll('input, select, textarea');

    inputs.forEach(input => {
        if (originalData[input.name] !== input.value) {
            changedData[input.name] = input.value; // Include only changed fields
        }
    });

    // Check if there are changes before sending the request
    if (Object.keys(changedData).length === 0) {
        Swal.fire({
            icon: 'info',
            title: 'Sin cambios',
            text: 'No se han realizado cambios en el perfil.',
            confirmButtonText: 'Continuar'
        });
        return;
    }

    // Create an object with the form data
    const data = {
        ...changedData, // Only the changed fields
    };

    
    try {
        const response = await fetch(`/users/${userId}`, {
            method: 'PATCH', 
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data) 
        });
    
        const result = await response.json();
    
        if (response.ok) {
            Swal.fire({
                title: '¡Éxito!',
                text: 'Perfil actualizado correctamente',
                icon: 'success',
                confirmButtonText: 'Continuar'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.reload(); 
                }
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error al actualizar el perfil',
                text: result.error || 'Verifica los datos ingresados'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.reload(); 
                }
            });
        }
    }catch(error) {
        console.error('Error:', error); 
        Swal.fire({
            icon: 'error',
            title: 'Error al actualizar el perfil',
            text: result.error || 'Verifica los datos ingresados'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.reload(); 
            }
        }); 
    }
    
});