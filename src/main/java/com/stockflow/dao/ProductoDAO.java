package com.stockflow.dao;

import com.stockflow.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private String url = "jdbc:mysql://localhost:3306/stockflow_db?sslMode=DISABLED&allowPublicKeyRetrieval=true";
    private String usuario = "root";
    private String contrasena = "root";

    private Connection conexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id, p.codigo, p.nombre, p.id_categoria, c.nombre AS categoria, p.descripcion, p.cantidad, p.precio, p.estado "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria ORDER BY p.id DESC";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setCategoria(rs.getString("categoria"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setPrecio(rs.getDouble("precio"));
                p.setEstado(rs.getString("estado"));
                productos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT p.id, p.codigo, p.nombre, p.id_categoria, c.nombre AS categoria, p.descripcion, p.cantidad, p.precio, p.estado "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria WHERE p.id = ?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setCategoria(rs.getString("categoria"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setPrecio(rs.getDouble("precio"));
                p.setEstado(rs.getString("estado"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Producto> buscar(String termino) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id, p.codigo, p.nombre, p.id_categoria, c.nombre AS categoria, p.descripcion, p.cantidad, p.precio, p.estado "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.codigo LIKE ? OR p.nombre LIKE ? ORDER BY p.id DESC";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String busqueda = "%" + termino + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setCategoria(rs.getString("categoria"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setPrecio(rs.getDouble("precio"));
                p.setEstado(rs.getString("estado"));
                productos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    public boolean existeCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existeCodigoExcluyendo(String codigo, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ? AND id != ?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, idExcluir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (codigo, nombre, id_categoria, descripcion, cantidad, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getIdCategoria());
            ps.setString(4, p.getDescripcion());
            ps.setInt(5, p.getCantidad());
            ps.setDouble(6, p.getPrecio());
            ps.setString(7, p.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, id_categoria=?, descripcion=?, cantidad=?, precio=?, estado=? WHERE id=?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getIdCategoria());
            ps.setString(4, p.getDescripcion());
            ps.setInt(5, p.getCantidad());
            ps.setDouble(6, p.getPrecio());
            ps.setString(7, p.getEstado());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}