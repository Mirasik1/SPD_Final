package factory;

public interface Filament {
    String getName();
    int getNozzleTemp();
    int getBedTemp();
    int getChamberTemp();
    double getDensity();
    double getPricePerKg();
    boolean isRequiresEnclosure();
    void setPricePerKg(double pricePerKg);
    String getPrintSpecs();
}
