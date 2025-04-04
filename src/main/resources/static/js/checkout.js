document.addEventListener('DOMContentLoaded', function() {
    // Asegurarnos de que shoppingCart esté inicializado
    if (window.shoppingCart) {
        shoppingCart.init();
    }

    // Obtener los items del carrito desde localStorage
    const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
    const checkoutItemsList = document.getElementById('checkout-items-list');
    
    if (cartItems.length === 0) {
        // Si el carrito está vacío, mostrar mensaje
        checkoutItemsList.innerHTML = `
            <div class="empty-checkout">
                <p>Tu carrito está vacío.</p>
                <a href="/" class="continue-shopping">Continuar comprando</a>
            </div>
        `;
        document.getElementById('checkout-subtotal').textContent = '0.00 €';
        document.getElementById('checkout-total').textContent = '3.95 €';
        document.getElementById('proceed-payment').disabled = true;
        document.getElementById('proceed-payment').style.opacity = 0.5;
        return;
    }
    
    // Renderizar items con botones de cantidad
    let subtotal = 0;
    cartItems.forEach(item => {
        const itemTotal = item.price * item.quantity;
        subtotal += itemTotal;
        
        const itemElement = document.createElement('div');
        itemElement.className = 'checkout-item';
        itemElement.setAttribute('data-id', item.id);
        itemElement.innerHTML = `
            <img src="${item.image}" alt="${item.name}">
            <div class="checkout-item-info">
                <h3>${item.name}</h3>
                <p>${item.venue}</p>
                <p>${item.date}</p>
                <div class="checkout-quantity-controls">
                    <button class="checkout-quantity-btn minus" data-id="${item.id}">-</button>
                    <span>${item.quantity}</span>
                    <button class="checkout-quantity-btn plus" data-id="${item.id}">+</button>
                </div>
                <div class="checkout-quantity-price">
                    <span>Precio unitario: ${item.price.toFixed(2)} €</span>
                    <span>Total: ${itemTotal.toFixed(2)} €</span>
                </div>
                <button class="checkout-remove" data-id="${item.id}">Eliminar</button>
            </div>
        `;
        
        checkoutItemsList.appendChild(itemElement);
    });
    
    // Agregar event listeners a los botones de eliminar
    document.querySelectorAll('.checkout-remove').forEach(button => {
        button.addEventListener('click', function() {
            const eventId = this.getAttribute('data-id');
            removeItemFromCheckout(eventId);
        });
    });

    // Agregar event listeners a los botones de cantidad
    document.querySelectorAll('.checkout-quantity-btn').forEach(button => {
        button.addEventListener('click', function() {
            const eventId = this.getAttribute('data-id');
            const change = this.classList.contains('plus') ? 1 : -1;
            updateQuantityInCheckout(eventId, change);
        });
    });
    
    // Calcular y mostrar totales
    const fee = 3.95;
    document.getElementById('checkout-subtotal').textContent = `${subtotal.toFixed(2)} €`;
    document.getElementById('checkout-total').textContent = `${(subtotal + fee).toFixed(2)} €`;
    
    // Event listener para el botón de pago
    document.getElementById('proceed-payment').addEventListener('click', function() {
        // Aquí iría la lógica para procesar el pago
        alert('¡Gracias por tu compra! Esta es una simulación, no se ha realizado ningún cargo.');
        // Limpiar el carrito después de la compra
        localStorage.removeItem('cartItems');
        window.location.href = '/';
    });
});

// Función para eliminar un item del checkout
function removeItemFromCheckout(eventId) {
    // Obtener los items del carrito
    let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
    
    // Filtrar el item a eliminar
    cartItems = cartItems.filter(item => item.id !== eventId);
    
    // Guardar el carrito actualizado
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
    
    // Eliminar el elemento del DOM
    const itemElement = document.querySelector(`.checkout-item[data-id="${eventId}"]`);
    if (itemElement) {
        itemElement.remove();
    }
    
    // Recalcular totales
    let subtotal = cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    const fee = 3.95;
    document.getElementById('checkout-subtotal').textContent = `${subtotal.toFixed(2)} €`;
    document.getElementById('checkout-total').textContent = `${(subtotal + fee).toFixed(2)} €`;
    
    // Si no quedan items, mostrar mensaje de carrito vacío
    if (cartItems.length === 0) {
        const checkoutItemsList = document.getElementById('checkout-items-list');
        checkoutItemsList.innerHTML = `
            <div class="empty-checkout">
                <p>Tu carrito está vacío.</p>
                <a href="/" class="continue-shopping">Continuar comprando</a>
            </div>
        `;
        document.getElementById('proceed-payment').disabled = true;
        document.getElementById('proceed-payment').style.opacity = 0.5;
    }
    
    // Actualizar el contador y menú del carrito si shoppingCart está disponible
    if (window.shoppingCart) {
        shoppingCart.cartItems = cartItems;
        shoppingCart.updateCartCount();
        shoppingCart.renderCartItems();
    }
}

// Nueva función para actualizar la cantidad en el checkout
function updateQuantityInCheckout(eventId, change) {
    // Obtener los items del carrito
    let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
    
    // Encontrar el item a actualizar
    const index = cartItems.findIndex(item => item.id === eventId);
    
    if (index !== -1) {
        // Actualizar la cantidad
        cartItems[index].quantity += change;
        
        // Si la cantidad llega a cero o menos, eliminar el item
        if (cartItems[index].quantity <= 0) {
            return removeItemFromCheckout(eventId);
        }
        
        // Actualizar el DOM
        const itemElement = document.querySelector(`.checkout-item[data-id="${eventId}"]`);
        if (itemElement) {
            // Actualizar el contador de cantidad
            const quantitySpan = itemElement.querySelector('.checkout-quantity-controls span');
            quantitySpan.textContent = cartItems[index].quantity;
            
            // Actualizar el precio total del item
            const itemTotal = cartItems[index].price * cartItems[index].quantity;
            const priceInfo = itemElement.querySelector('.checkout-quantity-price');
            priceInfo.innerHTML = `
                <span>Precio unitario: ${cartItems[index].price.toFixed(2)} €</span>
                <span>Total: ${itemTotal.toFixed(2)} €</span>
            `;
        }
        
        // Guardar el carrito actualizado
        localStorage.setItem('cartItems', JSON.stringify(cartItems));
        
        // Recalcular totales
        let subtotal = cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
        const fee = 3.95;
        document.getElementById('checkout-subtotal').textContent = `${subtotal.toFixed(2)} €`;
        document.getElementById('checkout-total').textContent = `${(subtotal + fee).toFixed(2)} €`;
        
        // Actualizar el contador y menú del carrito si shoppingCart está disponible
        if (window.shoppingCart) {
            shoppingCart.cartItems = cartItems;
            shoppingCart.updateCartCount();
            shoppingCart.renderCartItems();
        }
    }
}