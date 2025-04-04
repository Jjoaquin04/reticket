// Objeto para manejar las funciones del carrito
const shoppingCart = {
    // Inicializa el carrito en localStorage o crea uno vacío
    init: function() {
        this.cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
        this.updateCartCount();
        this.setupEventListeners();
    },

    // Configurar listeners para eventos
    setupEventListeners: function() {
        // Listener para cerrar el menú del carrito cuando se hace clic fuera
        document.addEventListener('click', function(event) {
            const cartMenu = document.getElementById('cart-menu');
            const cartIcon = document.getElementById('carrito');
            
            if (cartMenu && cartMenu.style.display === 'block' && 
                !cartMenu.contains(event.target) && 
                !cartIcon.contains(event.target)) {
                cartMenu.style.display = 'none';
            }
        });

        // Mostrar/ocultar el menú del carrito al hacer clic en el icono
        const cartIcon = document.getElementById('carrito');
        if (cartIcon) {
            cartIcon.addEventListener('click', function(event) {
                event.preventDefault();
                const cartMenu = document.getElementById('cart-menu');
                if (cartMenu.style.display === 'block') {
                    cartMenu.style.display = 'none';
                } else {
                    cartMenu.style.display = 'block';
                    shoppingCart.renderCartItems();
                }
            });
        }
    },

    // Añadir un evento al carrito
    addToCart: function(eventId, eventName, eventDate, eventVenue, eventPrice, eventImage) {
        const existingItemIndex = this.cartItems.findIndex(item => item.id === eventId);
        
        if (existingItemIndex !== -1) {
            this.cartItems[existingItemIndex].quantity += 1;
        } else {
            this.cartItems.push({
                id: eventId,
                name: eventName,
                date: eventDate,
                venue: eventVenue,
                price: eventPrice || 50, // Precio por defecto si no se proporciona
                image: eventImage,
                quantity: 1
            });
        }
        
        this.saveCart();
        this.updateCartCount();
        this.showAddedToCartMessage(eventName);
    },

    // Mostrar mensaje temporal de añadido al carrito
    showAddedToCartMessage: function(eventName) {
        const message = document.createElement('div');
        message.className = 'added-to-cart-message';
        message.innerHTML = `<p>"${eventName}" añadido al carrito</p>`;
        document.body.appendChild(message);
        
        setTimeout(() => {
            message.classList.add('fade-out');
            setTimeout(() => document.body.removeChild(message), 500);
        }, 2000);
    },

    // Eliminar un evento del carrito
    removeFromCart: function(eventId) {
        this.cartItems = this.cartItems.filter(item => item.id !== eventId);
        this.saveCart();
        this.updateCartCount();
        this.renderCartItems();
    },

    // Cambiar la cantidad de un item
    updateQuantity: function(eventId, change) {
        const index = this.cartItems.findIndex(item => item.id === eventId);
        if (index !== -1) {
            this.cartItems[index].quantity += change;
            
            if (this.cartItems[index].quantity <= 0) {
                this.cartItems.splice(index, 1);
            }
            
            this.saveCart();
            this.updateCartCount();
            this.renderCartItems();
        }
    },

    // Guardar el carrito en localStorage
    saveCart: function() {
        localStorage.setItem('cartItems', JSON.stringify(this.cartItems));
    },

    // Actualizar el contador de items en el carrito
    updateCartCount: function() {
        const cartCountElement = document.getElementById('cart-count');
        if (cartCountElement) {
            const totalItems = this.cartItems.reduce((total, item) => total + item.quantity, 0);
            cartCountElement.textContent = totalItems;
            cartCountElement.style.display = totalItems > 0 ? 'block' : 'none';
        }
    },

    // Renderizar los items del carrito en el menú desplegable
    renderCartItems: function() {
        const cartItemsContainer = document.getElementById('cart-items');
        if (!cartItemsContainer) return;

        cartItemsContainer.innerHTML = '';
        
        if (this.cartItems.length === 0) {
            cartItemsContainer.innerHTML = '<p class="empty-cart">Tu carrito está vacío</p>';
            document.getElementById('checkout-button').style.display = 'none';
            document.getElementById('cart-total').textContent = '0.00 €';
            return;
        }
        
        let totalPrice = 0;
        
        this.cartItems.forEach(item => {
            const itemPrice = item.price * item.quantity;
            totalPrice += itemPrice;
            
            const cartItem = document.createElement('div');
            cartItem.className = 'cart-item';
            cartItem.innerHTML = `
                <img src="${item.image}" alt="${item.name}">
                <div class="cart-item-info">
                    <h4>${item.name}</h4>
                    <p>${item.venue}</p>
                    <p>${item.date}</p>
                    <div class="cart-item-quantity">
                        <button class="quantity-btn minus" data-id="${item.id}">-</button>
                        <span>${item.quantity}</span>
                        <button class="quantity-btn plus" data-id="${item.id}">+</button>
                    </div>
                    <p class="cart-item-price">${itemPrice.toFixed(2)} €</p>
                </div>
                <button class="remove-item-btn" data-id="${item.id}">×</button>
            `;
            
            cartItemsContainer.appendChild(cartItem);
        });
        
        // Actualizar el precio total
        document.getElementById('cart-total').textContent = `${totalPrice.toFixed(2)} €`;
        document.getElementById('checkout-button').style.display = 'block';
        
        // Agregar event listeners a los botones
        cartItemsContainer.querySelectorAll('.remove-item-btn').forEach(button => {
            button.addEventListener('click', function() {
                const eventId = this.getAttribute('data-id');
                shoppingCart.removeFromCart(eventId);
            });
        });
        
        cartItemsContainer.querySelectorAll('.quantity-btn').forEach(button => {
            button.addEventListener('click', function() {
                const eventId = this.getAttribute('data-id');
                const change = this.classList.contains('plus') ? 1 : -1;
                shoppingCart.updateQuantity(eventId, change);
            });
        });
    },

    // Vaciar el carrito
    clearCart: function() {
        this.cartItems = [];
        this.saveCart();
        this.updateCartCount();
        this.renderCartItems();
    }
};

