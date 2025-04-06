document.getElementById("new-event-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Evita el envío del formulario por defecto

    // Obtener los datos del formulario
    const formData = new FormData(this);
    const data = Object.fromEntries(formData.entries());

    /**@RequestParam(required = true) String name,
        @RequestParam(required = true) String dateTime,
        @RequestParam(required = true) String location,
        @RequestParam(required = true) String venue,
        @RequestParam(required = true) String description,
        @RequestParam(required = true) String imageUrl,
        @RequestParam(required = true) String imageAltText,
        @RequestParam(required = true) EventType eventType, : concert, festival, theater, sports
        @RequestParam(required = true) EventStatus eventStatus, : aviable, calceled, finished, sold_out
        @RequestParam(required = true) int availableTickets */

    const eventObject = {
        name: data.name,
        dateTime: data.dateTime,
        location: data.location,
        venue: data.venue,
        description: data.description,
        imageUrl: data.imageUrl,
        imageAltText: data.imageAltText,
        eventType: data.eventType,
        eventStatus: data.eventStatus,
        availableTickets: data.availableTickets,
    };

    console.log(eventObject); // Muestra el objeto en la consola para depuración

    // Enviar la solicitud POST al servidor
    try {
        const response = await fetch("/submitEvent", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: eventObject
        });

        const result = await response.json(); // Convierte la respuesta a JSON

        console.log(response); // Muestra la respuesta en la consola para depuración
        console.log(result); // Muestra la respuesta en la consola para depuración

        if (response.ok) {
            alert("Evento creado con éxito!");
            window.location.href = "/events"; // Redirigir a la página de eventos
        } else {
            alert(result.error || "Error al crear el evento."); // Muestra un mensaje de error
        }
    } catch (error) {
        console.error("Error:", error); // Manejo de errores
        alert("Error al crear el evento.");
    }
});