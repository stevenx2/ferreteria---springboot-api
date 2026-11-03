CREATE TABLE clients
(
    client_id    INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20)  NOT NULL,
    address      VARCHAR(150) NOT NULL
);

CREATE TABLE suppliers
(
    supplier_id  INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20)  NOT NULL,
    address      VARCHAR(150) NOT NULL
);

CREATE TABLE products
(
    product_id  INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    stock       INT            NOT NULL DEFAULT 0,
    supplier_id INT            NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers (supplier_id)
);

CREATE TABLE sales
(
    sale_id   INT AUTO_INCREMENT PRIMARY KEY,
    date      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    client_id INT      NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients (client_id)
);

CREATE TABLE sale_details
(
    detail_id  INT AUTO_INCREMENT PRIMARY KEY,
    sale_id    INT            NOT NULL,
    product_id INT            NOT NULL,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales (sale_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);

CREATE TABLE purchases
(
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    date        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    supplier_id INT      NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers (supplier_id)
);

CREATE TABLE purchase_details
(
    detail_id   INT AUTO_INCREMENT PRIMARY KEY,
    purchase_id INT            NOT NULL,
    product_id  INT            NOT NULL,
    quantity    INT            NOT NULL,
    unit_cost   DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchases (purchase_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);

