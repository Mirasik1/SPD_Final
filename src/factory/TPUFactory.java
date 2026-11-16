package factory;

public class TPUFactory extends FilamentFactory {
    @Override
    public Filament createFilament() {
        return new TPUFilament();
    }
}
