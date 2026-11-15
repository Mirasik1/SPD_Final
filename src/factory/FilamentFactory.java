package factory;

import adapter.FilamentPriceProvider;
import java.io.IOException;

public class FilamentFactory {
    public static Filament createFilament(String type){
        if (type==null || type.isBlank()){
            throw new IllegalArgumentException("Filament type cannot be empty.");
        }
        return switch (type.trim().toUpperCase()){
            case "PLA" -> new PLAFilament();
            case "PETG" -> new PETGFilament();
            case "ABS" -> new ABSFilament();
            case "TPU" -> new TPUFilament();

            default -> throw new IllegalArgumentException("Unknown filament type: " + type);
        };
    }

    public static Filament createFilament(String type, FilamentPriceProvider prices) {
        Filament filament = createFilament(type);
        try {
            filament.setPricePerKg(prices.getPricePerKg(filament.getName()));
            System.out.printf("Marketplace price for %s: $%.2f/kg%n",
                    filament.getName(), filament.getPricePerKg());
        } catch (IOException e) {
            System.out.println("Marketplace unavailable for " + filament.getName()
                    + ": " + e.getMessage() + ". Using default price.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Marketplace request interrupted. Using default price for "
                    + filament.getName());
        }
        return filament;
    }
}
