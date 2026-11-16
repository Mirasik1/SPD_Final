package factory;

public class PLAFactory extends FilamentFactory {
    @Override
    public Filament createFilament() {
        return new PLAFilament();
    }
}
