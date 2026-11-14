package strategy;

public class HighQualityPrintStrategy implements PrintStrategy {
    @Override
    public String getName() {
        return "High quality";
    }

    @Override
    public double getLayerHeightMm() {
        return 0.12;
    }

    @Override
    public double getTimeMultiplier() {
        return 1.8;
    }
}
