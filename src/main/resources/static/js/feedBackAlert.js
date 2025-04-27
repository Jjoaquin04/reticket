
const RequestFeedback = {
    
    showLoading: function(options = {}) {
        const config = {
            title: options.title || 'Procesando',
            text: options.text || 'Por favor espere...',
            allowOutsideClick: options.allowOutsideClick || false,
            
        };
        
        return Swal.fire({
            title: config.title,
            text: config.text,
            allowOutsideClick: config.allowOutsideClick,
            heightAuto: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });
    },
    
    showSuccess: function(options = {}) {
        const config = {
            title: options.title || 'Éxito',
            text: options.text || 'Operación completada correctamente',
            timer: options.timer,
            showConfirmButton: options.showConfirmButton !== false,
            position: options.position || 'center',
        };
        
        return Swal.fire({
            icon: 'success',
            title: config.title,
            text: config.text,
            timer: config.timer,
            showConfirmButton: config.showConfirmButton,
            position: config.position,
            heightAuto: false,
        });
    },
    
    showError: function(options = {}) {
        const config = {
            title: options.title || 'Error',
            text: options.text || 'Ha ocurrido un error',
            showConfirmButton: options.showConfirmButton !== false,
        };
        
        return Swal.fire({
            icon: 'error',
            title: config.title,
            text: config.text,
            showConfirmButton: config.showConfirmButton,
            heightAuto: false,
        });
    },

    showInfo: function(options = {}) {
        const config = {
            title: options.title || 'Información',
            text: options.text || 'Información importante',
            showConfirmButton: options.showConfirmButton !== false
        };
        
        return Swal.fire({
            icon: 'info',
            title: config.title,
            text: config.text,
            showConfirmButton: config.showConfirmButton,
        });
    },

    showConfirm: function(options = {}) {
        const config = {
            title: options.title || '¿Estás seguro?',
            text: options.text || '¿Deseas continuar con esta acción?',
            icon: options.icon || 'warning',
            showCancelButton: true,
            confirmButtonColor: options.confirmButtonColor || '#3085d6',
            cancelButtonColor: options.cancelButtonColor || '#d33',
            confirmButtonText: options.confirmButtonText || 'Sí',
            cancelButtonText: options.cancelButtonText || 'Cancelar'
        };
        
        return Swal.fire(config);
    }
};

export default RequestFeedback;