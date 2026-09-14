-- BD 3: POSTGRESQL (Matrículas)
CREATE TABLE IF NOT EXISTS matricula (
    id_matricula SERIAL PRIMARY KEY,
    id_estudiante INT NOT NULL,
    id_carrera INT NOT NULL,
    periodo_academico VARCHAR(10) NOT NULL,
    fecha_matricula DATE NOT NULL
);

TRUNCATE TABLE matricula RESTART IDENTITY;

INSERT INTO matricula (id_estudiante, id_carrera, periodo_academico, fecha_matricula) VALUES
(1, 1, '2026-I', '2026-03-01'),
(2, 2, '2026-I', '2026-03-02'),
(3, 3, '2026-I', '2026-03-03'),
(4, 4, '2026-I', '2026-03-04'),
(5, 5, '2026-I', '2026-03-05');

SELECT * FROM matricula;