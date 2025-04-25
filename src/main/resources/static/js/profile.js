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

document.querySelectorAll('.toggle-password').forEach(eyeIcon => {
    eyeIcon.addEventListener('click', function() {
        // Obtener ambos campos de contraseña
        const newPasswordInput = document.getElementById('new-password');
        const confirmPasswordInput = document.getElementById('confirm-password');
        
        // Cambiar el tipo de ambos campos
        const type = newPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        newPasswordInput.setAttribute('type', type);
        confirmPasswordInput.setAttribute('type', type);
        
        // Cambiar el ícono
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
});

document.getElementById('profile-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevent default form submission

    const userId = this.getAttribute("data-user-id"); // Get the user ID
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    // Validate passwords match if any password field is filled
    if (newPassword || confirmPassword) {
        if (newPassword !== confirmPassword) {
            RequestFeedback.showError({
                title: 'Error',
                text: 'Las contraseñas no coinciden'
            });
            return;
        }
    }

    // Include only the fields that have changed
    const changedData = {};
    const inputs = this.querySelectorAll('input[type="text"], input[type="email"]');

    inputs.forEach(input => {
        if (originalData[input.name] !== input.value) {
            changedData[input.name] = input.value; // Include only changed fields
        }
    });

    // Add password if it was filled and validated
    if (newPassword) {
        changedData.password = newPassword;
    }

    // Check if there are changes before sending the request
    if (Object.keys(changedData).length === 0) {
        RequestFeedback.showInfo({
            title: 'Sin cambios',
            text: 'No se han realizado cambios en el perfil.'
        });
        return;
    }

    try {
        
        const loadingSwal = RequestFeedback.showLoading({
            title: 'Actualizando perfil',
            text: 'Guardando cambios...'
        });
        
        const response = await fetch(`/users/${userId}`, {
            method: 'PATCH', 
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(changedData) 
        });
    
        const result = await response.json();
        
        // Cerrar mensaje de carga
        loadingSwal.close();
    
        if (response.ok) {
            RequestFeedback.showSuccess({
                title: '¡Éxito!',
                text: 'Perfil actualizado correctamente'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.reload(); 
                }
            });
        } else {
            RequestFeedback.showError({
                title: 'Error al actualizar el perfil',
                text: result.error || 'Verifica los datos ingresados'
            });
        }
    } catch(error) {
        console.error('Error:', error); 
        RequestFeedback.showError({
            title: 'Error al actualizar el perfil',
            text: 'Ha ocurrido un error al procesar tu solicitud'
        });
    }
});