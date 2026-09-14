-- BD 2: SQL SERVER (Carreras)
USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = 'universidad_db')
BEGIN
    ALTER DATABASE universidad_db SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE universidad_db;
END;
GO

CREATE DATABASE universidad_db;
GO

USE universidad_db;
GO

CREATE TABLE carrera_universidad (
    id_carrera INT IDENTITY(1,1) PRIMARY KEY,
    nombre_carrera VARCHAR(100) NOT NULL,
    facultad VARCHAR(100) NOT NULL,
    duracion_semestres INT NOT NULL
);

INSERT INTO carrera_universidad (nombre_carrera, facultad, duracion_semestres) VALUES
('Ingeniería de Sistemas', 'Ingeniería', 10),
('Diseño Gráfico', 'Arte y Diseño', 6),
('Administración de Empresas', 'Ciencias Empresariales', 10),
('Marketing Digital', 'Ciencias Empresariales', 8),
('Redes y Seguridad', 'Ingeniería', 6);

SELECT * FROM carrera_universidad;