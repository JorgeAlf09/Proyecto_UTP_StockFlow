<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.stockflow.model.Producto" %>
<%@ page import="com.stockflow.model.Categoria" %>
<%@ page import="com.stockflow.dao.CategoriaDAO" %>
<%
    Producto producto = (Producto) request.getAttribute("producto");

    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje");

    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    if (categorias == null) {
        categorias = new CategoriaDAO().listarTodas();
    }

    int idProducto = 0;
    int idCategoriaProducto = 0;
    String codigo = "";
    String nombre = "";
    String descripcion = "";
    String cantidad = "";
    String precio = "";
    String estado = "";

    if (producto != null) {
        idProducto = producto.getId();
        idCategoriaProducto = producto.getIdCategoria();
        codigo = producto.getCodigo() != null ? producto.getCodigo() : "";
        nombre = producto.getNombre() != null ? producto.getNombre() : "";
        descripcion = producto.getDescripcion() != null ? producto.getDescripcion() : "";
        cantidad = String.valueOf(producto.getCantidad());
        precio = String.valueOf(producto.getPrecio());
        estado = producto.getEstado() != null ? producto.getEstado() : "";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Producto | StockFlow</title>
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
         5. EDICIÓN DE PRODUCTO
         ============================================ -->
        <div class="titulo-pagina">
            <span>Editar Producto</span>
            <a href="${pageContext.request.contextPath}/productos" class="volver">&larr; Volver al listado</a>
        </div>

        <form class="formulario" action="${pageContext.request.contextPath}/productos/editar" method="post" onsubmit="return validarFormulario();">
            <input type="hidden" name="id" value="<%= idProducto %>">

            <div class="formulario-grid">

                <div class="campo-formulario">
                    <label for="codigo">Código <span>*</span></label>
                    <input type="text" id="codigo" name="codigo" maxlength="20"
                           placeholder="Ej: PRD-001" value="<%= codigo %>">
                    <span class="error-mensaje" id="error-codigo"></span>
                </div>

                <div class="campo-formulario">
                    <label for="nombre">Nombre <span>*</span></label>
                    <input type="text" id="nombre" name="nombre" maxlength="100"
                           placeholder="Ej: Laptop HP Pavilion" value="<%= nombre %>">
                    <span class="error-mensaje" id="error-nombre"></span>
                </div>

                <div class="campo-formulario">
                    <label for="categoria">Categoría <span>*</span></label>
                    <select id="categoria" name="idCategoria">
                        <option value="">-- Seleccionar categoría --</option>
                        <% for (Categoria c : categorias) { %>
                            <option value="<%= c.getIdCategoria() %>"
                                <%= (producto != null && producto.getIdCategoria() == c.getIdCategoria()) ? "selected" : "" %>>
                                <%= c.getNombre() %>
                            </option>
                        <% } %>
                    </select>
                    <span class="error-mensaje" id="error-categoria"></span>
                </div>

                <div class="campo-formulario">
                    <label for="estado">Estado <span>*</span></label>
                    <select id="estado" name="estado">
                        <option value="">-- Seleccionar estado --</option>
                        <option value="Activo" <%= "Activo".equals(estado) ? "selected" : "" %>>Activo</option>
                        <option value="Inactivo" <%= "Inactivo".equals(estado) ? "selected" : "" %>>Inactivo</option>
                    </select>
                    <span class="error-mensaje" id="error-estado"></span>
                </div>

                <div class="campo-formulario">
                    <label for="cantidad">Cantidad <span>*</span></label>
                    <input type="number" id="cantidad" name="cantidad" min="0" step="1"
                           placeholder="Ej: 25" value="<%= cantidad %>">
                    <span class="error-mensaje" id="error-cantidad"></span>
                </div>

                <div class="campo-formulario">
                    <label for="precio">Precio (S/) <span>*</span></label>
                    <input type="number" id="precio" name="precio" min="0" step="0.01"
                           placeholder="Ej: 199.99" value="<%= precio %>">
                    <span class="error-mensaje" id="error-precio"></span>
                </div>

                <div class="campo-formulario campo-completo">
                    <label for="descripcion">Descripción</label>
                    <textarea id="descripcion" name="descripcion" rows="3" maxlength="250"
                              placeholder="Breve descripción del producto..."><%= descripcion %></textarea>
                </div>

            </div>

            <div class="acciones-formulario">
                <button type="submit" class="boton boton-exito">Guardar cambios</button>
                <a href="${pageContext.request.contextPath}/productos" class="boton boton-secundario">Cancelar</a>
            </div>
        </form>

    </main>

    <footer class="pie">
        <p>StockFlow &mdash; Sistema Web de Gestión de Almacén</p>
    </footer>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>