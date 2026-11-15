package builder;

import decorator.PrintJobConfig;
import factory.Filament;
import strategy.PrintStrategy;

public class PrintCalculator {
    private static final double MINUTES_PER_CM3 = 6.0;
    private static final double GRAMS_PER_KG = 1000.0;

    public PrintMetrics calculate(double modelVolumeCm3, Filament filament,
                                  PrintStrategy strategy, PrintJobConfig config) {
        double timeMinutes = modelVolumeCm3 * MINUTES_PER_CM3 * strategy.getTimeMultiplier()
                * config.getTimeMultiplier();
        double weightGrams = modelVolumeCm3 * filament.getDensity()
                * config.getMaterialMultiplier();
        double totalCost = weightGrams / GRAMS_PER_KG * filament.getPricePerKg();
        if (!Double.isFinite(timeMinutes) || !Double.isFinite(weightGrams)
                || !Double.isFinite(totalCost) || timeMinutes <= 0 || weightGrams <= 0
                || totalCost <= 0) {
            throw new IllegalArgumentException("Print settings produce invalid metrics");
        }
        return new PrintMetrics(timeMinutes, weightGrams, totalCost);
    }
}
