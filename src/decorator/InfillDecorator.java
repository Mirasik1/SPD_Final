package decorator;

public class InfillDecorator extends PrintJobDecorator {
    public InfillDecorator(PrintJobConfig wrapped) {
        super(wrapped);
    }

    @Override
    public double getTimeMultiplier() {
        return wrapped.getTimeMultiplier() * 1.20;
    }

    @Override
    public double getMaterialMultiplier() {
        return wrapped.getMaterialMultiplier() * 1.30;
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + Dense infill";
    }
}
