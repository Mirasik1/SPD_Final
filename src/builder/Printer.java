package builder;

import decorator.BasePrintJobConfig;
import decorator.PrintJobConfig;
import factory.Filament;
import observer.PrintObserver;
import strategy.PrintStrategy;
import strategy.StandardPrintStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Printer {
    private final String modelName;
    private final double nozzleDiameter;
    private final int maxNozzleTemp;
    private final int maxBedTemp;
    private final boolean hasAMS;
    private final boolean hasEnclosure;
    private final boolean hasHardenedNozzle;
    private final String bedType;

    private PrintStrategy currentStrategy;
    private final PrintCalculator calculator = new PrintCalculator();
    private final List<PrintObserver> observers = new ArrayList<>();

    private Printer(PrinterBuilder builder){
        this.modelName = builder.getModelName();
        this.nozzleDiameter = builder.getNozzleDiameter();
        this.maxNozzleTemp = builder.getMaxNozzleTemp();
        this.maxBedTemp = builder.getMaxBedTemp();
        this.hasAMS = builder.isHasAMS();
        this.hasEnclosure = builder.isHasEnclosure();
        this.hasHardenedNozzle = builder.isHasHardenedNozzle();
        this.bedType = builder.getBedType();
    }
    public String getModelName() {
        return modelName;
    }

    public double getNozzleDiameter() {
        return nozzleDiameter;
    }

    public int getMaxNozzleTemp() {
        return maxNozzleTemp;
    }

    public int getMaxBedTemp() {
        return maxBedTemp;
    }

    public boolean isHasAMS() {
        return hasAMS;
    }

    public boolean isHasEnclosure() {
        return hasEnclosure;
    }

    public boolean isHasHardenedNozzle() {
        return hasHardenedNozzle;
    }

    public String getBedType() {
        return bedType;
    }

    public void setStrategy(PrintStrategy strategy) {
        this.currentStrategy = Objects.requireNonNull(strategy, "Print strategy cannot be null");
    }

    public void addObserver(PrintObserver observer) {
        observers.add(Objects.requireNonNull(observer, "Print observer cannot be null"));
    }

    public void removeObserver(PrintObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String fileName, String status, double totalCost, double timeMinutes) {
        for (PrintObserver observer : observers) {
            observer.onPrintFinished(fileName, status, totalCost, timeMinutes);
        }
    }

    public void performPrint(String filePath, Filament filament, double modelVolumeCm3) {
        performPrint(filePath, filament, modelVolumeCm3, new BasePrintJobConfig());
    }

    public void performPrint(String filePath, Filament filament, double modelVolumeCm3,
                             PrintJobConfig config) {
        if (filePath == null || filePath.isBlank() || filament == null || config == null
                || !Double.isFinite(modelVolumeCm3) || modelVolumeCm3 <= 0) {
            throw new IllegalArgumentException("Print job requires a file, filament, positive volume and settings");
        }
        String fileName = filePath.substring(Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\')) + 1);

        String compatibilityError = getCompatibilityError(filament);
        if (compatibilityError != null) {
            System.out.println("ERROR: " + compatibilityError);
            notifyObservers(fileName, "FAILED", 0, 0);
            return;
        }

        PrintStrategy strategy = currentStrategy == null ? new StandardPrintStrategy() : currentStrategy;
        PrintMetrics metrics = calculator.calculate(modelVolumeCm3, filament, strategy, config);
        printEstimate(strategy, config, metrics);
        simulatePrinting(filePath, filament);
        notifyObservers(fileName, "SUCCESS", metrics.cost(), metrics.timeMinutes());
    }

    private String getCompatibilityError(Filament filament) {
        if (filament.getNozzleTemp() > maxNozzleTemp) {
            return "nozzle of " + modelName + " cannot reach "
                    + filament.getNozzleTemp() + "°C for " + filament.getName();
        }
        if (filament.getBedTemp() > maxBedTemp) {
            return "bed of " + modelName + " cannot reach "
                    + filament.getBedTemp() + "°C for " + filament.getName();
        }
        if (filament.isRequiresEnclosure() && !hasEnclosure) {
            return filament.getName() + " requires an enclosure, " + modelName + " has none";
        }
        return null;
    }

    private void printEstimate(PrintStrategy strategy, PrintJobConfig config, PrintMetrics metrics) {
        System.out.println("\nPrinter: " + modelName);
        System.out.println("Mode: " + strategy.getName()
                + " (layer " + strategy.getLayerHeightMm() + " mm)");
        System.out.println("Settings: " + config.getDescription());
        System.out.printf("Estimated time: %.1f min, filament: %.1f g, cost: %.2f%n",
                metrics.timeMinutes(), metrics.weightGrams(), metrics.cost());
    }

    private void simulatePrinting(String filePath, Filament filament) {
        System.out.println("Heating bed to " + filament.getBedTemp() + "°C...");
        System.out.println("Heating nozzle to " + filament.getNozzleTemp() + "°C...");
        System.out.println("Printing file " + filePath + "...");
    }


    @Override
    public String toString() {
        return "\n========== PRINTER CONFIGURATION =========="
                + "\nModel: " + modelName
                + "\nNozzle diameter: " + nozzleDiameter + " mm"
                + "\nMaximum nozzle temperature: " + maxNozzleTemp + "°C"
                + "\nMaximum bed temperature: " + maxBedTemp + "°C"
                + "\nAMS system: " + (hasAMS ? "Yes" : "No")
                + "\nEnclosure: " + (hasEnclosure ? "Yes" : "No")
                + "\nHardened nozzle: " + (hasHardenedNozzle ? "Yes" : "No")
                + "\nBed type: " + bedType
                + "\n==========================================";
    }
    static Printer create(PrinterBuilder builder) {
        return new Printer(builder);
    }
}
