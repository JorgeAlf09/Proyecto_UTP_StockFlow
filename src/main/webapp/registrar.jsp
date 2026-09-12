<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.stockflow.model.Categoria" %>
<%@ page import="com.stockflow.dao.CategoriaDAO" %>
<%
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje");

    String codigo = (String) request.getAttribute("codigo");
    String nombre = (String) request.getAttribute("nombre");
    String categoriaId = (String) request.getAttribute("categoriaId");
    String descripcion = (String) request.getAttribute("descripcion");
    String cantidad = (String) request.getAttribute("cantidad");
    String precio = (String) request.getAttribute("precio");
    String estado = (String) request.getAttribute("estado");

    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    if (categorias == null) {
        categorias = new CategoriaDAO().listarTodas();
    }

    if (codigo == null) codigo = "";
    if (nombre == null) nombre = "";
    if (categoriaId == null) categoriaId = "";
    if (descripcion == null) descripcion = "";
    if (cantidad == null) cantidad = "";
    if (precio == null) precio = "";
    if (estado == null) estado = "";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Producto | StockFlow</title>
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
         4. REGISTRO DE PRODUCTO
         ============================================ -->
        <div class="titulo-pagina">
            <span>Registrar Producto</span>
            <a href="${pageContext.request.contextPath}/productos" class="volver">&larr; Volver al listado</a>
        </div>

        <form class="formulario" action="${pageContext.request.contextPath}/productos/nuevo" method="post" onsubmit="return validarFormulario();">
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
                                <%= Integer.toString(c.getIdCategoria()).equals(categoriaId) ? "selected" : "" %>>
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
                <button type="submit" class="boton boton-exito">Guardar</button>
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