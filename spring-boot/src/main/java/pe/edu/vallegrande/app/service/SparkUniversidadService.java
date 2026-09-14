    package pe.edu.vallegrande.app.service;

    import org.apache.spark.sql.Dataset;
    import org.apache.spark.sql.Row;
    import org.apache.spark.sql.SparkSession;
    import org.apache.spark.sql.types.DataTypes;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import static org.apache.spark.sql.functions.col;
    import static org.apache.spark.sql.functions.lower;
    import static org.apache.spark.sql.functions.trim;
    import static org.apache.spark.sql.functions.when;

    @Service
    public class SparkUniversidadService {

        private final SparkSession spark;

        // SQL Server
        private final String sqlServerUrl;
        private final String sqlServerUsername;
        private final String sqlServerPassword;
        private final String sqlServerDriver;

        // MariaDB
        private final String mariaDbUrl;
        private final String mariaDbUsername;
        private final String mariaDbPassword;
        private final String mariaDbDriver;

        public SparkUniversidadService(
                SparkSession spark,

                @Value("${app.sqlserver.url}") String sqlServerUrl,
                @Value("${app.sqlserver.username}") String sqlServerUsername,
                @Value("${app.sqlserver.password}") String sqlServerPassword,
                @Value("${app.sqlserver.driver}") String sqlServerDriver,

                @Value("${app.mariadb.url}") String mariaDbUrl,
                @Value("${app.mariadb.username}") String mariaDbUsername,
                @Value("${app.mariadb.password}") String mariaDbPassword,
                @Value("${app.mariadb.driver}") String mariaDbDriver
        ) {

            this.spark = spark;

            this.sqlServerUrl = sqlServerUrl;
            this.sqlServerUsername = sqlServerUsername;
            this.sqlServerPassword = sqlServerPassword;
            this.sqlServerDriver = sqlServerDriver;

            this.mariaDbUrl = mariaDbUrl;
            this.mariaDbUsername = mariaDbUsername;
            this.mariaDbPassword = mariaDbPassword;
            this.mariaDbDriver = mariaDbDriver;
        }

        public Dataset<Row> obtenerResumen() {

        Dataset<Row> estudiantes = leerEstudiantes();
        Dataset<Row> matriculas = leerMatriculas();

        // Realizar el JOIN directamente por los IDs numéricos
        Dataset<Row> resultado = estudiantes
                .join(
                        matriculas,
                        estudiantes.col("id").equalTo(matriculas.col("estudiante_id")),
                        "left"
                )
                .select(
                        estudiantes.col("codigo"),
                        estudiantes.col("nombre"),
                        estudiantes.col("apellido"),
                        estudiantes.col("carrera"),
                        matriculas.col("curso"),
                        matriculas.col("nota")
                );

        return resultado;
    }

        public List<Map<String, Object>> obtenerResumenLista() {
            Dataset<Row> dataset = obtenerResumen();
            List<Row> rows = dataset.collectAsList();
            String[] columnas = dataset.columns();

            List<Map<String, Object>> resultado = new ArrayList<>();
            for (Row row : rows) {
                Map<String, Object> fila = new HashMap<>();
                for (String col : columnas) {
                    fila.put(col, row.getAs(col));
                }
                resultado.add(fila);
            }
            return resultado;
        }

        private Dataset<Row> leerEstudiantes() {
            return spark.read()
                    .format("jdbc")
                    .option("url", sqlServerUrl)
                    .option("dbtable", "dbo.estudiantes")
                    .option("user", sqlServerUsername)
                    .option("password", sqlServerPassword)
                    .option("driver", sqlServerDriver)
                    .load();
        }

        private Dataset<Row> leerMatriculas() {
        return spark.read()
                .format("jdbc")
                .option("url", mariaDbUrl)
                .option("dbtable", "matriculas") // Lee directamente la tabla
                .option("user", mariaDbUsername)
                .option("password", mariaDbPassword)
                .option("driver", mariaDbDriver)
                .load();
    }
    }