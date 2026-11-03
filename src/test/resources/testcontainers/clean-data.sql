-- ==========================================
-- LIMPIAR TABLAS
-- ==========================================

-- 1. Eliminar primero los registros de las tablas dependientes
DELETE FROM purchase_details;
DELETE FROM sale_details;

-- 2. Eliminar compras y ventas
DELETE FROM purchases;
DELETE FROM sales;

-- 3. Eliminar productos
DELETE FROM products;

-- 4. Eliminar proveedores y clientes
DELETE FROM suppliers;
DELETE FROM clients;


-- ==========================================
-- RESTABLECER AUTO_INCREMENT
-- ==========================================

ALTER TABLE purchase_details AUTO_INCREMENT = 1;
ALTER TABLE sale_details AUTO_INCREMENT = 1;
ALTER TABLE purchases AUTO_INCREMENT = 1;
ALTER TABLE sales AUTO_INCREMENT = 1;
ALTER TABLE products AUTO_INCREMENT = 1;
ALTER TABLE suppliers AUTO_INCREMENT = 1;
ALTER TABLE clients AUTO_INCREMENT = 1;