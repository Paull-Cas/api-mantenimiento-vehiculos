package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.config;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.RdfExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FusekiStartupExporter implements ApplicationRunner {

    private final RdfExportService rdfExportService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            rdfExportService.exportarTodo();
            System.out.println("✅ Datos exportados a Fuseki correctamente al iniciar");
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo exportar a Fuseki: " + e.getMessage());
        }
    }
}