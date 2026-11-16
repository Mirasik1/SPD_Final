package factory;

import adapter.FilamentPriceProvider;
import java.io.IOException;
import java.util.Objects;

public abstract class FilamentFactory {
    public abstract Filament createFilament();

    public Filament createFilament(FilamentPriceProvider prices) {
        Objects.requireNonNull(prices, "Price provider cannot be null");
        Filament filament = createFilament();
        try {
            filament.setPricePerKg(prices.getPricePerKg(filament.getName()));
            System.out.printf("Marketplace price for %s: $%.2f/kg%n",
                    filament.getName(), filament.getPricePerKg());
        } catch (IOException e) {
            System.out.println("Marketplace unavailable for " + filament.getName()
                    + ": " + e.getClass().getSimpleName() + ". Using default price.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Marketplace request interrupted. Using default price for "
                    + filament.getName());
        }
        return filament;
    }
}
