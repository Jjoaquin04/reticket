const eliminar_button = document.getElementsByClassName("eliminar-button"); // Get all elements with the class "eliminar-button"

// Loop through each button and add a click event listener
for (let i = 0; i < eliminar_button.length; i++) {
    eliminar_button[i].addEventListener("click", async function() {
        // Get the ID of the event to be deleted from the button's data attribute
        const eventId = this.getAttribute("key");

        // Show a confirmation dialog to the user
        RequestFeedback.showConfirm({
            title: "¿Estás seguro?",
            text: "No se podrá revertir la acción!",
            icon: "warning",
            confirmButtonText: "Sí, bórralo",
            cancelButtonText: "Cancelar"
        }).then(async (result) => {
            if (result.isConfirmed) {
                // Mostrar mensaje de carga
                const loadingSwal = RequestFeedback.showLoading({
                    title: 'Eliminando evento',
                    text: 'Por favor espere...'
                });
                
                try{
                    const response = await fetch("/events/" + eventId, {
                        method: "DELETE",
                        headers: {
                            "Content-Type": "application/json"
                        }
                    });
    
                    // Cerrar mensaje de carga
                    loadingSwal.close();
    
                    if (!response.ok) {
                        RequestFeedback.showError({
                            title: "Error al eliminar",
                            text: "Ha ocurrido un error al eliminar el evento. Por favor, inténtalo de nuevo"
                        });
                    } else {
                        RequestFeedback.showSuccess({
                            title: "¡Éxito!",
                            text: "Evento eliminado correctamente",
                            timer: 1500,
                            showConfirmButton: false
                        }).then(() => {
                            location.reload();
                        }); 
                    }
                } catch (error) {
                    console.error("Error al eliminar el evento:", error);
                    // Cerrar mensaje de carga si aún está abierto
                    loadingSwal.close();
                    
                    RequestFeedback.showError({
                        title: "Error de conexión",
                        text: "Ha ocurrido un error al eliminar el evento. Por favor, inténtalo de nuevo"
                    });
                }
            }
          });
    });
}

const update_forms = document.getElementsByClassName("status-form");
for (let i = 0; i < update_forms.length; i++) {
    update_forms[i].addEventListener("submit", async function(event) {
        event.preventDefault();

        const formData = new FormData(this);
        // Get only the value of the status
        const statusValue = formData.get('eventStatus');
        const eventId = this.getAttribute("key");

        // Mostrar mensaje de carga
        const loadingSwal = RequestFeedback.showLoading({
            title: 'Actualizando estado',
            text: 'Guardando cambios...'
        });
        
        try {
            const response = await fetch("/events/" + eventId, {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json'
                },
                // Send the value directly as a string in JSON
                body: JSON.stringify(statusValue)
            });
            const data = await response.json();

            // Cerrar mensaje de carga
            loadingSwal.close();
            
            if (!response.ok) {
                RequestFeedback.showError({
                    title: "Error al actualizar",
                    text: "Ha ocurrido un error al actualizar el estado del evento. Por favor, inténtalo de nuevo"
                });
            } else {
                RequestFeedback.showSuccess({
                    title: "Estado actualizado",
                    text: "Nuevo estado: " + data.status,
                    timer: 1500,
                    showConfirmButton: false
                }).then(() => {
                    location.reload();
                });
            }
        } catch (error) {
            console.error("Error al actualizar el evento:", error);
            
            // Cerrar mensaje de carga si aún está abierto
            loadingSwal.close();
            
            RequestFeedback.showError({
                title: "Error de conexión",
                text: "Ha ocurrido un error al actualizar el estado del evento. Por favor, inténtalo de nuevo"
            });
        }
    });
}