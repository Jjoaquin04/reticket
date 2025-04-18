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

    document.getElementById("login-form").addEventListener('submit',async function (e) {
        e.preventDefault();

        const formData = {
            username: document.getElementById("username-login").value,
            password: document.getElementById("password-login").value
        }
        if(!formData.username || !formData.password) {
            Swal.fire({
                icon: 'error',
                title: 'Campos requeridos',
                text: 'Por favor, completa todos los campos.',
                heightAuto: false,
            })
            return;
        }
        try{
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            })
            const data = await response.json();

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
            }else{
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.error,
                    heightAuto: false,
                })
            }
        }catch(error) {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al procesar la solicitud.',
                heightAuto: false,
            })
        }
    })
    document.getElementById("register-form").addEventListener('submit',async function (e) {
        e.preventDefault();
        
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
        }
        
        if(!formData.username || !formData.password || !formData.email) {
            Swal.fire({
                icon: 'error',
                title: 'Campos requeridos',
                text: 'Por favor, completa todos los campos.',
                heightAuto: false,
            })
            return;
        }
        
        try{
            const response = await fetch('/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            })
            const data = await response.json();

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
            }else{
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.error,
                    heightAuto: false,
                })
            }
        }catch(error) { 
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al procesar la solicitud.',
                heightAuto: false,
            })
        }
    }) 
});


