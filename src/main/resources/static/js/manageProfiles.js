document.addEventListener('DOMContentLoaded', function() {
    // Funcionalidad para alternar visibilidad de contraseñas
    document.querySelectorAll('.toggle-password').forEach(icon => {
        icon.addEventListener('click', function() {
            const passwordInput = document.getElementById(this.getAttribute('data-target') || this.parentElement.querySelector('input').id);
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            this.classList.toggle('fa-eye');
            this.classList.toggle('fa-eye-slash');
        });
    });

    // Modal de edición
    const modal = document.getElementById('edit-modal');
    const closeBtn = document.querySelector('.close');

    // Cerrar el modal con el botón X
    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // Cerrar el modal haciendo clic fuera de él
    window.addEventListener('click', event => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Manejador para crear un nuevo usuario
    document.getElementById('new-user-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const userData = {
            username: document.getElementById('username').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            userType: document.getElementById('userType').value
        };

        try {
            const response = await fetch('/admin/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            const result = await response.json();

            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Usuario creado',
                    text: 'El usuario ha sido creado exitosamente'
                }).then(() => {
                    window.location.reload();
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: result.error || 'Error al crear el usuario'
                });
            }
        } catch (error) {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ha ocurrido un error en la operación'
            });
        }
    });

    // Manejador para abrir el modal de edición
    document.querySelectorAll('.btn-edit').forEach(button => {
        button.addEventListener('click', async function() {
            const userId = this.getAttribute('data-id');
            
            try {
                const response = await fetch(`/admin/users/${userId}`);
                const user = await response.json();
                
                document.getElementById('edit-user-id').value = user.id;
                document.getElementById('edit-username').value = user.username;
                document.getElementById('edit-email').value = user.email;
                document.getElementById('edit-password').value = '';
                document.getElementById('edit-userType').value = user.userType;
                
                modal.style.display = 'block';
            } catch (error) {
                console.error('Error:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Error al cargar los datos del usuario'
                });
            }
        });
    });

    // Manejador para guardar cambios del usuario
    document.getElementById('edit-user-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const userId = document.getElementById('edit-user-id').value;
        const userData = {
            username: document.getElementById('edit-username').value,
            email: document.getElementById('edit-email').value,
            userType: document.getElementById('edit-userType').value
        };
        
        const password = document.getElementById('edit-password').value;
        if (password) {
            userData.password = password;
        }
        try {
            const response = await fetch(`/admin/users/${userId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            const result = await response.json();

            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Usuario actualizado',
                    text: 'Los cambios han sido guardados',
                }).then(() => {
                    window.location.reload();
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: result.error || 'Error al actualizar el usuario'
                });
            }
        } catch (error) {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ha ocurrido un error en la operación'
            });
        }
    });

    // Manejador para eliminar usuario
    document.querySelectorAll('.btn-delete').forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-id');
            
            Swal.fire({
                title: '¿Estás seguro?',
                text: "No podrás revertir esta acción",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then(async (result) => {
                if (result.isConfirmed) {
                    try {
                        const response = await fetch(`/admin/users/${userId}`, {
                            method: 'DELETE'
                        });
                        
                        if (response.ok) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Usuario eliminado',
                                text: 'El usuario ha sido eliminado exitosamente'
                            }).then(() => {
                                window.location.reload();
                            });
                        } else {
                            const error = await response.json();
                            Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: error.error || 'Error al eliminar el usuario'
                            });
                        }
                    } catch (error) {
                        console.error('Error:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Ha ocurrido un error en la operación'
                        });
                    }
                }
            });
        });
    });

    // Filtro de usuarios
    document.getElementById('apply-filter').addEventListener('click', function() {
        const filterType = document.getElementById('filter-type').value;
        const searchText = document.getElementById('search-user').value.toLowerCase();
        
        const rows = document.querySelectorAll('#users-table-body tr');
        
        rows.forEach(row => {
            const userType = row.getAttribute('data-type');
            const username = row.cells[1].textContent.toLowerCase();
            const email = row.cells[2].textContent.toLowerCase();
            
            const matchesType = filterType === 'ALL' || userType === filterType;
            const matchesSearch = !searchText || 
                               username.includes(searchText) || 
                               email.includes(searchText);
            
            if (matchesType && matchesSearch) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });
});