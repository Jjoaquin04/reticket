const eliminar_button = document.getElementsByClassName("eliminar-button"); // Get all elements with the class "eliminar-button"


for (let i = 0; i < eliminar_button.length; i++) {
    eliminar_button[i].addEventListener("click", async function() {
        //Obtener el id del evento desde el atributo "key" del elemento padre
        const eventId = this.getAttribute("key");

        // Mostrar mensaje de confirmación
        RequestFeedback.showConfirm({
            title: "¿Estás seguro?",
            text: "No se podrá revertir la acción!",
            icon: "warning",
            confirmButtonText: "Sí, bórralo",
            cancelButtonText: "Cancelar"
        }).then(async (result) => {
            if (result.isConfirmed) {
                
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
        const statusValue = formData.get('eventStatus');
        const eventId = this.getAttribute("key");

        
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
                
                body: JSON.stringify(statusValue)
            });
            const data = await response.json();

          
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
            loadingSwal.close();
            
            RequestFeedback.showError({
                title: "Error de conexión",
                text: "Ha ocurrido un error al actualizar el estado del evento. Por favor, inténtalo de nuevo"
            });
        }
    });
}