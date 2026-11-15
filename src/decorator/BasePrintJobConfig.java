package decorator;

public class BasePrintJobConfig implements PrintJobConfig {
    @Override
    public double getTimeMultiplier() {
        return 1.0;
    }

    @Override
    public double getMaterialMultiplier() {
        return 1.0;
    }

    @Override
    public String getDescription() {
        return "Default settings";
    }
}
