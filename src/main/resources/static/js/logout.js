// Agregar el manejador de evento para el cierre de sesión
document.addEventListener('DOMContentLoaded', function() {
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', async function(e) {
            e.preventDefault();
            
            Swal.fire({ 
                title: "Cerrar sesión",
                text: "Estas a punto de cerrar sesión!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Sí, cerrar sesión",
                cancelButtonText: "Cancelar"
            }).then(async (result) => {
                if(result.isConfirmed) {
                    try {
                        const loadingSwal = RequestFeedback.showLoading({
                            title: 'Cerrando sesión',
                            text: 'Procesando...'
                        });
                        
                        const response = await fetch('/auth/logout', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        });
                        
                        loadingSwal.close();
                        
                        if (response.ok) {
                            RequestFeedback.showSuccess({
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
                        RequestFeedback.showError({
                            title: 'Error',
                            text: 'Ocurrió un error al cerrar sesión'
                        });
                    }
                }
            });
        });
    }
});