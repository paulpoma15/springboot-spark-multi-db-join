package pe.edu.vallegrande.app.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SparkUniversidadService {

    private final SparkSession spark;

    // MySQL
    private final String mysqlUrl;
    private final String mysqlUsername;
    private final String mysqlPassword;
    private final String mysqlDriver;

    // SQL Server
    private final String sqlServerUrl;
    private final String sqlServerUsername;
    private final String sqlServerPassword;
    private final String sqlServerDriver;

    // PostgreSQL Neon
    private final String postgresUrl;
    private final String postgresUsername;
    private final String postgresPassword;
    private final String postgresDriver;

    public SparkUniversidadService(
            SparkSession spark,

            @Value("${app.mysql.url}") String mysqlUrl,
            @Value("${app.mysql.username}") String mysqlUsername,
            @Value("${app.mysql.password}") String mysqlPassword,
            @Value("${app.mysql.driver}") String mysqlDriver,

            @Value("${app.sqlserver.url}") String sqlServerUrl,
            @Value("${app.sqlserver.username}") String sqlServerUsername,
            @Value("${app.sqlserver.password}") String sqlServerPassword,
            @Value("${app.sqlserver.driver}") String sqlServerDriver,

            @Value("${app.postgres.url}") String postgresUrl,
            @Value("${app.postgres.username}") String postgresUsername,
            @Value("${app.postgres.password}") String postgresPassword,
            @Value("${app.postgres.driver}") String postgresDriver
    ) {
        this.spark = spark;

        this.mysqlUrl = mysqlUrl;
        this.mysqlUsername = mysqlUsername;
        this.mysqlPassword = mysqlPassword;
        this.mysqlDriver = mysqlDriver;

        this.sqlServerUrl = sqlServerUrl;
        this.sqlServerUsername = sqlServerUsername;
        this.sqlServerPassword = sqlServerPassword;
        this.sqlServerDriver = sqlServerDriver;

        this.postgresUrl = postgresUrl;
        this.postgresUsername = postgresUsername;
        this.postgresPassword = postgresPassword;
        this.postgresDriver = postgresDriver;
    }

    public Dataset<Row> obtenerResumen() {
    Dataset<Row> dfEstudiantes = leerEstudiantesMySQL();
    Dataset<Row> dfCarreras = leerCarrerasSQLServer();
    Dataset<Row> dfMatriculas = leerMatriculasPostgreSQL();

    // Registrar como vistas temporales en Spark
    dfEstudiantes.createOrReplaceTempView("estudiante");
    dfCarreras.createOrReplaceTempView("carrera_universidad");
    dfMatriculas.createOrReplaceTempView("matricula");

    // Ejecutar JOIN SQL con el espacio correcto antes de ORDER BY
    return spark.sql(
        "SELECT " +
        "   e.id_estudiante, " +
        "   e.nombre, " +
        "   e.apellido, " +
        "   c.nombre_carrera, " +
        "   m.periodo_academico, " +
        "   m.fecha_matricula " +
        "FROM matricula m " +
        "JOIN estudiante e ON m.id_estudiante = e.id_estudiante " +
        "JOIN carrera_universidad c ON m.id_carrera = c.id_carrera " + // <-- Espacio agregado al final
        "ORDER BY e.id_estudiante ASC"
    );
}

    public List<Map<String, Object>> obtenerResumenLista() {
        Dataset<Row> dataset = obtenerResumen();
        List<Row> rows = dataset.collectAsList();
        String[] columnas = dataset.columns();

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Row row : rows) {
            Map<String, Object> fila = new java.util.LinkedHashMap<>();
            for (String col : columnas) {
                fila.put(col, row.getAs(col));
            }
            resultado.add(fila);
        }
        return resultado;
    }

    private Dataset<Row> leerEstudiantesMySQL() {
        return spark.read()
                .format("jdbc")
                .option("url", mysqlUrl)
                .option("dbtable", "estudiante")
                .option("user", mysqlUsername)
                .option("password", mysqlPassword)
                .option("driver", mysqlDriver)
                .load();
    }

    private Dataset<Row> leerCarrerasSQLServer() {
        return spark.read()
                .format("jdbc")
                .option("url", sqlServerUrl)
                .option("dbtable", "dbo.carrera_universidad")
                .option("user", sqlServerUsername)
                .option("password", sqlServerPassword)
                .option("driver", sqlServerDriver)
                .load();
    }

    private Dataset<Row> leerMatriculasPostgreSQL() {
        return spark.read()
                .format("jdbc")
                .option("url", postgresUrl)
                .option("dbtable", "matricula")
                .option("user", postgresUsername)
                .option("password", postgresPassword)
                .option("driver", postgresDriver)
                .load();
    }
}