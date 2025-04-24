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
                    location.reload(); // Recargar la página para actualizar el carrito
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
                location.reload(); // Recargar la página para actualizar el carrito
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
                location.reload(); // Recargar la página para actualizar el carrito
            } else {
                console.error('Error al eliminar el producto del carrito');
            }
        })
        .catch(error => console.error('Error:', error));
    });
});