package builder;

public class PrinterBuilder {
    private final String modelName;

    private double nozzleDiameter = 0.4;
    private int maxNozzleTemp = 260;
    private int maxBedTemp = 100;
    private boolean hasAMS = false;
    private boolean hasEnclosure = false;
    private boolean hasHardenedNozzle = false;
    private String bedType = "Textured PEI";

    public PrinterBuilder(String modelName){
        if (modelName==null || modelName.isBlank()){
            throw new IllegalArgumentException("Printer model name cannot be empty.");
        }
        this.modelName=modelName;
    }
    public PrinterBuilder setNozzleDiameter(double diameter){
        if (diameter<=0 || !Double.isFinite(diameter)){
            throw new IllegalArgumentException("Nozzle diameter must be positive.");
        }
        this.nozzleDiameter=diameter;
        return this;
    }
    public PrinterBuilder setMaxTemps(int nozzleTemp, int bedTemp){
        if(nozzleTemp<=0 || bedTemp<=0 || bedTemp>nozzleTemp){
            throw new IllegalArgumentException("Invalid printer temperature limits.");
        }
        this.maxNozzleTemp=nozzleTemp;
        this.maxBedTemp=bedTemp;
        return this;
    }
    public PrinterBuilder setBedType(String bedType){
        if (bedType == null || bedType.isBlank()) {
            throw new IllegalArgumentException("Bed type cannot be empty.");
        }
        this.bedType=bedType;
        return this;
    }

    public PrinterBuilder withAMS(boolean hasAMS) {
        this.hasAMS = hasAMS;
        return this;
    }

    public PrinterBuilder withEnclosure(boolean hasEnclosure) {
        this.hasEnclosure = hasEnclosure;
        return this;
    }

    public PrinterBuilder withHardenedNozzle(boolean hasHardenedNozzle) {
        this.hasHardenedNozzle = hasHardenedNozzle;
        return this;
    }

    public Printer build(){
        return Printer.create(this);
    }

    String getModelName() {
        return modelName;}

    double getNozzleDiameter() {
        return nozzleDiameter;}

    int getMaxNozzleTemp() {
        return maxNozzleTemp;}

    int getMaxBedTemp() {
        return maxBedTemp;}

    boolean isHasAMS() {
        return hasAMS;}

    boolean isHasEnclosure() {
        return hasEnclosure;}

    boolean isHasHardenedNozzle() {
        return hasHardenedNozzle;}

    String getBedType() {
        return bedType;}
}
