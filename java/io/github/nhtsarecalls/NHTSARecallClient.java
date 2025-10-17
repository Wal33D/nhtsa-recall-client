package io.github.nhtsarecalls;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Java wrapper for the public NHTSA recall API.
 */
public class NHTSARecallClient {

    private static final String DEFAULT_BASE_URL = "https://api.nhtsa.gov/recalls";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public NHTSARecallClient() {
        this(DEFAULT_BASE_URL, HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build(), new ObjectMapper());
    }

    public NHTSARecallClient(String baseUrl, HttpClient httpClient, ObjectMapper mapper) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    public List<RecallRecord> getRecallsByVin(String vin) {
        return fetch("/recallsByVIN", Map.of("vin", vin));
    }

    public List<RecallRecord> getRecallsByVehicle(String make, String model, String modelYear) {
        return fetch("/recallsByVehicle", Map.of("make", make, "model", model, "modelYear", modelYear));
    }

    public List<RecallRecord> getRecallsByCampaign(String campaignNumber) {
        return fetch("/recallsByCampaign", Map.of("campaignNumber", campaignNumber));
    }

    private List<RecallRecord> fetch(String path, Map<String, String> query) {
        try {
            String queryString = query.entrySet()
                    .stream()
                    .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                    .collect(Collectors.joining("&"));

            URI uri = new URI(baseUrl + path + "?" + queryString + "&format=json");

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(10))
                    .header("User-Agent", "nhtsa-recall-client-java/0.1")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new IllegalStateException("NHTSA recall API error: " + response.statusCode());
            }

            return parseRecords(response.body());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalStateException("Failed to fetch NHTSA recall data", e);
        }
    }

    List<RecallRecord> parseRecords(String json) throws IOException {
        JsonNode root = mapper.readTree(json);
        JsonNode results = root.get("results") != null ? root.get("results") : root.get("Results");
        if (results == null || !results.isArray()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> rawList = mapper.convertValue(
                results, new TypeReference<List<Map<String, Object>>>() {});

        List<RecallRecord> recalls = new ArrayList<>();
        for (Map<String, Object> entry : rawList) {
            recalls.add(mapToRecord(entry));
        }
        return recalls;
    }

    private static String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private RecallRecord mapToRecord(Map<String, Object> entry) {
        return new RecallRecord(
                (String) entry.get("ReportReceivedDate"),
                (String) entry.get("Manufacturer"),
                (String) entry.get("Component"),
                (String) entry.get("Summary"),
                valueAsString(entry.get("Consequence"), entry.get("Conequence")),
                (String) entry.get("Remedy"),
                (String) entry.get("Notes"),
                (String) entry.get("NHTSACampaignNumber"),
                (String) entry.get("RecallNumber"),
                valueAsString(entry.get("ModelYear")),
                (String) entry.get("Make"),
                (String) entry.get("Model"),
                (String) entry.get("VIN"),
                entry
        );
    }

    private static String valueAsString(Object... values) {
        for (Object value : values) {
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
}
