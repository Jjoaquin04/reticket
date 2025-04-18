document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.event').forEach(event => {
        const ticketsInfo = event.querySelector('.tickets-info .tickets-count');
        const buyButton = event.querySelector('.buy-button-event');
        
        // Check if the number of tickets is 0
        if (parseInt(ticketsInfo.textContent) <= 0 || event.dataset.eventStatus === 'SOLD_OUT' || event.dataset.eventStatus === 'CANCELLED' || event.dataset.eventStatus === 'FINISHED') {
            buyButton.disabled = true;
            buyButton.textContent = event.dataset.eventStatus === 'SOLD_OUT' ? 'Agotado' : event.dataset.eventStatus === 'CANCELLED' ? 'Cancelado' : event.dataset.eventStatus === 'FINISHED' ? 'Finalizado' : 'No disponible';
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
                    Swal.fire({
                        title: "Ticket comprado con éxito!",
                        text: "Seguir comprando",
                        icon: "success"
                      });
                    
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Ha ocurrido un error al comprar el ticket",
                    });
                }
            }catch (error) {
                console.error('Error:', error); // Error handling
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Ha ocurrido un error al procesar la compra",
                });
            }
        });
    });
});