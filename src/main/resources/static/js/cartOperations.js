import RequestFeedback from './feedBackAlert.js';

const eliminarbutton = document.querySelectorAll('.remove-button');

eliminarbutton.forEach(button => {
    button.addEventListener('click', function() {
        const id = this.getAttribute('key');
        const url = '/cartItems/' + id;
        fetch(url, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload(); // Recargar la página para actualizar el carrito
            } else {
                console.error('Error al eliminar el producto del carrito');
            }
        })
        .catch(error => console.error('Error:', error));
    });
});

const lessbutton = document.querySelectorAll('.less-button');

lessbutton.forEach(button => {
    button.addEventListener('click', function() {
        const id = this.getAttribute('data-i');
        const quantity = this.getAttribute('data-q');
        // Decrementar la cantidad en 1
        const newQuantity = parseInt(quantity) - 1;
        if (newQuantity < 1) {
            // Si la cantidad es menor a 1, eliminar el producto del carrito
            const url = '/cartItems/' + id;
            fetch(url, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    location.reload(); 
                } else {
                    console.error('Error al eliminar el producto del carrito');
                }
            })
            .catch(error => console.error('Error:', error));
            return;
        }

        const url = '/cartItems/' + id + '/' + newQuantity;
        fetch(url, {
            method: 'PATCH'
        })
        .then(response => {
            if (response.ok) {
                location.reload(); 
            } else {
                console.error('Error al eliminar el producto del carrito');
            }
        })
        .catch(error => console.error('Error:', error));
    });
});

const addbutton = document.querySelectorAll('.add-button');

addbutton.forEach(button => {
    button.addEventListener('click', function() {
        const id = this.getAttribute('data-i');
        const quantity = this.getAttribute('data-q');
        // Incrementar la cantidad en 1
        const newQuantity = parseInt(quantity) + 1;
        const url = '/cartItems/' + id + '/' + newQuantity;
        fetch(url, {
            method: 'PATCH'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                RequestFeedback.showError({
                    title: 'Error',
                    text: 'No se pudo añadir el producto al carrito',
                    showConfirmButton: true,
                });
                console.error('Error al añadir el producto al carrito');
            }
        })
        .catch(error => console.error('Error:', error));
    });
});

const checkoutButton = document.querySelector('.checkout-button');

checkoutButton.addEventListener('click', function() {
    RequestFeedback.showLoading({
        title: 'Procesando',
        text: 'Por favor espere...',
        allowOutsideClick: false,
    });

    const url = '/checkout';
    fetch(url, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            RequestFeedback.showSuccess({
                title: 'Éxito',
                text: 'Checkout procesado correctamente',
                timer: 1500,
                showConfirmButton: false,
            }).then(() => {
                location.reload(); // Recargar la página para mostrar el carrito
            });
        } else {
            RequestFeedback.showError({
                title: 'Error',
                text: 'No se pudo procesar el checkout',
                showConfirmButton: true,
            });
            console.error('Error al procesar el checkout');
        }
    })
    .catch(error => console.error('Error:', error));
});