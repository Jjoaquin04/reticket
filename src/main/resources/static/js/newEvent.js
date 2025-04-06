document.getElementById("new-event-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Evita el envío del formulario por defecto

    // Obtener los datos del formulario
    const formData = new FormData(this);

    // Convertir FormData a objeto y adaptar tipos
    const data = {
        name: formData.get("name"),
        date: new Date(formData.get("date")),
        location: formData.get("location"),
        venue: formData.get("venue"),
        description: formData.get("description"),
        imageURL: formData.get("imageURL"),
        altImage: formData.get("altImage"),
        eventType: formData.get("eventType"), // CONCERT, THEATER, etc.
        eventStatus: formData.get("eventStatus"), // AVAILABLE, CANCELLED, etc.
        currenNumberOfTickets: parseInt(formData.get("currenNumberOfTickets")) // Convertir a número
    };

    console.log(data); // Muestra el objeto en la consola para depuración
    console.log(JSON.stringify(data)); // Muestra el objeto en la consola para depuración

    // Enviar la solicitud POST al servidor
    try {
        const response = await fetch("/submitEvent", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
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