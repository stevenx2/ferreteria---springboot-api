
-- ==========================================
-- 1. REGISTROS PARA SUPPLIERS (Proveedores)
-- ==========================================
INSERT INTO suppliers (name, phone_number, address) VALUES
('Ferretería Mayorista El Ancla S.A.', '+573104567890', 'Calle 10 # 45-20, Bogotá'),
('Herramientas y Construcción Andina', '+573209876543', 'Carrera 15 # 80-12, Medellín'),
('Distribuidora El Tornillo Feliz', '+573001234567', 'Av. Las Palmas # 32-10, Cali'),
('Aceros y Mallas de Colombia', '+573157890123', 'Zona Industrial 5 # 12-40, Barranquilla'),
('Pinturas y Acabados del Norte', '+573185554433', 'Calle 72 # 50-18, Bucaramanga'),
('Cementos y Agregados del Valle', '+573112223344', 'Vía Panamericana Km 4, Pereira');


-- ==========================================
-- 2. REGISTROS PARA CLIENTES (Clients)
-- ==========================================
INSERT INTO clients (name, phone_number, address) VALUES
('Carlos Pérez', '+573123456789', 'Transversal 54 # 70-20, Medellín'),
('María Gómez', '+573149871234', 'Calle 50 # 30-15, Bogotá'),
('Constructora e Ingeniería S.A.S.', '+573105556789', 'Carrera 43A # 1 Sur-50, Medellín'),
('Juan Rodríguez', '+573198887766', 'Calle 80 # 12-45, Cali'),
('Ana Sofía Martínez', '+573164443322', 'Avenida Circunvalar # 10-88, Pereira'),
('Pedro Luis Torres', '+573151119988', 'Calle 33 # 65-10, Bucaramanga');


-- ==========================================
-- 3. REGISTROS PARA PRODUCTS (Productos)
-- ==========================================
INSERT INTO products (name, price, stock, supplier_id) VALUES
('Cemento gris x 50kg', 32000.00, 150, 6),
('Varilla corrugada 3/8', 25000.00, 300, 4),
('Taladro percutor 1/2', 185000.00, 25, 2),
('Pintura vinilo blanca 1 galón', 65000.00, 40, 5),
('Juego de destornilladores x 6 pzas', 28500.00, 60, 1),
('Caja de tornillos autorroscantes x 100', 12000.00, 120, 3);


-- ==========================================
-- 4. REGISTROS PARA SALES (Ventas - Cabecera)
-- ==========================================
INSERT INTO sales (date, client_id) VALUES
('2026-06-01 08:30:00', 1),
('2026-06-02 10:15:00', 2),
('2026-06-03 14:00:00', 3),
('2026-06-04 16:45:00', 4),
('2026-06-05 09:20:00', 5),
('2026-06-06 11:10:00', 6);


-- ==========================================
-- 5. REGISTROS PARA SALE_DETAILS (Detalle de Ventas)
-- ==========================================
INSERT INTO sale_details (sale_id, product_id, quantity, unit_price) VALUES
(1, 1, 5, 32000.00),   -- Venta 1: 5 Cementos
(1, 2, 5, 125000.00),
(1, 3, 1, 185000),
(1, 1, 5, 32000.00),
(2, 3, 1, 185000.00), -- Venta 2: 1 Taladro
(3, 2, 20, 25000.00), -- Venta 3: 20 Varillas
(4, 4, 2, 65000.00),  -- Venta 4: 2 Pinturas
(5, 5, 2, 28500.00),  -- Venta 5: 2 Juegos de destornilladores
(6, 6, 4, 12000.00);  -- Venta 6: 4 Cajas de tornillos


-- ==========================================
-- 6. REGISTROS PARA PURCHASES (Compras a Proveedores - Cabecera)
-- ==========================================
INSERT INTO purchases (date, supplier_id) VALUES
('2026-05-10 09:00:00', 1),
('2026-05-12 11:00:00', 2),
('2026-05-15 14:30:00', 3),
('2026-05-18 08:00:00', 4),
('2026-05-20 10:00:00', 5),
('2026-05-22 15:00:00', 6);


-- ==========================================
-- 7. REGISTROS PARA PURCHASE_DETAILS (Detalle de Compras)
-- ==========================================
INSERT INTO purchase_details (purchase_id, product_id, quantity, unit_cost) VALUES
(1, 5, 50, 20000.00),  -- Compra 1: 50 Juegos de destornilladores
(2, 3, 10, 140000.00), -- Compra 2: 10 Taladros
(3, 6, 100, 8000.00),  -- Compra 3: 100 Cajas de tornillos
(4, 2, 200, 19000.00), -- Compra 4: 200 Varillas
(5, 4, 30, 45000.00),  -- Compra 5: 30 Pinturas
(6, 1, 150, 24000.00); -- Compra 6: 150 Bultos de cemento
