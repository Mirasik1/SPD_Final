package builder;

public class Printer {
    private final String modelName;
    private final double nozzleDiameter;
    private final int maxNozzleTemp;
    private final int maxBedTemp;
    private final boolean hasAMS;
    private final boolean hasEnclosure;
    private final boolean hasHardenedNozzle;
    private final String bedType;

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
