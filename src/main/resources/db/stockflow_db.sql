-- ============================================
-- STOCKFLOW - Sistema Web de Gestión de Almacén
-- Script de creación de la base de datos
-- Este script es reejecutable: si las tablas ya
-- existen, las elimina y las vuelve a crear con
-- el esquema correcto (relación 1 a muchos).
-- Ejecutar TODO el script (Ctrl+Shift+Enter).
-- ============================================

CREATE DATABASE IF NOT EXISTS stockflow_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE stockflow_db;

-- Si ya existen, se eliminan para volver a crearlas limpias
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categorias;

-- ============================================
-- Tabla CATEGORÍAS (tabla padre, 1 a muchos)
-- ============================================
CREATE TABLE categorias (
    id_categoria INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(250),
    PRIMARY KEY (id_categoria)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ============================================
-- Tabla PRODUCTOS
-- La llave foránea id_categoria crea la
-- relación 1 a muchos: 1 categoría -> muchos productos
-- ============================================
CREATE TABLE productos (
    id INT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    id_categoria INT NOT NULL,
    descripcion VARCHAR(250),
    cantidad INT NOT NULL DEFAULT 0,
    precio DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
    PRIMARY KEY (id),
    UNIQUE KEY uq_codigo (codigo),
    CONSTRAINT fk_productos_categorias
        FOREIGN KEY (id_categoria)
        REFERENCES categorias (id_categoria)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ============================================
-- Datos de ejemplo (opcional)
-- ============================================
INSERT INTO categorias (id_categoria, nombre, descripcion) VALUES
(1, 'Electrónica', 'Dispositivos y aparatos electrónicos'),
(2, 'Computación', 'Equipos de cómputo y accesorios'),
(3, 'Oficina', 'Artículos para oficina'),
(4, 'Muebles', 'Muebles para hogar y oficina'),
(5, 'Alimentos', 'Productos alimenticios');

INSERT INTO productos (codigo, nombre, id_categoria, descripcion, cantidad, precio, estado) VALUES
('PRD-001', 'Laptop HP Pavilion', 2, 'Laptop de 15.6 pulgadas, 8GB RAM, 256GB SSD', 15, 2499.90, 'Activo'),
('PRD-002', 'Teclado inalámbrico', 2, 'Teclado compacto con conexión Bluetooth', 40, 89.50, 'Activo'),
('PRD-003', 'Monitor LG 24"', 2, 'Monitor LED Full HD con puertos HDMI', 12, 599.00, 'Activo'),
('PRD-004', 'Impresora Epson L3250', 1, 'Impresora multifuncional sistema continuo', 8, 749.00, 'Activo'),
('PRD-005', 'Escritorio de oficina', 4, 'Escritorio de madera con cajonera', 5, 459.90, 'Inactivo');

-- ============================================
-- Verificación (opcional)
-- ============================================
SELECT * FROM categorias;

SELECT p.codigo, p.nombre, c.nombre AS categoria
FROM productos p
INNER JOIN categorias c ON p.id_categoria = c.id_categoria;