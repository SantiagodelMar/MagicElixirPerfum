-- =====================================================================
--  MAGIC ELIXIR PERFUM  -  Base de datos para XAMPP (MySQL / MariaDB)
--  Como usarlo: abrir phpMyAdmin -> pestaña "SQL" (o "Importar") -> pegar/subir este archivo -> Continuar
--  OJO: borra y vuelve a crear la base "magic_elixir_db" (sirve para reiniciar los datos de prueba)
-- =====================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP DATABASE IF EXISTS magic_elixir_db;
CREATE DATABASE magic_elixir_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE magic_elixir_db;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================================
--  1. TABLAS (una por cada clase del dominio; las llaves foraneas son las relaciones entre clases)
--     Los id NO son AUTO_INCREMENT porque el programa los pide/calcula (igual que en la version en memoria)
-- =====================================================================

-- Usuario es clase padre en Java; en la base cada hija tiene su propia tabla con las columnas del padre
CREATE TABLE empleado (
    id_usuario       INT PRIMARY KEY,
    nombre           VARCHAR(60)  NOT NULL,
    apellido         VARCHAR(60)  NOT NULL,
    correo           VARCHAR(100) NOT NULL UNIQUE,
    tipo_doc         VARCHAR(20)  NOT NULL,
    celular          VARCHAR(20)  NOT NULL,
    direccion        VARCHAR(150) NOT NULL,
    fecha_nacimiento VARCHAR(10)  NOT NULL,
    rol              VARCHAR(40)  NOT NULL,
    clave            VARCHAR(60)  NOT NULL,
    activo           BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE = InnoDB;

CREATE TABLE administrador (
    id_usuario       INT PRIMARY KEY,
    nombre           VARCHAR(60)  NOT NULL,
    apellido         VARCHAR(60)  NOT NULL,
    correo           VARCHAR(100) NOT NULL UNIQUE,
    tipo_doc         VARCHAR(20)  NOT NULL,
    celular          VARCHAR(20)  NOT NULL,
    direccion        VARCHAR(150) NOT NULL,
    fecha_nacimiento VARCHAR(10)  NOT NULL,
    permisos         VARCHAR(60)  NOT NULL,
    nivel            VARCHAR(20)  NOT NULL
) ENGINE = InnoDB;

CREATE TABLE cliente (
    id_usuario       INT PRIMARY KEY,
    nombre           VARCHAR(60)  NOT NULL,
    apellido         VARCHAR(60)  NOT NULL,
    correo           VARCHAR(100) NOT NULL UNIQUE,
    tipo_doc         VARCHAR(20)  NOT NULL,
    celular          VARCHAR(20)  NOT NULL,
    direccion        VARCHAR(150) NOT NULL,
    fecha_nacimiento VARCHAR(10)  NOT NULL,
    tipo_cliente     VARCHAR(30)  NOT NULL,
    direccion_envio  VARCHAR(150) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE categoria (
    id_categoria INT PRIMARY KEY,
    nombre       VARCHAR(60)  NOT NULL,
    descripcion  VARCHAR(150) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE perfume (
    id_perfume      INT PRIMARY KEY,
    nombre          VARCHAR(100)  NOT NULL,
    marca           VARCHAR(60)   NOT NULL,
    precio          DECIMAL(12,2) NOT NULL,
    mililitros      INT           NOT NULL,
    notas_olfativas VARCHAR(200)  NOT NULL,
    id_categoria    INT           NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria)
) ENGINE = InnoDB;

CREATE TABLE inventario (
    id_inventario         INT PRIMARY KEY,
    id_perfume            INT         NOT NULL UNIQUE,   -- un perfume tiene un solo registro de inventario
    stock_disponible      INT         NOT NULL,
    estado_disponibilidad VARCHAR(20) NOT NULL,          -- DISPONIBLE, BAJO_STOCK, AGOTADO
    FOREIGN KEY (id_perfume) REFERENCES perfume (id_perfume) ON DELETE CASCADE   -- si se borra el perfume se borra su inventario
) ENGINE = InnoDB;

CREATE TABLE pedido (
    id_pedido     INT PRIMARY KEY,
    fecha_pedido  DATE          NOT NULL,
    id_cliente    INT           NOT NULL,
    total         DECIMAL(12,2) NOT NULL DEFAULT 0,
    observaciones VARCHAR(200),
    estado_pedido VARCHAR(20)   NOT NULL,                -- PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    metodo_pago   VARCHAR(20)   NOT NULL,                -- EFECTIVO, PSE, T_CREDITO, T_DEBITO
    canal_venta   VARCHAR(20),                           -- INSTAGRAM, WHATSAPP, LOCAL  (HU-24)
    FOREIGN KEY (id_cliente) REFERENCES cliente (id_usuario)
) ENGINE = InnoDB;

-- Un pedido lleva varios perfumes (HU-10): relacion 1 a muchos
CREATE TABLE detalle_pedido (
    id_pedido       INT           NOT NULL,
    id_detalle      INT           NOT NULL,
    id_perfume      INT           NOT NULL,
    cantidad        INT           NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    subtotal        DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id_pedido, id_detalle),
    FOREIGN KEY (id_pedido)  REFERENCES pedido (id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_perfume) REFERENCES perfume (id_perfume)
) ENGINE = InnoDB;

CREATE TABLE envio (
    id_envio          INT PRIMARY KEY,
    id_pedido         INT           NOT NULL,
    transportadora    VARCHAR(60)   NOT NULL,
    numero_guia       VARCHAR(40)   NOT NULL,
    direccion_destino VARCHAR(150)  NOT NULL,
    fecha_envio       DATE          NOT NULL,
    fecha_entrega     DATE          NULL,                -- queda vacia hasta que se entrega
    costo_envio       DECIMAL(12,2) NOT NULL,
    estado_envio      VARCHAR(20)   NOT NULL,            -- CREADO, EN_CAMINO, ENTREGADO
    FOREIGN KEY (id_pedido) REFERENCES pedido (id_pedido)
) ENGINE = InnoDB;

CREATE TABLE proveedor (
    id_proveedor INT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    documento    VARCHAR(30)  NOT NULL UNIQUE,
    telefono     VARCHAR(20)  NOT NULL,
    correo       VARCHAR(100) NOT NULL,
    direccion    VARCHAR(150) NOT NULL,
    activo       BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE = InnoDB;

CREATE TABLE orden_compra (
    id_orden_compra INT PRIMARY KEY,
    id_proveedor    INT           NOT NULL,
    fecha_orden     DATE          NOT NULL,
    estado          VARCHAR(20)   NOT NULL,              -- PENDIENTE, APROBADA, RECIBIDA, CANCELADA
    total           DECIMAL(12,2) NOT NULL,
    observaciones   VARCHAR(200),
    FOREIGN KEY (id_proveedor) REFERENCES proveedor (id_proveedor)
) ENGINE = InnoDB;

CREATE TABLE devolucion (
    id_devolucion   INT PRIMARY KEY,
    id_pedido       INT           NOT NULL,
    fecha_entrega   DATE          NOT NULL,
    fecha_solicitud DATE          NOT NULL,
    motivo          VARCHAR(30)   NOT NULL,
    descripcion     VARCHAR(200)  NOT NULL,
    total_pagado    DECIMAL(12,2) NOT NULL,
    estado          VARCHAR(20)   NOT NULL,              -- SOLICITADA, APROBADA, RECHAZADA, REEMBOLSADA
    monto_reembolso DECIMAL(12,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_pedido) REFERENCES pedido (id_pedido)
) ENGINE = InnoDB;

CREATE TABLE garantia (
    id_garantia     INT PRIMARY KEY,
    id_pedido       INT          NOT NULL,
    fecha_inicio    DATE         NOT NULL,
    meses_cobertura INT          NOT NULL,
    descripcion     VARCHAR(200) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido (id_pedido)
) ENGINE = InnoDB;

-- =====================================================================
--  2. DATOS DE PRUEBA (los mismos que cargaba Contexto en memoria + unos pedidos mas para los reportes)
-- =====================================================================

-- Empleados (HU-01). Para entrar: admin@magicelixir.com / 1234
INSERT INTO empleado (id_usuario, nombre, apellido, correo, tipo_doc, celular, direccion, fecha_nacimiento, rol, clave, activo) VALUES
  (1, 'Admin', 'Magic Elixir', 'admin@magicelixir.com', 'CC', '3000000000', 'Medellin', '2000-01-01', 'Vendedor', '1234', TRUE),
  (2, 'Laura', 'Zapata', 'laura@magicelixir.com', 'CC', '3009998877', 'Envigado', '1999-09-09', 'Bodega', '1234', TRUE);

INSERT INTO administrador (id_usuario, nombre, apellido, correo, tipo_doc, celular, direccion, fecha_nacimiento, permisos, nivel) VALUES
  (1, 'Aaron Mateo', 'Ruiz', 'aaron@magicelixir.com', 'CC', '3001112233', 'Medellin', '2000-05-10', 'Total', '1');

-- Clientes (HU-03)
INSERT INTO cliente (id_usuario, nombre, apellido, correo, tipo_doc, celular, direccion, fecha_nacimiento, tipo_cliente, direccion_envio) VALUES
  (1, 'Anyi', 'Cañas', 'anyi@gmail.com', 'CC', '3001234567', 'Calle 10 #20-30', '2001-03-15', 'Frecuente', 'Calle 10 #20-30'),
  (2, 'Valentina', 'Gómez', 'valegomez@gmail.com', 'CC', '3105558899', 'Cra 43A #1-50, El Poblado', '1998-07-22', 'Nuevo', 'Cra 43A #1-50, El Poblado'),
  (3, 'Camilo', 'Restrepo', 'camilo.res@hotmail.com', 'CC', '3017773344', 'Calle 33 #65-12, Laureles', '1995-11-02', 'Frecuente', 'Calle 33 #65-12, Laureles'),
  (4, 'Mariana', 'Ospina', 'mari.ospina@gmail.com', 'CE', '3226661122', 'Av. El Poblado #10-20', '2000-02-14', 'Mayorista', 'Calle 50 #40-10, Itagüí');

-- Proveedores (HU-15)
INSERT INTO proveedor (id_proveedor, nombre, documento, telefono, correo, direccion, activo) VALUES
  (1, 'Distribuidora Arabe SAS', '900123456', '6044445566', 'ventas@distriarabe.com', 'Bogota', TRUE),
  (2, 'Perfumes Importados del Caribe', '901555777', '6053332211', 'contacto@pidcaribe.com', 'Barranquilla', TRUE);

-- Las 4 categorias de Instagram (HU-06)
INSERT INTO categoria (id_categoria, nombre, descripcion) VALUES
  (1, 'Árabe Masculina', 'Fragancias arabes para hombre'),
  (2, 'Perfumería Masculina', 'Fragancias de diseñador para hombre'),
  (3, 'Árabe Femenina', 'Fragancias arabes para mujer'),
  (4, 'Perfumería Femenina', 'Fragancias de diseñador para mujer');

-- Catalogo completo: 201 perfumes (HU-04)
INSERT INTO perfume (id_perfume, nombre, marca, precio, mililitros, notas_olfativas, id_categoria) VALUES
  (1, 'Asad', 'Lattafa', 240000.00, 100, 'Pimienta negra, Piña, Tabaco', 1),
  (2, 'Khamrah', 'Lattafa', 250000.00, 100, 'Canela, Nuez moscada, Praliné', 1),
  (3, 'Khamrah Qahwa', 'Lattafa', 270000.00, 100, 'Café, Canela, Vainilla', 1),
  (4, 'Sublime (Bade''e Al Oud)', 'Lattafa', 230000.00, 100, 'Manzana, Rosa, Ciruela', 1),
  (5, 'Amethyst (Bade''e Al Oud)', 'Lattafa', 230000.00, 100, 'Pimienta rosa, Rosa de Bulgaria', 1),
  (6, 'Oud for Glory (Bade''e Al Oud)', 'Lattafa', 230000.00, 100, 'Azafrán, Nuez moscada, Oud', 1),
  (7, 'Honor & Glory (Bade''e Al Oud)', 'Lattafa', 240000.00, 100, 'Piña, Brulee, Canela', 1),
  (8, 'His Confession', 'Lattafa', 280000.00, 100, 'Canela, Iris, Tonka', 1),
  (9, 'Qaed Al Fursan', 'Lattafa', 190000.00, 90, 'Piña, Azafrán, Jazmín', 1),
  (10, 'Qaed Al Fursan Unlimited', 'Lattafa', 190000.00, 90, 'Coco, Cítricos, Ylang-Ylang', 1),
  (11, 'Maahir', 'Lattafa', 210000.00, 100, 'Azafrán, Rosa, Ámbar', 1),
  (12, 'Maahir Black Edition', 'Lattafa', 220000.00, 100, 'Pimienta negra, Incienso, Cuero', 1),
  (13, 'Shaheen Gold', 'Lattafa', 250000.00, 100, 'Piña, Toronja, Lavanda', 1),
  (14, 'Shaheen Silver', 'Lattafa', 250000.00, 100, 'Casis, Bergamota, Rosa', 1),
  (15, 'Yara', 'Lattafa', 210000.00, 100, 'Orquídea, Vainilla, Almizcle', 1),
  (16, 'Ejaazi', 'Lattafa', 180000.00, 100, 'Cítricos, Cardamomo, Cedro', 1),
  (17, 'Ejaazi Intensive Silver', 'Lattafa', 180000.00, 100, 'Mandaria, Toronja, Nota marina', 1),
  (18, 'Al Noble Ameer', 'Lattafa', 210000.00, 100, 'Manzana, Pimienta rosa, Romero', 1),
  (19, 'Jorge Di Profumo', 'Maison Alhambra', 190000.00, 100, 'Notas marinas, Incienso, Patchouli', 1),
  (20, 'Amber & Leather', 'Maison Alhambra', 190000.00, 100, 'Cuero, Cardamomo, Ámbar', 1),
  (21, 'Tobacco Touch', 'Maison Alhambra', 190000.00, 100, 'Tabaco, Vainilla, Especias', 1),
  (22, 'Porto Neroli', 'Maison Alhambra', 190000.00, 100, 'Neroli, Cítricos, Jazmín', 1),
  (23, 'Woody Oud', 'Maison Alhambra', 190000.00, 100, 'Oud, Palo de Rosa, Cardamomo', 1),
  (24, 'Toscano Leather', 'Maison Alhambra', 190000.00, 100, 'Cuero, Frambuesa, Azafrán', 1),
  (25, 'Fabulous Life', 'Maison Alhambra', 190000.00, 100, 'Almendra amarga, Cuero, Orris', 1),
  (26, 'Hercules', 'Maison Alhambra', 190000.00, 100, 'Canela, Hojas de Tabaco, Incienso', 1),
  (27, 'Kismet For Men', 'Maison Alhambra', 200000.00, 100, 'Madera, Café, Cacao', 1),
  (28, 'Jean Lowe Noir', 'Maison Alhambra', 220000.00, 100, 'Oud, Incienso, Cacao', 1),
  (29, 'Royal Bleu', 'Orientica', 420000.00, 80, 'Manzana, Lavanda, Pimienta', 1),
  (30, 'Fantasy', 'Orientica', 380000.00, 80, 'Nuez moscada, Manzanilla, Ámbar', 1),
  (31, 'Amber Rouge', 'Orientica', 420000.00, 80, 'Jazmín, Azafrán, Ámbar gris', 1),
  (32, 'Amber Noir', 'Orientica', 420000.00, 80, 'Zanahoria, Elemi, Madera', 1),
  (33, 'Velvet Gold', 'Orientica', 450000.00, 80, 'Pimienta rosa, Violeta, Vainilla', 1),
  (34, 'Odyssey Wild One', 'Armaf', 240000.00, 100, 'Pimienta, Menta, Bergamota', 1),
  (35, 'Odyssey Mandarin Sky', 'Armaf', 250000.00, 100, 'Mandarina, Caramelo, Habita Tonka', 1),
  (36, 'Club de Nuit Intense Man', 'Armaf', 260000.00, 105, 'Limón, Piña, Abedul, Almizcle', 1),
  (37, 'Club de Nuit Iconic', 'Armaf', 320000.00, 105, 'Toronja, Menta, Jengibre', 1),
  (38, 'Club de Nuit Untold', 'Armaf', 350000.00, 105, 'Azafrán, Jazmín, Madera de Ámbar', 1),
  (39, 'Club de Nuit Sillage', 'Armaf', 280000.00, 105, 'Bergamota, Grosellas negras, Violeta', 1),
  (40, 'Club de Nuit Milestone', 'Armaf', 280000.00, 105, 'Notas marinas, Cítricos, Violeta', 1),
  (41, 'Club de Nuit Urban Elixir', 'Armaf', 290000.00, 105, 'Pimienta rosa, Lavanda, Vetiver', 1),
  (42, 'L''Aventure', 'Al Haramain', 280000.00, 100, 'Limón, Bergamota, Elemi', 1),
  (43, 'L''Aventure Knight', 'Al Haramain', 290000.00, 100, 'Verbona, Té, Violeta', 1),
  (44, 'Amber Oud Gold Edition', 'Al Haramain', 380000.00, 120, 'Bergamota, Notas verdes, Melón', 1),
  (45, 'Amber Oud Rouge Edition', 'Al Haramain', 390000.00, 120, 'Azafrán, Madera de Ámbar', 1),
  (46, 'Amber Oud Tobacco Edition', 'Al Haramain', 390000.00, 120, 'Jengibre, Pimienta, Canela', 1),
  (47, 'Premium Collection', 'Al Haramain', 350000.00, 100, 'Notas florales, Amaderadas', 1),
  (48, '9 PM Classic', 'Afnan', 220000.00, 100, 'Manzana, Canela, Lavanda, Vainilla', 1),
  (49, '9 AM Dive', 'Afnan', 220000.00, 100, 'Menta, Limón, Manzana verde', 1),
  (50, 'Supremacy Not Only Intense', 'Afnan', 290000.00, 100, 'Grosella negra, Bergamota, Liquen', 1),
  (51, 'Supremacy In Heaven', 'Afnan', 260000.00, 100, 'Bergamota, Mandarina, Té verde', 1),
  (52, 'Turathi Blue', 'Afnan', 260000.00, 90, 'Cítricos, Ámbar, Amaderado', 1),
  (53, 'Historic Olmeda', 'Afnan', 250000.00, 100, 'Pimienta rosa, Cardamomo, Pomelo', 1),
  (54, 'Il Kakao / Ilmin Parfums', 'Ilmin', 620000.00, 30, 'Cacao, Vainilla, Especias', 2),
  (55, 'Il Carmine', 'Ilmin', 620000.00, 30, 'Notas amaderadas, Ámbar', 2),
  (56, 'Il Sesso', 'Ilmin', 620000.00, 30, 'Cítricos, Almizcle, Cuero', 2),
  (57, 'Il Male', 'Ilmin', 620000.00, 30, 'Pimienta, Lavanda, Sándalo', 2),
  (58, 'Il Regalo', 'Ilmin', 620000.00, 30, 'Notas orientales, Tabaco', 2),
  (59, 'Il Pascolo', 'Ilmin', 620000.00, 30, 'Hierbas frescas, Vetiver', 2),
  (60, 'Il Oro', 'Ilmin', 650000.00, 30, 'Azafrán, Ámbar, Oud', 2),
  (61, 'Il Rosso', 'Ilmin', 620000.00, 30, 'Frutos rojos, Madera', 2),
  (62, 'Il Noche', 'Ilmin', 620000.00, 30, 'Incienso, Cuero, Especias', 2),
  (63, 'Il Cuoio', 'Ilmin', 620000.00, 30, 'Cuero, Tabaco, Madera', 2),
  (64, 'Il Blanco', 'Ilmin', 620000.00, 30, 'Notas blancas, Cítricos', 2),
  (65, 'Il Verde', 'Ilmin', 620000.00, 30, 'Notas verdes, Menta', 2),
  (66, 'Black Religion', 'Devier', 380000.00, 100, 'Incienso, Maderas oscuras', 2),
  (67, 'Spice Mexican', 'Devier', 380000.00, 100, 'Especias, Vainilla', 2),
  (68, 'Forgive Us', 'Devier', 380000.00, 100, 'Ámbar, Notas dulces', 2),
  (69, 'Strong as Metal', 'Devier', 380000.00, 100, 'Notas metálicas, Cítricos', 2),
  (70, 'VIP Carat', 'Devier', 390000.00, 100, 'Especias finas, Ámbar', 2),
  (71, 'Amber Black', 'Devier', 380000.00, 100, 'Ámbar, Madera', 2),
  (72, 'Satin Rose', 'Devier', 380000.00, 100, 'Rosa, Almizcle', 2),
  (73, 'Oud Noir', 'Devier', 390000.00, 100, 'Oud, Cuero', 2),
  (74, 'Drew', 'Devier', 380000.00, 100, 'Frutal, Fresco', 2),
  (75, 'Lermo', 'Devier', 380000.00, 100, 'Cítrico, Amaderado', 2),
  (76, 'Acqua Di Giò Pour Homme', 'Giorgio Armani', 520000.00, 100, 'Notas marinas, Lima, Bergamota', 2),
  (77, 'Acqua Di Giò Profondo', 'Giorgio Armani', 580000.00, 100, 'Notas marinas, Romero, Lavanda', 2),
  (78, 'Acqua Di Giò Parfum', 'Giorgio Armani', 640000.00, 100, 'Bergamota, Incienso, Patchouli', 2),
  (79, 'Acqua Di Giò Absolu', 'Giorgio Armani', 590000.00, 100, 'Notas marinas, Pomelo, Tonka', 2),
  (80, 'MYSLF Eau de Parfum', 'Yves Saint Laurent', 620000.00, 100, 'Bergamota, Azahar, Patchouli', 2),
  (81, 'Game of Spades Ace', 'Jo Milano Paris', 420000.00, 100, 'Cítricos, Amaderado', 2),
  (82, 'Game of Spades Queen', 'Jo Milano Paris', 420000.00, 100, 'Floral, Frutal', 2),
  (83, 'Game of Spades King', 'Jo Milano Paris', 420000.00, 100, 'Notas dulces, Ámbar', 2),
  (84, 'Game of Spades Royale', 'Jo Milano Paris', 450000.00, 100, 'Madera, Especias', 2),
  (85, 'Game of Spades Wild Card', 'Jo Milano Paris', 430000.00, 100, 'Fresco, Especiado', 2),
  (86, 'Bad Boy', 'Carolina Herrera', 480000.00, 100, 'Pimienta negra, Pimienta blanca, Cacao', 2),
  (87, 'Bad Boy Cobalt', 'Carolina Herrera', 520000.00, 100, 'Pimienta rosa, Ciruela, Trufa', 2),
  (88, 'Born in Roma Uomo', 'Valentino', 630000.00, 100, 'Hojas de violeta, Sal de mar, Jengibre', 2),
  (89, 'Born in Roma Intense Uomo', 'Valentino', 680000.00, 100, 'Vainilla, Lavanda, Vetiver', 2),
  (90, '1 Million', 'Paco Rabanne', 490000.00, 100, 'Mandarina sangrienta, Canela, Cuero', 2),
  (91, '1 Million Royal / Elixir', 'Paco Rabanne', 550000.00, 100, 'Manzana, Lavanda, Benzoin', 2),
  (92, 'Le Male Elixir', 'Jean Paul Gaultier', 590000.00, 125, 'Menta, Lavanda, Benzoin, Miel', 2),
  (93, 'Le Male Le Parfum', 'Jean Paul Gaultier', 560000.00, 125, 'Cardamomo, Lavanda, Iris, Vainilla', 2),
  (94, 'L.12.12 Blanc', 'Lacoste', 360000.00, 100, 'Toronja, Cardamomo, Romera, Tuberosa', 2),
  (95, 'L.12.12 Black / Noir', 'Lacoste', 360000.00, 100, 'Sandía, Lavanda, Basílico, Chocolate', 2),
  (96, 'Scent of Peace for Him', 'BOND No. 9', 1350000.00, 100, 'Piña, Baya de enebro, Bergamota', 2),
  (97, 'Aventus', 'Creed', 1650000.00, 100, 'Piña, Bergamota, Grosella negra, Abedul', 2),
  (98, 'Eros Eau de Parfum / Parfum', 'Versace', 480000.00, 100, 'Menta, Manzana verde, Limón, Habain Tonka', 2),
  (99, 'Dylan Blue Pour Homme', 'Versace', 440000.00, 100, 'Notas acuáticas, Bergamota, Toronja, Incienso', 2),
  (100, 'Yara (Rosa)', 'Lattafa', 210000.00, 100, 'Orquídea, Heliotropo, Tangerina, Vainilla', 3),
  (101, 'Yara Tous (Amarillo)', 'Lattafa', 220000.00, 100, 'Mango, Coco, Maracuyá, Jazmín', 3),
  (102, 'Yara Moi (Blanco/Melocotón)', 'Lattafa', 220000.00, 100, 'Jazmín, Melocotón, Caramelo', 3),
  (103, 'Yara Candy (Verde/Rosa)', 'Lattafa', 240000.00, 100, 'Grosella negra, Caramelos, Vainilla', 3),
  (104, 'Khamrah', 'Lattafa', 250000.00, 100, 'Canela, Nuez moscada, Praliné', 3),
  (105, 'Khamrah Qahwa', 'Lattafa', 270000.00, 100, 'Café, Canela, Vainilla', 3),
  (106, 'Sublime (Bade''e Al Oud)', 'Lattafa', 230000.00, 100, 'Manzana, Rosa, Ciruela', 3),
  (107, 'Amethyst (Bade''e Al Oud)', 'Lattafa', 230000.00, 100, 'Pimienta rosa, Rosa de Bulgaria', 3),
  (108, 'Honor & Glory (Bade''e Al Oud)', 'Lattafa', 240000.00, 100, 'Piña, Brulee, Canela', 3),
  (109, 'Eclat Extraordinaire', 'Lattafa', 210000.00, 100, 'Notas florales, Ámbar', 3),
  (110, 'Mayar', 'Lattafa', 220000.00, 100, 'Frambuesa, Litchi, Violeta', 3),
  (111, 'Mayar Natural Intense', 'Lattafa', 230000.00, 100, 'Higo, Agua de coco, Vainilla', 3),
  (112, 'Qaed Al Fursan Unlimited (Blanco)', 'Lattafa', 190000.00, 90, 'Coco, Cítricos, Ylang-Ylang', 3),
  (113, 'Musk Mood', 'Lattafa', 180000.00, 100, 'Cítricos, Almizcle blanco, Floral', 3),
  (114, 'Pure Musk', 'Lattafa', 180000.00, 100, 'Almizcle blanco, Notas dulces', 3),
  (115, 'Haya', 'Lattafa', 220000.00, 100, 'Champán rosa, Fresas, Orquídea', 3),
  (116, 'Sakeena', 'Lattafa', 230000.00, 100, 'Maracuyá, Mandarina, Rosa', 3),
  (117, 'Mohra Silky Rose', 'Lattafa', 210000.00, 100, 'Rosa, Pimienta rosa, Almizcle', 3),
  (118, 'Nebras', 'Lattafa', 260000.00, 100, 'Bayas rojas, Mandarina, Cacao, Vainilla', 3),
  (119, 'Ansaam Gold', 'Lattafa', 270000.00, 100, 'Pera, Mandarina, Jazmín, Vainilla', 3),
  (120, 'Delilah', 'Maison Alhambra', 190000.00, 100, 'Litchi, Ruibarbo, Bergamota, Rosa', 3),
  (121, 'Exclusif Rose', 'Maison Alhambra', 210000.00, 100, 'Rosa, Cítricos, Ámbar', 3),
  (122, 'Exclusif Saffron', 'Maison Alhambra', 210000.00, 100, 'Azafrán, Cuero, Especias', 3),
  (123, 'Porto Neroli', 'Maison Alhambra', 190000.00, 100, 'Neroli, Cítricos, Jazmín', 3),
  (124, 'Bright Peach', 'Maison Alhambra', 190000.00, 100, 'Melocotón, Naranja sanguínea, Ron', 3),
  (125, 'Lovely Cherie', 'Maison Alhambra', 190000.00, 100, 'Cereza negra, Almendra amarga, Licor', 3),
  (126, 'Rose Petals', 'Maison Alhambra', 190000.00, 100, 'Pimienta de Sichuan, Rosa de mayo', 3),
  (127, 'Infini Rose', 'Maison Alhambra', 200000.00, 100, 'Bergamota, Pimienta rosa, Rosa', 3),
  (128, 'Narissa For Her', 'Maison Alhambra', 190000.00, 100, 'Flor de azahar, Almizcle, Ámbar', 3),
  (129, 'Kismet For Women', 'Maison Alhambra', 200000.00, 100, 'Damasco, Jazmín, Tuberosa', 3),
  (130, 'Club de Nuit Woman', 'Armaf', 240000.00, 105, 'Naranja, Bergamota, Rosa, Jazmín', 3),
  (131, 'Club de Nuit Imperiale', 'Armaf', 290000.00, 105, 'Litchi, Bergamota, Rosa turca, Vainilla', 3),
  (132, 'Club de Nuit Untold', 'Armaf', 350000.00, 105, 'Azafrán, Jazmín, Madera de Ámbar', 3),
  (133, 'Club de Nuit Milestone', 'Armaf', 280000.00, 105, 'Notas marinas, Cítricos, Violeta', 3),
  (134, 'Odyssey Candleglow', 'Armaf', 240000.00, 100, 'Flores blancas, Vainilla', 3),
  (135, 'Odyssey Femme', 'Armaf', 230000.00, 100, 'Naranja, Tuberosa, Jasmin', 3),
  (136, 'Le Parfait Pour Femme', 'Armaf', 210000.00, 100, 'Jazmín, Mandarina, Tuberosa', 3),
  (137, 'Vanity Femme Essence', 'Armaf', 190000.00, 100, 'Fresa, Melocotón, Flor de Azahar', 3),
  (138, 'Tres Nuit Valentina', 'Armaf', 220000.00, 100, 'Cítricos, Rosa, Almizcle', 3),
  (139, 'Velvet Gold', 'Orientica', 450000.00, 80, 'Pimienta rosa, Violeta, Vainilla', 3),
  (140, 'Amber Rouge', 'Orientica', 420000.00, 80, 'Jazmín, Azafrán, Ámbar gris', 3),
  (141, 'Royal Amber', 'Orientica', 420000.00, 80, 'Bergamota, Melón, Ámbar', 3),
  (142, 'Fantasy', 'Orientica', 380000.00, 80, 'Nuez moscada, Manzanilla, Ámbar', 3),
  (143, 'Arte Bellissimo Romantic', 'Orientica', 380000.00, 80, 'Bergamota, Melocotón, Rosa', 3),
  (144, 'Fleur de Orientica', 'Orientica', 390000.00, 80, 'Flores blancas, Cítricos', 3),
  (145, '9 AM Pour Femme', 'Afnan', 220000.00, 100, 'Mandarina, Bergamota, Toronja, Grosellas', 3),
  (146, '9 PM Pour Femme', 'Afnan', 220000.00, 100, 'Frambuesa, Manzana, Violeta, Vainilla', 3),
  (147, 'Zimaya Sharaf Blend', 'Afnan', 240000.00, 100, 'Datiles, Canela, Vainilla', 3),
  (148, 'Supremacy Pink', 'Afnan', 250000.00, 100, 'Rosa, Violeta, Almizcle', 3),
  (149, 'Modest Deux', 'Afnan', 230000.00, 100, 'Frutos oscuros, Chocolate, Vainilla', 3),
  (150, 'Historic Doria', 'Afnan', 250000.00, 100, 'Flor de Azahar, Tuberosa, Jasmin', 3),
  (151, 'Amber Oud Gold Edition', 'Al Haramain', 380000.00, 120, 'Bergamota, Melón, Piña', 3),
  (152, 'Amber Oud Ultra Violet', 'Al Haramain', 390000.00, 120, 'Jazmín, Rosa, Cedro', 3),
  (153, 'L''Aventure Femme', 'Al Haramain', 280000.00, 100, 'Piña, Grosella negra, Bergamota', 3),
  (154, 'Junoon Rose', 'Al Haramain', 340000.00, 75, 'Flor de Cerezo, Amaretto, Rosa', 3),
  (155, 'Good Girl', 'Carolina Herrera', 580000.00, 80, 'Almendra, Café, Jazmín Sambac, Tuberosa', 4),
  (156, 'Good Girl Blush', 'Carolina Herrera', 610000.00, 80, 'Bergamota, Peonía, Vainilla', 4),
  (157, 'Very Good Girl', 'Carolina Herrera', 610000.00, 80, 'Grosella roja, Litchi, Rosa', 4),
  (158, 'Very Good Girl Elixir', 'Carolina Herrera', 640000.00, 80, 'Cereza, Rosa, Vainilla', 4),
  (159, '212 VIP Rosé', 'Carolina Herrera', 490000.00, 80, 'Champán rosado, Flor de durazno, Amaderado', 4),
  (160, '212 VIP Rosé Elixir', 'Carolina Herrera', 530000.00, 80, 'Licor de cereza, Flor de azahar', 4),
  (161, 'Greenwich Village', 'BOND No. 9', 1450000.00, 100, 'Cassis, Lychee, Mandarina, Peonía', 4),
  (162, 'Tribeca', 'BOND No. 9', 1450000.00, 100, 'Cacao, Avellana, Jazmín Sambac', 4),
  (163, 'Chez Bond / Nolita', 'BOND No. 9', 1350000.00, 100, 'Té verde, Cítricos, Violeta', 4),
  (164, 'Central Park West', 'BOND No. 9', 1350000.00, 100, 'Narciso, Ylang-Ylang, Pimienta negra', 4),
  (165, 'Gold Coast', 'BOND No. 9', 140000.00, 100, 'Litchi, Damasco, Rosa de Francia', 4),
  (166, 'Chinatown', 'BOND No. 9', 1350000.00, 100, 'Flor de Melocotón, Bergamota, Tuberosa', 4),
  (167, 'Donna Born in Roma', 'Valentino', 630000.00, 100, 'Grosellas negras, Bergamota, Jazmín Sambac', 4),
  (168, 'Donna Born in Roma Coral Fantasy', 'Valentino', 650000.00, 100, 'Kiwi, Naranja de Brasil, Rosa', 4),
  (169, 'Donna Born in Roma Yellow Dream', 'Valentino', 650000.00, 100, 'Limón primofiore, Rosa, Almizcle', 4),
  (170, 'Donna Born in Roma Intense', 'Valentino', 700000.00, 100, 'Vainilla Bourbon, Jazmín, Benzoin', 4),
  (171, 'Sweet Like Candy', 'Ariana Grande', 380000.00, 100, 'Mora zarzamora, Bergamota, Crema batida', 4),
  (172, 'Thank U, Next', 'Ariana Grande', 390000.00, 100, 'Pera blanca, Frambuesa, Coco', 4),
  (173, 'Cloud Eau de Parfum', 'Ariana Grande', 420000.00, 100, 'Lavanda, Pera, Bergamota, Crema de coco', 4),
  (174, 'Cloud Pink', 'Ariana Grande', 460000.00, 100, 'Pitahaya, Bayas silvestres, Agua de coco', 4),
  (175, 'Toy 2 Bubble Gum', 'Moschino', 360000.00, 100, 'Fruta confitada, Chicle, Naranja, Rosa', 4),
  (176, 'Fresh Couture / Pink Fresh', 'Moschino', 340000.00, 100, 'Toronja rosada, CASSIS, Lirio del valle', 4),
  (177, 'Gold Fresh Couture', 'Moschino', 350000.00, 100, 'Mango, Mandarina, Pera, Orquídea', 4),
  (178, 'Scandal Eau de Parfum', 'Jean Paul Gaultier', 530000.00, 80, 'Naranja sanguínea, Miel, Gardenia', 4),
  (179, 'So Scandal!', 'Jean Paul Gaultier', 560000.00, 80, 'Flor de Azahar, Tuberosa, Jazmín Sambac', 4),
  (180, 'La Belle Le Parfum', 'Jean Paul Gaultier', 580000.00, 100, 'Pera, Tonka, Vainilla', 4),
  (181, 'Il Rosa', 'Ilmin', 620000.00, 30, 'Rosa, Notas dulces', 4),
  (182, 'Il Fiori', 'Ilmin', 620000.00, 30, 'Flores blancas, Cítricos', 4),
  (183, 'Il Dolce', 'Ilmin', 620000.00, 30, 'Gourmand, Vainilla', 4),
  (184, 'Il Femme', 'Ilmin', 620000.00, 30, 'Floral, Amaderado', 4),
  (185, 'Il Carmine', 'Ilmin', 620000.00, 30, 'Notas amaderadas, Ámbar', 4),
  (186, 'Il Sesso', 'Ilmin', 620000.00, 30, 'Cítricos, Almizcle', 4),
  (187, 'Satin Rose', 'Devier', 380000.00, 100, 'Rosa, Almizcle', 4),
  (188, 'VIP Carat', 'Devier', 390000.00, 100, 'Especias finas, Ámbar', 4),
  (189, 'Amber Black', 'Devier', 380000.00, 100, 'Ámbar, Madera', 4),
  (190, 'Lermo', 'Devier', 380000.00, 100, 'Cítrico, Amaderado', 4),
  (191, 'Libre Eau de Parfum', 'Yves Saint Laurent', 620000.00, 90, 'Lavanda, Mandarina, Grosellas negras', 4),
  (192, 'Black Opium', 'Yves Saint Laurent', 580000.00, 90, 'Pera, Pimienta rosa, Flor de azahar, Café', 4),
  (193, 'La Vie Est Belle', 'Lancôme', 520000.00, 100, 'Grosellas negras, Pera, Iris, Jazmín', 4),
  (194, 'Idôle', 'Lancôme', 490000.00, 100, 'Pera, Bergamota, Rosa de Esparta, Jazmín', 4),
  (195, 'Bright Crystal', 'Versace', 430000.00, 90, 'Yuzu, Granada, Peonía, Flor de loto', 4),
  (196, 'Yellow Diamond', 'Versace', 410000.00, 90, 'Limón de Amalfi, Bergamota, Neroli', 4),
  (197, 'Miss Dior Eau de Parfum', 'Dior', 680000.00, 100, 'Lirio del valle, Peonía, Iris, Rosa de centifolia', 4),
  (198, 'Coco Mademoiselle', 'Chanel', 780000.00, 100, 'Naranja, Mandarina, Bergamota, Mimosa', 4),
  (199, 'Daisy Love', 'Marc Jacobs', 420000.00, 100, 'Mora de los pantanos, Margaritas, Almizcle', 4),
  (200, 'Her Eau de Parfum', 'Burberry', 520000.00, 100, 'Fresa, Frambuesa, Mora zarzamora, Guinda', 4),
  (201, 'Olympea', 'Paco Rabanne', 490000.00, 80, 'Mandarina verde, Jazmín de agua, Flor de jengibre', 4);

-- Inventario: 10 unidades de cada perfume (despues se ajustan los que tienen movimientos)
INSERT INTO inventario (id_inventario, id_perfume, stock_disponible, estado_disponibilidad) VALUES
  (1, 1, 10, 'DISPONIBLE'),
  (2, 2, 10, 'DISPONIBLE'),
  (3, 3, 10, 'DISPONIBLE'),
  (4, 4, 10, 'DISPONIBLE'),
  (5, 5, 10, 'DISPONIBLE'),
  (6, 6, 10, 'DISPONIBLE'),
  (7, 7, 10, 'DISPONIBLE'),
  (8, 8, 10, 'DISPONIBLE'),
  (9, 9, 10, 'DISPONIBLE'),
  (10, 10, 10, 'DISPONIBLE'),
  (11, 11, 10, 'DISPONIBLE'),
  (12, 12, 10, 'DISPONIBLE'),
  (13, 13, 10, 'DISPONIBLE'),
  (14, 14, 10, 'DISPONIBLE'),
  (15, 15, 10, 'DISPONIBLE'),
  (16, 16, 10, 'DISPONIBLE'),
  (17, 17, 10, 'DISPONIBLE'),
  (18, 18, 10, 'DISPONIBLE'),
  (19, 19, 10, 'DISPONIBLE'),
  (20, 20, 10, 'DISPONIBLE'),
  (21, 21, 10, 'DISPONIBLE'),
  (22, 22, 10, 'DISPONIBLE'),
  (23, 23, 10, 'DISPONIBLE'),
  (24, 24, 10, 'DISPONIBLE'),
  (25, 25, 10, 'DISPONIBLE'),
  (26, 26, 10, 'DISPONIBLE'),
  (27, 27, 10, 'DISPONIBLE'),
  (28, 28, 10, 'DISPONIBLE'),
  (29, 29, 10, 'DISPONIBLE'),
  (30, 30, 10, 'DISPONIBLE'),
  (31, 31, 10, 'DISPONIBLE'),
  (32, 32, 10, 'DISPONIBLE'),
  (33, 33, 10, 'DISPONIBLE'),
  (34, 34, 10, 'DISPONIBLE'),
  (35, 35, 10, 'DISPONIBLE'),
  (36, 36, 10, 'DISPONIBLE'),
  (37, 37, 10, 'DISPONIBLE'),
  (38, 38, 10, 'DISPONIBLE'),
  (39, 39, 10, 'DISPONIBLE'),
  (40, 40, 10, 'DISPONIBLE'),
  (41, 41, 10, 'DISPONIBLE'),
  (42, 42, 10, 'DISPONIBLE'),
  (43, 43, 10, 'DISPONIBLE'),
  (44, 44, 10, 'DISPONIBLE'),
  (45, 45, 10, 'DISPONIBLE'),
  (46, 46, 10, 'DISPONIBLE'),
  (47, 47, 10, 'DISPONIBLE'),
  (48, 48, 10, 'DISPONIBLE'),
  (49, 49, 10, 'DISPONIBLE'),
  (50, 50, 10, 'DISPONIBLE'),
  (51, 51, 10, 'DISPONIBLE'),
  (52, 52, 10, 'DISPONIBLE'),
  (53, 53, 10, 'DISPONIBLE'),
  (54, 54, 10, 'DISPONIBLE'),
  (55, 55, 10, 'DISPONIBLE'),
  (56, 56, 10, 'DISPONIBLE'),
  (57, 57, 10, 'DISPONIBLE'),
  (58, 58, 10, 'DISPONIBLE'),
  (59, 59, 10, 'DISPONIBLE'),
  (60, 60, 10, 'DISPONIBLE'),
  (61, 61, 10, 'DISPONIBLE'),
  (62, 62, 10, 'DISPONIBLE'),
  (63, 63, 10, 'DISPONIBLE'),
  (64, 64, 10, 'DISPONIBLE'),
  (65, 65, 10, 'DISPONIBLE'),
  (66, 66, 10, 'DISPONIBLE'),
  (67, 67, 10, 'DISPONIBLE'),
  (68, 68, 10, 'DISPONIBLE'),
  (69, 69, 10, 'DISPONIBLE'),
  (70, 70, 10, 'DISPONIBLE'),
  (71, 71, 10, 'DISPONIBLE'),
  (72, 72, 10, 'DISPONIBLE'),
  (73, 73, 10, 'DISPONIBLE'),
  (74, 74, 10, 'DISPONIBLE'),
  (75, 75, 10, 'DISPONIBLE'),
  (76, 76, 10, 'DISPONIBLE'),
  (77, 77, 10, 'DISPONIBLE'),
  (78, 78, 10, 'DISPONIBLE'),
  (79, 79, 10, 'DISPONIBLE'),
  (80, 80, 10, 'DISPONIBLE'),
  (81, 81, 10, 'DISPONIBLE'),
  (82, 82, 10, 'DISPONIBLE'),
  (83, 83, 10, 'DISPONIBLE'),
  (84, 84, 10, 'DISPONIBLE'),
  (85, 85, 10, 'DISPONIBLE'),
  (86, 86, 10, 'DISPONIBLE'),
  (87, 87, 10, 'DISPONIBLE'),
  (88, 88, 10, 'DISPONIBLE'),
  (89, 89, 10, 'DISPONIBLE'),
  (90, 90, 10, 'DISPONIBLE'),
  (91, 91, 10, 'DISPONIBLE'),
  (92, 92, 10, 'DISPONIBLE'),
  (93, 93, 10, 'DISPONIBLE'),
  (94, 94, 10, 'DISPONIBLE'),
  (95, 95, 10, 'DISPONIBLE'),
  (96, 96, 10, 'DISPONIBLE'),
  (97, 97, 10, 'DISPONIBLE'),
  (98, 98, 10, 'DISPONIBLE'),
  (99, 99, 10, 'DISPONIBLE'),
  (100, 100, 10, 'DISPONIBLE'),
  (101, 101, 10, 'DISPONIBLE'),
  (102, 102, 10, 'DISPONIBLE'),
  (103, 103, 10, 'DISPONIBLE'),
  (104, 104, 10, 'DISPONIBLE'),
  (105, 105, 10, 'DISPONIBLE'),
  (106, 106, 10, 'DISPONIBLE'),
  (107, 107, 10, 'DISPONIBLE'),
  (108, 108, 10, 'DISPONIBLE'),
  (109, 109, 10, 'DISPONIBLE'),
  (110, 110, 10, 'DISPONIBLE'),
  (111, 111, 10, 'DISPONIBLE'),
  (112, 112, 10, 'DISPONIBLE'),
  (113, 113, 10, 'DISPONIBLE'),
  (114, 114, 10, 'DISPONIBLE'),
  (115, 115, 10, 'DISPONIBLE'),
  (116, 116, 10, 'DISPONIBLE'),
  (117, 117, 10, 'DISPONIBLE'),
  (118, 118, 10, 'DISPONIBLE'),
  (119, 119, 10, 'DISPONIBLE'),
  (120, 120, 10, 'DISPONIBLE'),
  (121, 121, 10, 'DISPONIBLE'),
  (122, 122, 10, 'DISPONIBLE'),
  (123, 123, 10, 'DISPONIBLE'),
  (124, 124, 10, 'DISPONIBLE'),
  (125, 125, 10, 'DISPONIBLE'),
  (126, 126, 10, 'DISPONIBLE'),
  (127, 127, 10, 'DISPONIBLE'),
  (128, 128, 10, 'DISPONIBLE'),
  (129, 129, 10, 'DISPONIBLE'),
  (130, 130, 10, 'DISPONIBLE'),
  (131, 131, 10, 'DISPONIBLE'),
  (132, 132, 10, 'DISPONIBLE'),
  (133, 133, 10, 'DISPONIBLE'),
  (134, 134, 10, 'DISPONIBLE'),
  (135, 135, 10, 'DISPONIBLE'),
  (136, 136, 10, 'DISPONIBLE'),
  (137, 137, 10, 'DISPONIBLE'),
  (138, 138, 10, 'DISPONIBLE'),
  (139, 139, 10, 'DISPONIBLE'),
  (140, 140, 10, 'DISPONIBLE'),
  (141, 141, 10, 'DISPONIBLE'),
  (142, 142, 10, 'DISPONIBLE'),
  (143, 143, 10, 'DISPONIBLE'),
  (144, 144, 10, 'DISPONIBLE'),
  (145, 145, 10, 'DISPONIBLE'),
  (146, 146, 10, 'DISPONIBLE'),
  (147, 147, 10, 'DISPONIBLE'),
  (148, 148, 10, 'DISPONIBLE'),
  (149, 149, 10, 'DISPONIBLE'),
  (150, 150, 10, 'DISPONIBLE'),
  (151, 151, 10, 'DISPONIBLE'),
  (152, 152, 10, 'DISPONIBLE'),
  (153, 153, 10, 'DISPONIBLE'),
  (154, 154, 10, 'DISPONIBLE'),
  (155, 155, 10, 'DISPONIBLE'),
  (156, 156, 10, 'DISPONIBLE'),
  (157, 157, 10, 'DISPONIBLE'),
  (158, 158, 10, 'DISPONIBLE'),
  (159, 159, 10, 'DISPONIBLE'),
  (160, 160, 10, 'DISPONIBLE'),
  (161, 161, 10, 'DISPONIBLE'),
  (162, 162, 10, 'DISPONIBLE'),
  (163, 163, 10, 'DISPONIBLE'),
  (164, 164, 10, 'DISPONIBLE'),
  (165, 165, 10, 'DISPONIBLE'),
  (166, 166, 10, 'DISPONIBLE'),
  (167, 167, 10, 'DISPONIBLE'),
  (168, 168, 10, 'DISPONIBLE'),
  (169, 169, 10, 'DISPONIBLE'),
  (170, 170, 10, 'DISPONIBLE'),
  (171, 171, 10, 'DISPONIBLE'),
  (172, 172, 10, 'DISPONIBLE'),
  (173, 173, 10, 'DISPONIBLE'),
  (174, 174, 10, 'DISPONIBLE'),
  (175, 175, 10, 'DISPONIBLE'),
  (176, 176, 10, 'DISPONIBLE'),
  (177, 177, 10, 'DISPONIBLE'),
  (178, 178, 10, 'DISPONIBLE'),
  (179, 179, 10, 'DISPONIBLE'),
  (180, 180, 10, 'DISPONIBLE'),
  (181, 181, 10, 'DISPONIBLE'),
  (182, 182, 10, 'DISPONIBLE'),
  (183, 183, 10, 'DISPONIBLE'),
  (184, 184, 10, 'DISPONIBLE'),
  (185, 185, 10, 'DISPONIBLE'),
  (186, 186, 10, 'DISPONIBLE'),
  (187, 187, 10, 'DISPONIBLE'),
  (188, 188, 10, 'DISPONIBLE'),
  (189, 189, 10, 'DISPONIBLE'),
  (190, 190, 10, 'DISPONIBLE'),
  (191, 191, 10, 'DISPONIBLE'),
  (192, 192, 10, 'DISPONIBLE'),
  (193, 193, 10, 'DISPONIBLE'),
  (194, 194, 10, 'DISPONIBLE'),
  (195, 195, 10, 'DISPONIBLE'),
  (196, 196, 10, 'DISPONIBLE'),
  (197, 197, 10, 'DISPONIBLE'),
  (198, 198, 10, 'DISPONIBLE'),
  (199, 199, 10, 'DISPONIBLE'),
  (200, 200, 10, 'DISPONIBLE'),
  (201, 201, 10, 'DISPONIBLE');


-- Ajustes de stock segun los movimientos de prueba de abajo (pedidos) y algunas alertas de bajo stock (HU-07)
-- perfume 1: -2 (pedido 2) -1 (pedido 6)  |  2: -1 (pedido 1) -2 (pedido 5)  |  3: -1 (pedido 4)
-- perfume 15: -1 (pedido 1) -1 (pedido 5)  |  36: -1 (pedido 3) -2 (pedido 4)
UPDATE inventario SET stock_disponible = 7, estado_disponibilidad = 'DISPONIBLE' WHERE id_inventario = 1;
UPDATE inventario SET stock_disponible = 7, estado_disponibilidad = 'DISPONIBLE' WHERE id_inventario = 2;
UPDATE inventario SET stock_disponible = 9, estado_disponibilidad = 'DISPONIBLE' WHERE id_inventario = 3;
UPDATE inventario SET stock_disponible = 8, estado_disponibilidad = 'DISPONIBLE' WHERE id_inventario = 15;
UPDATE inventario SET stock_disponible = 7, estado_disponibilidad = 'DISPONIBLE' WHERE id_inventario = 36;
UPDATE inventario SET stock_disponible = 3, estado_disponibilidad = 'BAJO_STOCK' WHERE id_inventario = 5;
UPDATE inventario SET stock_disponible = 0, estado_disponibilidad = 'AGOTADO'    WHERE id_inventario = 12;
UPDATE inventario SET stock_disponible = 4, estado_disponibilidad = 'BAJO_STOCK' WHERE id_inventario = 40;

-- Pedidos (HU-09, HU-12). Las fechas son relativas a HOY para que los reportes siempre tengan datos recientes
INSERT INTO pedido (id_pedido, fecha_pedido, id_cliente, total, observaciones, estado_pedido, metodo_pago, canal_venta) VALUES
  (1, CURDATE(),                        1, 460000, 'Cliente de Instagram, empacar para regalo', 'ENVIADO',   'PSE',        'INSTAGRAM'),
  (2, CURDATE(),                        2, 480000, 'Pago contra entrega por WhatsApp',          'PENDIENTE', 'EFECTIVO',   'WHATSAPP'),
  (3, CURDATE(),                        3, 260000, 'Venta en el local del C.C. Monterrey',      'ENTREGADO', 'T_CREDITO',  'LOCAL'),
  (4, DATE_SUB(CURDATE(), INTERVAL 12 DAY), 4, 790000, 'Pedido mayorista por Instagram',         'ENTREGADO', 'T_DEBITO',   'INSTAGRAM'),
  (5, DATE_SUB(CURDATE(), INTERVAL 5 DAY),  1, 710000, 'Pedido por WhatsApp',                    'PAGADO',    'PSE',        'WHATSAPP'),
  (6, DATE_SUB(CURDATE(), INTERVAL 50 DAY), 2, 240000, 'Venta del mes pasado en el local',       'ENTREGADO', 'EFECTIVO',   'LOCAL');

-- Detalle de cada pedido (HU-10 y HU-11): subtotal = precio x cantidad
INSERT INTO detalle_pedido (id_pedido, id_detalle, id_perfume, cantidad, precio_unitario, subtotal) VALUES
  (1, 1,  2, 1, 250000, 250000),
  (1, 2, 15, 1, 210000, 210000),
  (2, 1,  1, 2, 240000, 480000),
  (3, 1, 36, 1, 260000, 260000),
  (4, 1,  3, 1, 270000, 270000),
  (4, 2, 36, 2, 260000, 520000),
  (5, 1,  2, 2, 250000, 500000),
  (5, 2, 15, 1, 210000, 210000),
  (6, 1,  1, 1, 240000, 240000);

-- Envios (HU-13 y HU-14). El envio 3 se entrego hace 10 dias y el 4 hace 45 (sirven para probar HU-17)
INSERT INTO envio (id_envio, id_pedido, transportadora, numero_guia, direccion_destino, fecha_envio, fecha_entrega, costo_envio, estado_envio) VALUES
  (1, 1, 'Interrapidisimo', 'IR-778812', 'Calle 10 #20-30',            CURDATE(), NULL,     9500,  'EN_CAMINO'),
  (2, 3, 'Servientrega',    'SV-100245', 'Calle 33 #65-12, Laureles',   CURDATE(), CURDATE(), 12000, 'ENTREGADO'),
  (3, 4, 'Servientrega',    'SV-100301', 'Calle 50 #40-10, Itagüí',     DATE_SUB(CURDATE(), INTERVAL 14 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY), 15000, 'ENTREGADO'),
  (4, 6, 'Coordinadora',    'CO-554120', 'Cra 43A #1-50, El Poblado',   DATE_SUB(CURDATE(), INTERVAL 49 DAY), DATE_SUB(CURDATE(), INTERVAL 45 DAY), 11000, 'ENTREGADO');

-- Ordenes de compra a proveedores (HU-16)
INSERT INTO orden_compra (id_orden_compra, id_proveedor, fecha_orden, estado, total, observaciones) VALUES
  (1, 1, CURDATE(), 'APROBADA',  4500000, 'Reposicion de Lattafa y Armaf'),
  (2, 2, CURDATE(), 'PENDIENTE', 2800000, 'Perfumeria femenina para diciembre');

-- Devolucion SOLICITADA del pedido 3 por arrepentimiento (HU-17, HU-18, HU-19): el administrador la aprueba o rechaza
INSERT INTO devolucion (id_devolucion, id_pedido, fecha_entrega, fecha_solicitud, motivo, descripcion, total_pagado, estado, monto_reembolso) VALUES
  (1, 3, CURDATE(), CURDATE(), 'ARREPENTIMIENTO_DE_COMPRA', 'El cliente queria una fragancia mas dulce', 260000, 'SOLICITADA', 208000);

-- Garantias (HU-20 y HU-21): la 1 esta vigente y la 2 ya vencio
INSERT INTO garantia (id_garantia, id_pedido, fecha_inicio, meses_cobertura, descripcion) VALUES
  (1, 3, CURDATE(), 6, 'Garantia de fabrica contra defectos'),
  (2, 6, DATE_SUB(CURDATE(), INTERVAL 45 DAY), 1, 'Garantia de 1 mes por defecto de fabrica');
