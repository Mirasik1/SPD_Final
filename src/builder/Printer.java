package builder;

import factory.Filament;
import observer.PrintObserver;
import strategy.PrintStrategy;
import strategy.StandardPrintStrategy;

import java.util.ArrayList;
import java.util.List;

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
        this.currentStrategy = strategy;
    }

    public void addObserver(PrintObserver observer) {
        observers.add(observer);
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
        String fileName = filePath.substring(Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\')) + 1);

        // 1. compatibility checks
        if (filament.getNozzleTemp() > maxNozzleTemp) {
            System.out.println("ERROR: nozzle of " + modelName + " cannot reach "
                    + filament.getNozzleTemp() + "°C for " + filament.getName());
            notifyObservers(fileName, "FAILED", 0, 0);
            return;
        }
        if (filament.getBedTemp() > maxBedTemp) {
            System.out.println("ERROR: bed of " + modelName + " cannot reach "
                    + filament.getBedTemp() + "°C for " + filament.getName());
            notifyObservers(fileName, "FAILED", 0, 0);
            return;
        }
        if (filament.isRequiresEnclosure() && !hasEnclosure) {
            System.out.println("ERROR: " + filament.getName() + " requires an enclosure, "
                    + modelName + " has none");
            notifyObservers(fileName, "FAILED", 0, 0);
            return;
        }

        // 2. default strategy
        if (currentStrategy == null) {
            currentStrategy = new StandardPrintStrategy();
        }

        // 3. time, weight and cost
        double timeMinutes = modelVolumeCm3 * 6 * currentStrategy.getTimeMultiplier();
        double weightGrams = modelVolumeCm3 * filament.getDensity();
        double totalCost = weightGrams / 1000 * filament.getPricePerKg();

        System.out.println("\nPrinter: " + modelName);
        System.out.println("Mode: " + currentStrategy.getName()
                + " (layer " + currentStrategy.getLayerHeightMm() + " mm)");
        System.out.printf("Estimated time: %.1f min, filament: %.1f g, cost: %.2f%n",
                timeMinutes, weightGrams, totalCost);

        // 4. print simulation
        System.out.println("Heating bed to " + filament.getBedTemp() + "°C...");
        System.out.println("Heating nozzle to " + filament.getNozzleTemp() + "°C...");
        System.out.println("Printing file " + filePath + "...");

        // 5. notify subscribers
        notifyObservers(fileName, "SUCCESS", totalCost, timeMinutes);
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
