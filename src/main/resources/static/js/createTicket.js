document.addEventListener('DOMContentLoaded', function() {

    document.querySelectorAll('.event').forEach(event => {
        const ticketsInfo = event.querySelector('.tickets-info .tickets-count');
        const buyButton = event.querySelector('.buy-button-event');
        
        // Check if the number of tickets is 0
        if (parseInt(ticketsInfo.textContent) <= 0) {
            buyButton.disabled = true;
            buyButton.textContent = 'Agotado';
        }
    });

    document.querySelectorAll('.buy-button-event').forEach(button => {
        button.addEventListener('click', async function() {

            const eventId = this.closest(".event").dataset.eventId;
            try{
                const response = await fetch(`/buyTicket/${eventId}`, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                });

                if (response.ok) {

                    const ticketsInfo = this.closest('.event').querySelector('.tickets-info .tickets-count');
                    const currentText = ticketsInfo.textContent;
                    const currentCount = parseInt(currentText);
                    const newCount = currentCount - 1;
                    ticketsInfo.textContent = `${newCount}`;
                    
                    // Disable the button if no tickets are available
                    if (newCount <= 0) {
                        this.disabled = true;
                        this.textContent = 'Agotado';
                    }
                    alert('Ticket comprado con éxito!'); // Muestra un mensaje de éxito
                } else {
                    alert('Error al comprar el ticket.'); // Muestra un mensaje de error
                }
            }catch (error) {
                console.error('Error:', error); // Manejo de errores
                alert('Error al procesar la compra.'); // Muestra un mensaje de error
            }
        });
    });
});