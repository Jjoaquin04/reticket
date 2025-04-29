document.getElementById("new-event-form").addEventListener("submit", async function(event) {
    event.preventDefault(); 

   
    const formData = new FormData(this);

    // Converir los datos del formulario a un objeto
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
        currenNumberOfTickets: parseInt(formData.get("currenNumberOfTickets")),
        price: parseFloat(formData.get("price")),
    };
    try {
       
        const loadingSwal = RequestFeedback.showLoading({
            title: 'Creando evento',
            text: 'Guardando información del evento...'
        });
        
        const response = await fetch("/submitEvent", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        
        loadingSwal.close();

        if (response.ok) {
            RequestFeedback.showSuccess({
                title: "¡Éxito!",
                text: "Evento creado correctamente",
                confirmButtonText: "Continuar"
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "/events";
                }
            });
        } else {
            const errorData = await response.json();
            RequestFeedback.showError({
                title: "Error al crear el evento",
                text: errorData.error || "Verifica los datos ingresados"
            });
        }
    } catch (error) {
        console.error("Error:", error);
        RequestFeedback.showError({
            title: "Error de conexión",
            text: "No se pudo conectar con el servidor"
        });
    }
});