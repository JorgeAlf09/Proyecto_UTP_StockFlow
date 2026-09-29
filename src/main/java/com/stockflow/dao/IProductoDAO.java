package com.stockflow.dao;

import com.stockflow.model.Producto;

public interface IProductoDAO extends IDAO<Producto> {

    boolean existeCodigo(String codigo);

    boolean existeCodigoExcluyendo(String codigo, int idExcluir);
}