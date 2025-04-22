document.addEventListener('DOMContentLoaded', function() {
    // Obtener referencias a los elementos
    const showRegisterLink = document.getElementById('show-register');
    const showLoginLink = document.getElementById('show-login');
    const flipper = document.querySelector('.flipper');
    
    // Evento para mostrar el registro (flip hacia atrás)
    showRegisterLink.addEventListener('click', function(e) {
        e.preventDefault();
        flipper.classList.add('flip');
    });
    
    // Evento para mostrar el login (flip hacia adelante)
    showLoginLink.addEventListener('click', function(e) {
        e.preventDefault();
        flipper.classList.remove('flip');
    });

    // Función reutilizable para manejar estados de carga
    function handleLoadingState(button, isLoading) {
        if (!button) return;
        
        // Guardar texto original si aún no se ha guardado
        if (!button.hasAttribute('data-original-text') && button.textContent) {
            button.setAttribute('data-original-text', button.textContent);
        }
        
        const originalText = button.getAttribute('data-original-text') || button.textContent;
        
        if (isLoading) {
            button.disabled = true;
            button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';
        } else {
            button.disabled = false;
            button.textContent = originalText;
        }
    }

    // Manejador para el formulario de inicio de sesión
    document.getElementById("login-form").addEventListener('submit', async function(e) {
        e.preventDefault();
        const submitButton = this.querySelector('button[type="submit"]');
        
        const formData = {
            username: document.getElementById("username-login").value,
            password: document.getElementById("password-login").value
        };
        
        if(!formData.username || !formData.password) {
            Swal.fire({
                icon: 'error',
                title: 'Campos requeridos',
                text: 'Por favor, completa todos los campos.',
                heightAuto: false,
            });
            return;
        }
        
        try {
            handleLoadingState(submitButton, true);
            
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });
            
            const data = await response.json();
            
            handleLoadingState(submitButton, false);
            
            if(response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Inicio de sesión exitoso',
                    text: 'Redirigiendo...',
                    timer: 1500,
                    showConfirmButton: false,
                    heightAuto: false,
                }).then(() => {
                    window.location.href = '/home';
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.error,
                    heightAuto: false,
                });
            }
        } catch(error) {
            console.error('Error:', error);
            handleLoadingState(submitButton, false);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al procesar la solicitud.',
                heightAuto: false,
            });
        }
    });
    
    // Manejador para el formulario de registro
    document.getElementById("register-form").addEventListener('submit', async function(e) {
        e.preventDefault();
        const submitButton = this.querySelector('button[type="submit"]');
        
        const username = document.getElementById("username-register").value;
        const email = document.getElementById("email-register").value;
        const password = document.getElementById("password-register").value;
        const confirmPassword = document.getElementById("confirm-password-register").value;
        
        // Verificar que las contraseñas coinciden
        if (password !== confirmPassword) {
            Swal.fire({
                icon: 'error',
                title: 'Las contraseñas no coinciden',
                text: 'Por favor, asegúrate de que ambas contraseñas sean iguales.',
                heightAuto: false,
            });
            return;
        }
        
        const formData = {
            username: username,
            email: email,
            password: password
        };
        
        if(!formData.username || !formData.password || !formData.email) {
            Swal.fire({
                icon: 'error',
                title: 'Campos requeridos',
                text: 'Por favor, completa todos los campos.',
                heightAuto: false,
            });
            return;
        }
        
        try {
            handleLoadingState(submitButton, true);
            
            const response = await fetch('/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });
            
            const data = await response.json();
            
            handleLoadingState(submitButton, false);
            
            if(response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Registro exitoso, bienvenido ' + data.username,
                    text: 'Redirigiendo...',
                    timer: 1500,
                    showConfirmButton: false,
                    heightAuto: false
                }).then(() => {
                    window.location.href = '/home';
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.error,
                    heightAuto: false,
                });
            }
        } catch(error) {
            console.error('Error:', error);
            handleLoadingState(submitButton, false);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al procesar la solicitud.',
                heightAuto: false,
            });
        }
    });
});


