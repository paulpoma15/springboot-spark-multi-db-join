-- BD 1: MYSQL (Estudiantes)
USE universidad;

CREATE TABLE IF NOT EXISTS estudiante (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(8) NOT NULL,
    correo VARCHAR(100) NOT NULL
);

-- Limpiar e Insertar 5 registros
TRUNCATE TABLE estudiante;

INSERT INTO estudiante (nombre, apellido, dni, correo) VALUES
('Juan', 'Pérez', '71234567', 'juan.perez@vallegrande.edu.pe'),
('Maria', 'Gómez', '72345678', 'maria.gomez@vallegrande.edu.pe'),
('Carlos', 'López', '73456789', 'carlos.lopez@vallegrande.edu.pe'),
('Ana', 'Torres', '74567890', 'ana.torres@vallegrande.edu.pe'),
('Luis', 'Ramírez', '75678901', 'luis.ramirez@vallegrande.edu.pe');

SELECT * FROM estudiante;