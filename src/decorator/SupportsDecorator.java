package decorator;

public class SupportsDecorator extends PrintJobDecorator {
    public SupportsDecorator(PrintJobConfig wrapped) {
        super(wrapped);
    }

    @Override
    public double getTimeMultiplier() {
        return wrapped.getTimeMultiplier() * 1.15;
    }

    @Override
    public double getMaterialMultiplier() {
        return wrapped.getMaterialMultiplier() * 1.20;
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + Supports";
    }
}
