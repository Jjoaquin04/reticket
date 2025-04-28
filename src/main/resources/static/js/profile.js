// Guardar los datos originales del formulario
let originalData = {};

document.addEventListener('DOMContentLoaded', function() {
    // Obtener los datos originales del formulario cuando se carga la página
    const form = document.getElementById('profile-form');
    const inputs = form.querySelectorAll('input, select, textarea');
    
    inputs.forEach(input => {
        originalData[input.name] = input.value; // Guardar el valor original
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
    event.preventDefault(); // Prevenir el envío predeterminado del formulario

    const userId = this.getAttribute("data-user-id"); // Obtener el ID del usuario
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    // Validar que las contraseñas coincidan si se ha completado algún campo de contraseña
    if (newPassword || confirmPassword) {
        if (newPassword !== confirmPassword) {
            RequestFeedback.showError({
                title: 'Error',
                text: 'Las contraseñas no coinciden'
            });
            return;
        }
    }

    // Incluir solo los campos que han cambiado
    const changedData = {};
    const inputs = this.querySelectorAll('input[type="text"], input[type="email"]');

    inputs.forEach(input => {
        if (originalData[input.name] !== input.value) {
            changedData[input.name] = input.value; // Incluir solo campos modificados
        }
    });

    // Añadir contraseña si fue completada y validada
    if (newPassword) {
        changedData.password = newPassword;
    }

    // Verificar si hay cambios antes de enviar la solicitud
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