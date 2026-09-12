// ============================================
// STOCKFLOW - JavaScript principal
// ============================================

document.addEventListener('DOMContentLoaded', function () {
    configurarModalEliminar();
    configurarCierreAutomaticoMensajes();
});

// ============================================
// Modal de confirmación para eliminar producto
// ============================================

function configurarModalEliminar() {
    var overlay = document.getElementById('modalEliminar');
    var enlacesEliminar = document.querySelectorAll('.eliminar-producto');
    var botonConfirmar = document.getElementById('confirmarEliminar');
    var botonCancelar = document.getElementById('cancelarEliminar');
    var nombreProducto = document.getElementById('nombreProductoEliminar');
    var enlaceDestino = document.getElementById('enlaceEliminarDestino');

    if (!overlay || enlacesEliminar.length === 0) {
        return;
    }

    enlacesEliminar.forEach(function (enlace) {
        enlace.addEventListener('click', function (evento) {
            evento.preventDefault();
            var nombre = enlace.getAttribute('data-nombre');
            var url = enlace.getAttribute('href');
            if (nombreProducto) {
                nombreProducto.textContent = nombre;
            }
            enlaceDestino.setAttribute('href', url);
            overlay.classList.add('visible');
        });
    });

    if (botonConfirmar) {
        botonConfirmar.addEventListener('click', function () {
            window.location.href = enlaceDestino.getAttribute('href');
        });
    }

    if (botonCancelar) {
        botonCancelar.addEventListener('click', cerrarModal);
    }

    overlay.addEventListener('click', function (evento) {
        if (evento.target === overlay) {
            cerrarModal();
        }
    });

    document.addEventListener('keydown', function (evento) {
        if (evento.key === 'Escape' && overlay.classList.contains('visible')) {
            cerrarModal();
        }
    });
}

function cerrarModal() {
    var overlay = document.getElementById('modalEliminar');
    if (overlay) {
        overlay.classList.remove('visible');
    }
}

// ============================================
// Cierre automático de mensajes
// ============================================

function configurarCierreAutomaticoMensajes() {
    var mensajes = document.querySelectorAll('.mensaje');
    mensajes.forEach(function (mensaje) {
        setTimeout(function () {
            if (mensaje.parentNode) {
                mensaje.style.transition = 'opacity 0.5s';
                mensaje.style.opacity = '0';
                setTimeout(function () {
                    if (mensaje.parentNode) {
                        mensaje.parentNode.removeChild(mensaje);
                    }
                }, 500);
            }
        }, 4500);
    });
}

// ============================================
// Validación básica del formulario
// ============================================

function validarFormulario() {
    var codigo = document.getElementById('codigo');
    var nombre = document.getElementById('nombre');
    var categoria = document.getElementById('categoria');
    var cantidad = document.getElementById('cantidad');
    var precio = document.getElementById('precio');
    var estado = document.getElementById('estado');
    var valido = true;

    var campos = [codigo, nombre, categoria, cantidad, precio, estado];
    campos.forEach(function (campo) {
        var mensajeError = document.getElementById('error-' + campo.id);
        if (!campo.value || campo.value.trim() === '') {
            campo.classList.add('campo-invalido');
            if (mensajeError) {
                mensajeError.textContent = 'Este campo es obligatorio.';
                mensajeError.style.display = 'block';
            }
            valido = false;
        } else {
            campo.classList.remove('campo-invalido');
            if (mensajeError) {
                mensajeError.textContent = '';
                mensajeError.style.display = 'none';
            }
        }
    });

    if (cantidad && cantidad.value.trim() !== '' && (parseInt(cantidad.value, 10) < 0)) {
        var errorCantidad = document.getElementById('error-cantidad');
        if (errorCantidad) {
            errorCantidad.textContent = 'La cantidad no puede ser negativa.';
            errorCantidad.style.display = 'block';
        }
        valido = false;
    }

    if (precio && precio.value.trim() !== '' && (parseFloat(precio.value) < 0)) {
        var errorPrecio = document.getElementById('error-precio');
        if (errorPrecio) {
            errorPrecio.textContent = 'El precio no puede ser negativo.';
            errorPrecio.style.display = 'block';
        }
        valido = false;
    }

    return valido;
}