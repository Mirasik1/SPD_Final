package adapter;

import java.io.IOException;

public interface FilamentPriceProvider {
    double getPricePerKg(String material) throws IOException, InterruptedException;
}
