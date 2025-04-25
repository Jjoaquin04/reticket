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


    // Manejador para el formulario de inicio de sesión
    document.getElementById("login-form").addEventListener('submit', async function(e) {
        e.preventDefault();
        const submitButton = this.querySelector('button[type="submit"]');
        
        const formData = {
            username: document.getElementById("username-login").value,
            password: document.getElementById("password-login").value
        };
        
        if(!formData.username || !formData.password) {
            RequestFeedback.showError({
                title: 'Campos requeridos',
                text: 'Por favor, completa todos los campos.'
            });
            return;
        }
        
        try {
            const loadingSwal = RequestFeedback.showLoading({
                title: 'Iniciando sesión',
                text: 'Verificando credenciales...'
            });
            
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });
            
            const data = await response.json();
            
            loadingSwal.close();
            
            if(response.ok) {
                RequestFeedback.showSuccess({
                    title: 'Inicio de sesión exitoso',
                    text: 'Redirigiendo...',
                    timer: 1500,
                    showConfirmButton: false,
                    height: 'false'
                }).then(() => {
                    window.location.href = '/home';
                });
            } else {
                RequestFeedback.showError({
                    title: 'Error',
                    text: data.error,
                    height: 'false'
                });
            }
        } catch(error) {
            console.error('Error:', error);
            handleLoadingState(submitButton, false);
            RequestFeedback.showError({
                title: 'Error',
                text: 'Ocurrió un error al procesar la solicitud.',
                height: 'false'
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
            RequestFeedback.showError({
                title: 'Las contraseñas no coinciden',
                text: 'Por favor, asegúrate de que ambas contraseñas sean iguales.'
            });
            return;
        }
        
        const formData = {
            username: username,
            email: email,
            password: password
        };
        
        if(!formData.username || !formData.password || !formData.email) {
            RequestFeedback.showError({
                title: 'Campos requeridos',
                text: 'Por favor, completa todos los campos.'
            });
            return;
        }
        
        try {
            
            const loadingSwal = RequestFeedback.showLoading({
                title: 'Creando cuenta',
                text: 'Registrando usuario...'
            });
            
            const response = await fetch('/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });
            
            const data = await response.json();
            
            loadingSwal.close();
            
            if(response.ok) {
                RequestFeedback.showSuccess({
                    title: 'Registro exitoso, bienvenido ' + username,
                    text: 'Redirigiendo...',
                    timer: 1500,
                    showConfirmButton: false,
                    height: 'false'
                }).then(() => {
                    window.location.href = '/home';
                });
            } else {
                RequestFeedback.showError({
                    title: 'Error',
                    text: data.error,
                    height: 'false'
                });
            }
        } catch(error) {
            console.error('Error:', error);
            RequestFeedback.showError({
                title: 'Error',
                text: 'Ocurrió un error al procesar la solicitud.',
                height: 'false'
            });
        }
    });
});


