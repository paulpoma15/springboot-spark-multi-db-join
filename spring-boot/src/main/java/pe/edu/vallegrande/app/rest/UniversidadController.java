package pe.edu.vallegrande.app.rest;

import pe.edu.vallegrande.app.service.SparkUniversidadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/universidad")
public class UniversidadController {

    private final SparkUniversidadService sparkService;

    public UniversidadController(SparkUniversidadService sparkService) {
        this.sparkService = sparkService;
    }

    @GetMapping("/matriculas")
    public List<Map<String, Object>> obtenerMatriculas() {
        return sparkService.obtenerResumenLista();
    }
}