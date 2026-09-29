package com.stockflow.dao;

import com.stockflow.db.ConexionDB;
import com.stockflow.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    private static final String SQL_SELECT =
            "SELECT p.id, p.codigo, p.nombre, p.id_categoria, c.nombre AS categoria, "
            + "p.descripcion, p.cantidad, p.precio, p.estado "
            + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria ";

    private Connection conexion() throws SQLException {
        return ConexionDB.getInstance().getConnection();
    }

    @Override
    public List<Producto> listarTodos() {
        return listar(SQL_SELECT + "ORDER BY p.id DESC", null);
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = SQL_SELECT + "WHERE p.id = ?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapper(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Producto> buscar(String termino) {
        String sql = SQL_SELECT + "WHERE p.codigo LIKE ? OR p.nombre LIKE ? ORDER BY p.id DESC";
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String busqueda = "%" + termino + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapper(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    @Override
    public boolean existeCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existeCodigoExcluyendo(String codigo, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ? AND id != ?";
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
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

    @Override
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

    @Override
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

    private List<Producto> listar(String sql, Object[] parametros) {
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = conexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (parametros != null) {
                for (int i = 0; i < parametros.length; i++) {
                    ps.setObject(i + 1, parametros[i]);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapper(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    private Producto mapper(ResultSet rs) throws SQLException {
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
}