package decorator;

public class FineLayerDecorator extends PrintJobDecorator {
    public FineLayerDecorator(PrintJobConfig wrapped) {
        super(wrapped);
    }

    @Override
    public double getTimeMultiplier() {
        return wrapped.getTimeMultiplier() * 1.40;
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + Fine layers";
    }
}
