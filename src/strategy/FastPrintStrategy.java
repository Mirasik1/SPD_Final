package strategy;

public class FastPrintStrategy implements PrintStrategy {
    @Override
    public String getName() {
        return "Fast (draft)";
    }

    @Override
    public double getLayerHeightMm() {
        return 0.28;
    }

    @Override
    public double getTimeMultiplier() {
        return 0.6;
    }
}
