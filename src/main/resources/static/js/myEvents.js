const eliminar_button = document.getElementsByClassName("eliminar-button"); // Get all elements with the class "eliminar-button"

// Loop through each button and add a click event listener
for (let i = 0; i < eliminar_button.length; i++) {
    eliminar_button[i].addEventListener("click", async function() {
        // Get the ID of the event to be deleted from the button's data attribute
        const eventId = this.getAttribute("key");

        // Show a confirmation dialog to the user
        Swal.fire({
            title: "Estás seguro?",
            text: "No se podra revertir la acción!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Sí, borralo",
            cancelButtonText: "Cancelar"
        }).then(async (result) => {
            if (result.isConfirmed) {
                try{
                    const response = await fetch("/events/" + eventId, {
                        method: "DELETE",
                        headers: {
                            "Content-Type": "application/json"
                        }
                    });
    
                    console.log(response);
    
                    if (!response.ok) {
                        Swal.fire({
                            icon: "error",
                            title: "Oops...",
                            text: "Ha ocurrido un error al eliminar el evento. Por favor, inténtalo de nuevo",
                        });
                    }else{
                        Swal.fire({
                            title: "¡Éxito!",
                            text: "Evento eliminado correctamente",
                            icon: "success",
                            confirmButtonText: "Continuar"
                        }).then((result) => {
                            if( result.isConfirmed) {
                                location.reload();
                            }
                        }); 
                    }
                } catch (error) {
                    console.error("Error al eliminar el evento:", error);
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Ha ocurrido un error al eliminar el evento. Por favor, inténtalo de nuevo",
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

        try {
            const response = await fetch("/events/" + eventId, {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json'
                },
                // Send the value directly as a string in JSON
                body: JSON.stringify(statusValue)
            });

            if (!response.ok) {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Ha ocurrido un error al actualizar el estado del evento. Por favor, inténtalo de nuevo",
                });
            } else {
                Swal.fire({
                    title: "Estado del evento actualizado con éxito!",
                    text: "Nuevo estado: ",
                    icon: "success"
                  });
                location.reload();
            }
        } catch (error) {
            console.error("Error al actualizar el evento:", error);
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Ha ocurrido un error al actualizar el estado del evento. Por favor, inténtalo de nuevo",
            });
        }
    });
}