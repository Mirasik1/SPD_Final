package strategy;

public class StandardPrintStrategy implements PrintStrategy {
    @Override
    public String getName() {
        return "Standard";
    }

    @Override
    public double getLayerHeightMm() {
        return 0.20;
    }

    @Override
    public double getTimeMultiplier() {
        return 1.0;
    }
}
