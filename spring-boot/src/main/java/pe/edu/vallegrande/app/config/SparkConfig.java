package pe.edu.vallegrande.app.config;

import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName("SpringBootSparkApp")
                .master("local[*]")
                // Desactiva la Web UI interna para evitar choques entre Jakarta EE (Spring Boot 3) y javax.servlet (Spark UI)
                .config("spark.ui.enabled", "false")
                // Permisos de módulos para compatibilidad con Java 21
                .config("spark.driver.extraJavaOptions",
                        "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED " +
                        "--add-opens=java.base/java.nio=ALL-UNNAMED " +
                        "--add-opens=java.base/java.util=ALL-UNNAMED")
                .config("spark.executor.extraJavaOptions",
                        "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED " +
                        "--add-opens=java.base/java.nio=ALL-UNNAMED " +
                        "--add-opens=java.base/java.util=ALL-UNNAMED")
                .getOrCreate();
    }
}