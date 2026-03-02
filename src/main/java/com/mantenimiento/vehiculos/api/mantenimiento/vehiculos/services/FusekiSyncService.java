package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class FusekiSyncService {

    private static final String FUSEKI_UPDATE_ENDPOINT = "http://localhost:3030/taller/update";

    private static final String TARGET_GRAPH = "<http://taller.com/datos>";

    public void syncEntity(String resourceUri, Model entityModel) {
        try {
            StringWriter sw = new StringWriter();
            entityModel.write(sw, "N-TRIPLE");
            String triples = sw.toString();

            String sparqlUpdate =
                    "WITH " + TARGET_GRAPH + " \n" +
                            "DELETE { <" + resourceUri + "> ?p ?o } \n" +
                            "WHERE { <" + resourceUri + "> ?p ?o }; \n" +
                            "INSERT DATA { \n" +
                            "  GRAPH " + TARGET_GRAPH + " { \n" +
                            "    " + triples + "\n" +
                            "  } \n" +
                            "}";

            HttpClient http = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FUSEKI_UPDATE_ENDPOINT))
                    .header("Content-Type", "application/sparql-update")
                    .POST(HttpRequest.BodyPublishers.ofString(sparqlUpdate))
                    .build();

            // Guardamos la respuesta para evaluar el estado
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("[FusekiSyncService] Éxito: Entidad sincronizada en el grafo " + TARGET_GRAPH + " -> " + resourceUri);
            } else {
                System.err.println("[FusekiSyncService] Error sincronizando en Fuseki. Status: " + response.statusCode());
                System.err.println("Detalle de respuesta: " + response.body());
            }

        } catch (Exception e) {
            System.err.println("[FusekiSyncService] Excepción en syncEntity: " + e.getMessage());
        }
    }

    public void deleteEntity(String resourceUri) {
        try {
            // Usamos WITH para el borrado también
            String sparqlUpdate =
                    "WITH " + TARGET_GRAPH + " \n" +
                            "DELETE { <" + resourceUri + "> ?p ?o } \n" +
                            "WHERE { <" + resourceUri + "> ?p ?o };";

            HttpClient http = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FUSEKI_UPDATE_ENDPOINT))
                    .header("Content-Type", "application/sparql-update")
                    .POST(HttpRequest.BodyPublishers.ofString(sparqlUpdate))
                    .build();

            // Guardamos la respuesta para evaluar el estado
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("🗑️ [FusekiSyncService] Éxito: Entidad eliminada del grafo " + TARGET_GRAPH + " -> " + resourceUri);
            } else {
                System.err.println("[FusekiSyncService] Error eliminando en Fuseki. Status: " + response.statusCode());
                System.err.println("Detalle de respuesta: " + response.body());
            }

        } catch (Exception e) {
            System.err.println("[FusekiSyncService] Excepción en deleteEntity: " + e.getMessage());
        }
    }
}