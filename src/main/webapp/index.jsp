<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.stockflow.model.Producto" %>
<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    if (productos == null) {
        productos = new java.util.ArrayList<Producto>();
    }
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje");
    String terminoBusqueda = (String) request.getAttribute("terminoBusqueda");
    if (terminoBusqueda == null) {
        terminoBusqueda = "";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Productos | StockFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

    <!-- ============================================
         1. ENCABEZADO
         ============================================ -->
    <header class="encabezado">
        <div class="contenedor">
            <div class="logo">SF</div>
            <div class="encabezado-titulos">
                <h1>StockFlow</h1>
                <p>Sistema Web de Gestión de Almacén</p>
            </div>
        </div>
    </header>

    <main class="contenedor">

        <!-- ============================================
         Mensajes
         ============================================ -->
        <% if (mensaje != null && tipoMensaje != null) { %>
            <div class="mensaje mensaje-<%= tipoMensaje %>">
                <%= mensaje %>
            </div>
        <% } %>

        <!-- ============================================
         2. ÁREA DE ACCIONES
         ============================================ -->
        <div class="barra-acciones">
            <form class="buscador" action="${pageContext.request.contextPath}/productos/buscar" method="get">
                <input type="text" name="termino" class="campo-busqueda" placeholder="Buscar por código o nombre..."
                       value="<%= terminoBusqueda %>" aria-label="Buscar producto">
                <button type="submit" class="boton boton-primario buscar-boton">Buscar</button>
                <a href="${pageContext.request.contextPath}/productos" class="boton boton-actualizar" title="Mostrar todos los productos">Actualizar</a>
            </form>
            <div class="botones-acciones-derecha">
                <a href="${pageContext.request.contextPath}/productos/nuevo" class="boton boton-exito">+ Nuevo Producto</a>
            </div>
        </div>

        <!-- ============================================
         3. LISTADO DE PRODUCTOS
         ============================================ -->
        <div class="tarjeta">
            <div class="tabla-contenedor">
                <% if (productos.isEmpty()) { %>
                    <div class="sin-productos">
                        <span class="icono">&#128722;</span>
                        <p>No se encontraron productos.</p>
                    </div>
                <% } else { %>
                    <table class="tabla-productos">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Nombre</th>
                                <th>Categoría</th>
                                <th>Descripción</th>
                                <th>Cantidad</th>
                                <th>Precio</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Producto p : productos) { %>
                                <tr>
                                    <td><%= p.getCodigo() %></td>
                                    <td><%= p.getNombre() %></td>
                                    <td><%= p.getCategoria() %></td>
                                    <td><%= p.getDescripcion() %></td>
                                    <td><%= p.getCantidad() %></td>
                                    <td>S/ <%= String.format(java.util.Locale.US, "%.2f", p.getPrecio()) %></td>
                                    <td class="col-estado">
                                        <% if ("Activo".equals(p.getEstado())) { %>
                                            <span class="insignia insignia-activo">Activo</span>
                                        <% } else { %>
                                            <span class="insignia insignia-inactivo">Inactivo</span>
                                        <% } %>
                                    </td>
                                    <td class="col-acciones">
                                        <a href="${pageContext.request.contextPath}/productos/editar?id=<%= p.getId() %>" class="boton boton-tabla boton-editar">Editar</a>
                                        <a href="${pageContext.request.contextPath}/productos/eliminar?id=<%= p.getId() %>"
                                           class="boton boton-tabla boton-eliminar eliminar-producto"
                                           data-nombre="<%= p.getNombre() %>">Eliminar</a>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
            </div>
        </div>

    </main>

    <footer class="pie">
        <p>StockFlow &mdash; Sistema Web de Gestión de Almacén</p>
    </footer>

    <!-- ============================================
         Modal de confirmación para eliminar
         ============================================ -->
    <div class="overlay" id="modalEliminar">
        <div class="modal">
            <div class="icono-pregunta">?</div>
            <h3>Eliminar producto</h3>
            <p>¿Está seguro de eliminar este producto? <strong id="nombreProductoEliminar"></strong></p>
            <div class="modal-acciones">
                <a id="enlaceEliminarDestino" href="#" class="boton boton-peligro">Confirmar</a>
                <button type="button" id="cancelarEliminar" class="boton boton-secundario">Cancelar</button>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>