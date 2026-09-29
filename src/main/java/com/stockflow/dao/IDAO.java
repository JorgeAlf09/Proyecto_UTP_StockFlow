package com.stockflow.dao;

import java.util.List;

public interface IDAO<T> {

    List<T> listarTodos();

    T buscarPorId(int id);

    List<T> buscar(String termino);

    boolean insertar(T entidad);

    boolean actualizar(T entidad);

    boolean eliminar(int id);
}