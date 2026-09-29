package com.stockflow.servlet;

import com.stockflow.dao.CategoriaDAO;
import com.stockflow.dao.IDAO;
import com.stockflow.dao.IProductoDAO;
import com.stockflow.dao.ProductoDAO;
import com.stockflow.model.Categoria;
import com.stockflow.model.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductoServlet extends HttpServlet {
    private IProductoDAO dao;
    private IDAO<Categoria> categoriaDao;

    @Override
    public void init() throws ServletException {
        dao = new ProductoDAO();
        categoriaDao = new CategoriaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        switch (path) {
            case "/productos/nuevo":
                cargarCategorias(request);
                request.getRequestDispatcher("/registrar.jsp").forward(request, response);
                break;

            case "/productos/editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Producto productoEditar = dao.buscarPorId(idEditar);
                if (productoEditar != null) {
                    cargarCategorias(request);
                    request.setAttribute("producto", productoEditar);
                    request.getRequestDispatcher("/editar.jsp").forward(request, response);
                } else {
                    request.setAttribute("mensaje", "Producto no encontrado.");
                    request.setAttribute("tipoMensaje", "error");
                    listarProductos(request, response);
                }
                break;

            case "/productos/eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                if (dao.eliminar(idEliminar)) {
                    request.setAttribute("mensaje", "Producto eliminado correctamente.");
                    request.setAttribute("tipoMensaje", "exito");
                } else {
                    request.setAttribute("mensaje", "Error al eliminar el producto.");
                    request.setAttribute("tipoMensaje", "error");
                }
                listarProductos(request, response);
                break;

            case "/productos/buscar":
                String termino = request.getParameter("termino");
                if (termino != null && !termino.trim().isEmpty()) {
                    List<Producto> resultados = dao.buscar(termino.trim());
                    request.setAttribute("productos", resultados);
                    request.setAttribute("terminoBusqueda", termino);
                    if (resultados.isEmpty()) {
                        request.setAttribute("mensaje", "No se encontraron productos.");
                        request.setAttribute("tipoMensaje", "info");
                    }
                } else {
                    listarProductos(request, response);
                    return;
                }
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;

            default:
                listarProductos(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        if (path.equals("/productos/nuevo")) {
            String codigo = request.getParameter("codigo");
            String nombre = request.getParameter("nombre");
            String categoriaIdStr = request.getParameter("idCategoria");
            String descripcion = request.getParameter("descripcion");
            String cantidadStr = request.getParameter("cantidad");
            String precioStr = request.getParameter("precio");
            String estado = request.getParameter("estado");

            if (camposObligatoriosVacios(codigo, nombre, categoriaIdStr, cantidadStr, precioStr, estado)) {
                cargarCategorias(request);
                request.setAttribute("mensaje", "Complete los campos obligatorios.");
                request.setAttribute("tipoMensaje", "error");
                rellenarFormularioRegistro(request, codigo, nombre, categoriaIdStr, descripcion, cantidadStr, precioStr, estado);
                request.getRequestDispatcher("/registrar.jsp").forward(request, response);
                return;
            }

            if (dao.existeCodigo(codigo.trim())) {
                cargarCategorias(request);
                request.setAttribute("mensaje", "El código ya existe.");
                request.setAttribute("tipoMensaje", "error");
                rellenarFormularioRegistro(request, codigo, nombre, categoriaIdStr, descripcion, cantidadStr, precioStr, estado);
                request.getRequestDispatcher("/registrar.jsp").forward(request, response);
                return;
            }

            Producto p = new Producto();
            p.setCodigo(codigo.trim());
            p.setNombre(nombre.trim());
            p.setIdCategoria(Integer.parseInt(categoriaIdStr));
            p.setDescripcion(descripcion != null ? descripcion.trim() : "");
            p.setCantidad(Integer.parseInt(cantidadStr));
            p.setPrecio(Double.parseDouble(precioStr));
            p.setEstado(estado.trim());

            if (dao.insertar(p)) {
                request.setAttribute("mensaje", "Producto registrado correctamente.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "Error al registrar el producto.");
                request.setAttribute("tipoMensaje", "error");
            }
            listarProductos(request, response);

        } else if (path.equals("/productos/editar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String codigo = request.getParameter("codigo");
            String nombre = request.getParameter("nombre");
            String categoriaIdStr = request.getParameter("idCategoria");
            String descripcion = request.getParameter("descripcion");
            String cantidadStr = request.getParameter("cantidad");
            String precioStr = request.getParameter("precio");
            String estado = request.getParameter("estado");

            Producto producto = dao.buscarPorId(id);

            if (camposObligatoriosVacios(codigo, nombre, categoriaIdStr, cantidadStr, precioStr, estado)) {
                cargarCategorias(request);
                request.setAttribute("mensaje", "Complete los campos obligatorios.");
                request.setAttribute("tipoMensaje", "error");
                request.setAttribute("producto", producto);
                request.getRequestDispatcher("/editar.jsp").forward(request, response);
                return;
            }

            if (dao.existeCodigoExcluyendo(codigo.trim(), id)) {
                cargarCategorias(request);
                request.setAttribute("mensaje", "El código ya existe.");
                request.setAttribute("tipoMensaje", "error");
                request.setAttribute("producto", producto);
                request.getRequestDispatcher("/editar.jsp").forward(request, response);
                return;
            }

            Producto p = new Producto();
            p.setId(id);
            p.setCodigo(codigo.trim());
            p.setNombre(nombre.trim());
            p.setIdCategoria(Integer.parseInt(categoriaIdStr));
            p.setDescripcion(descripcion != null ? descripcion.trim() : "");
            p.setCantidad(Integer.parseInt(cantidadStr));
            p.setPrecio(Double.parseDouble(precioStr));
            p.setEstado(estado.trim());

            if (dao.actualizar(p)) {
                request.setAttribute("mensaje", "Producto actualizado correctamente.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "Error al actualizar el producto.");
                request.setAttribute("tipoMensaje", "error");
            }
            listarProductos(request, response);
        }
    }

    private boolean camposObligatoriosVacios(String codigo, String nombre, String categoriaIdStr,
                                             String cantidadStr, String precioStr, String estado) {
        return codigo == null || codigo.trim().isEmpty() ||
               nombre == null || nombre.trim().isEmpty() ||
               categoriaIdStr == null || categoriaIdStr.trim().isEmpty() ||
               cantidadStr == null || cantidadStr.trim().isEmpty() ||
               precioStr == null || precioStr.trim().isEmpty() ||
               estado == null || estado.trim().isEmpty();
    }

    private void rellenarFormularioRegistro(HttpServletRequest request, String codigo, String nombre,
                                           String categoriaIdStr, String descripcion,
                                           String cantidadStr, String precioStr, String estado) {
        request.setAttribute("codigo", codigo);
        request.setAttribute("nombre", nombre);
        request.setAttribute("categoriaId", categoriaIdStr);
        request.setAttribute("descripcion", descripcion);
        request.setAttribute("cantidad", cantidadStr);
        request.setAttribute("precio", precioStr);
        request.setAttribute("estado", estado);
    }

    private void cargarCategorias(HttpServletRequest request) {
        List<Categoria> categorias = categoriaDao.listarTodos();
        request.setAttribute("categorias", categorias);
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Producto> productos = dao.listarTodos();
        request.setAttribute("productos", productos);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}