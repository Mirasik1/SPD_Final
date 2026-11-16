package factory;

public class PETGFactory extends FilamentFactory {
    @Override
    public Filament createFilament() {
        return new PETGFilament();
    }
}
