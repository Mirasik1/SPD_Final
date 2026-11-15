package adapter;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class ExternalPriceService {
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();
    private final String baseUrl;

    public ExternalPriceService(String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
    }

    public String fetchPrice(String material) throws IOException, InterruptedException {
        String encodedMaterial = URLEncoder.encode(material, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/prices/" + encodedMaterial))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Marketplace returned HTTP " + response.statusCode());
        }
        return response.body();
    }
}
