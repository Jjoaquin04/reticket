// Agregar el manejador de evento para el cierre de sesión
document.addEventListener('DOMContentLoaded', function() {
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', async function(e) {
            e.preventDefault();
            
            try {
                const response = await fetch('/auth/logout', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                
                if (response.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Sesión cerrada',
                        text: 'Has cerrado sesión correctamente',
                        timer: 1500,
                        showConfirmButton: false
                    }).then(() => {
                        window.location.href = '/';
                    });
                }
            } catch (error) {
                console.error('Error al cerrar sesión:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Ocurrió un error al cerrar sesión'
                });
            }
        });
    }
});