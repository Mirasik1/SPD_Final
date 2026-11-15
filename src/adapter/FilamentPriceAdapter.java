package adapter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilamentPriceAdapter implements FilamentPriceProvider {
    private static final Pattern PRICE = Pattern.compile("\"price\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");
    private static final Pattern WEIGHT = Pattern.compile("\"weight_kg\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");
    private static final Pattern CURRENCY = Pattern.compile("\"currency\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern MATERIAL = Pattern.compile("\"material\"\\s*:\\s*\"([^\"]+)\"");

    private final ExternalPriceService service;

    public FilamentPriceAdapter(ExternalPriceService service) {
        this.service = service;
    }

    @Override
    public double getPricePerKg(String material) throws IOException, InterruptedException {
        String json = service.fetchPrice(material);
        String responseMaterial = extract(MATERIAL, json, "material");
        String currency = extract(CURRENCY, json, "currency");
        if (!responseMaterial.equalsIgnoreCase(material) || !currency.equals("USD")) {
            throw new IOException("Unexpected material or currency in marketplace response");
        }

        double price = parsePositive(PRICE, json, "price");
        double weightKg = parsePositive(WEIGHT, json, "weight_kg");
        double pricePerKg = price / weightKg;
        if (!Double.isFinite(pricePerKg) || pricePerKg <= 0) {
            throw new IOException("Invalid price per kilogram");
        }
        return pricePerKg;
    }

    private static String extract(Pattern pattern, String json, String field) throws IOException {
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            throw new IOException("Missing or invalid field: " + field);
        }
        return matcher.group(1);
    }

    private static double parsePositive(Pattern pattern, String json, String field) throws IOException {
        double value = Double.parseDouble(extract(pattern, json, field));
        if (!Double.isFinite(value) || value <= 0) {
            throw new IOException("Invalid value for " + field);
        }
        return value;
    }
}