// Inicializar el carrito cuando el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    shoppingCart.init();
    
    // Agregar event listeners a todos los botones "Comprar"
    document.querySelectorAll('.buy-button-event').forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            
            let eventContainer;
            if (this.closest('.event-info')) {
                // Estamos en la página de eventos (layout horizontal)
                eventContainer = this.closest('.event');
                const infoContainer = eventContainer.querySelector('.event-info');
                
                const eventId = eventContainer.getAttribute('data-event-id');
                const eventName = infoContainer.querySelector('.title-event').textContent;
                const eventDate = infoContainer.querySelector('.date-event').textContent;
                const eventVenue = infoContainer.querySelector('.location-event').textContent;
                const eventImage = eventContainer.querySelector('img').src;
                
                shoppingCart.addToCart(eventId, eventName, eventDate, eventVenue, null, eventImage);
            } else {
                // Estamos en la página principal (layout vertical)
                eventContainer = this.closest('.event');
                
                const eventId = eventContainer.getAttribute('data-event-id');
                const eventName = eventContainer.querySelector('.title-event').textContent;
                const eventDate = eventContainer.querySelector('.date-event').textContent;
                const eventVenue = eventContainer.querySelector('.location-event').textContent;
                const eventImage = eventContainer.querySelector('img').src;
                
                shoppingCart.addToCart(eventId, eventName, eventDate, eventVenue, null, eventImage);
            }
        });
    });

    // Event listener para el botón de checkout
    const checkoutButton = document.getElementById('checkout-button');
    if (checkoutButton) {
        checkoutButton.addEventListener('click', function() {
            window.location.href = '/checkout';
        });
    }
});