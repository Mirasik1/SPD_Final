package decorator;

import java.util.Objects;

public abstract class PrintJobDecorator implements PrintJobConfig {
    protected final PrintJobConfig wrapped;

    protected PrintJobDecorator(PrintJobConfig wrapped) {
        this.wrapped = Objects.requireNonNull(wrapped, "Print settings cannot be null");
    }

    @Override
    public double getTimeMultiplier() {
        return wrapped.getTimeMultiplier();
    }

    @Override
    public double getMaterialMultiplier() {
        return wrapped.getMaterialMultiplier();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }
}
