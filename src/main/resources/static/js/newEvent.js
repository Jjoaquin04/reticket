document.getElementById("new-event-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Prevent default form submission

    // Get form data
    const formData = new FormData(this);

    // Convert FormData to an object and adapt types
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
        currenNumberOfTickets: parseInt(formData.get("currenNumberOfTickets")) // Convert to number
    };

    console.log(data); 
    console.log(JSON.stringify(data));

    // Send the POST request to the server
    try {
        const response = await fetch("/submitEvent", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const result = await response.json(); // Convert the response to JSON

        console.log(response);
        console.log(result); 

        if (response.ok) {
            alert("Evento creado con éxito!");
            window.location.href = "/events"; // Redirect to the events page
        } else {
            alert(result.error || "Error al crear el evento."); 
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Error al crear el evento.");
    }
});