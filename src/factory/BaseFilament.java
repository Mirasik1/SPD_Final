package factory;

public abstract class BaseFilament implements Filament {
    private final String name;
    private final int nozzleTemp;
    private final int bedTemp;
    private final int chamberTemp;
    private final double density;
    private double pricePerKg;
    private final boolean requiresEnclosure;

    public BaseFilament(String name, int nozzleTemp, int bedTemp,
                        int chamberTemp, double density,
                        double pricePerKg, boolean requiresEnclosure){
        this.name = name;
        this.nozzleTemp = nozzleTemp;
        this.bedTemp = bedTemp;
        this.chamberTemp = chamberTemp;
        this.density = density;
        this.pricePerKg = pricePerKg;
        this.requiresEnclosure = requiresEnclosure;
    }
    @Override
    public String getName(){
        return name;
    }

    @Override
    public int getNozzleTemp() {
        return nozzleTemp;
    }

    @Override
    public int getBedTemp() {
        return bedTemp;
    }

    @Override
    public int getChamberTemp() {
        return chamberTemp;
    }

    @Override
    public double getDensity() {
        return density;
    }

    @Override
    public double getPricePerKg() {
        return pricePerKg;
    }

    @Override
    public boolean isRequiresEnclosure() {
        return requiresEnclosure;
    }

    @Override
    public void setPricePerKg(double pricePerKg){
        if (pricePerKg<=0 || !Double.isFinite(pricePerKg)){
            throw new IllegalArgumentException("Price must be a positive finite number.");
        }
        this.pricePerKg = pricePerKg;
    }

    @Override
    public String getPrintSpecs() {
        return "Nozzle: " + nozzleTemp + "°C"
                + ", Bed: " + bedTemp + "°C"
                + ", Chamber: " + chamberTemp + "°C";
    }

    @Override
    public String toString() {
        return "Filament: " + name
                + "\n" + getPrintSpecs()
                + "\nDensity: " + density + " g/cm3"
                + "\nPrice: " + pricePerKg + " per kg"
                + "\nRequires enclosure: " + requiresEnclosure;
    }
}
