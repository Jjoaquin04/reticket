
const eliminar_button = document.getElementsByClassName("eliminar-button"); // Get all elements with the class "eliminar-button"

// Loop through each button and add a click event listener
for (let i = 0; i < eliminar_button.length; i++) {
    eliminar_button[i].addEventListener("click", async function() {
        // Get the ID of the event to be deleted from the button's data attribute
        const eventId = this.getAttribute("key");

        // Show a confirmation dialog to the user
        const confirm = window.confirm("¿Estás seguro de que deseas eliminar este evento?");

        if (confirm) {
            try{
                const response = await fetch("/events/" + eventId, {
                    method: "DELETE",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

                console.log(response);

                if (!response.ok) {
                    alert("Error al eliminar el evento. Por favor, inténtalo de nuevo.");
                }
            } catch (error) {
                console.error("Error al eliminar el evento:", error);
                alert("Ocurrió un error al intentar eliminar el evento. Por favor, inténtalo de nuevo más tarde.");
            }finally {
                // Reload the page to reflect the changes
                location.reload();
            }
        }
    });
}