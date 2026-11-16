package factory;

public class ABSFactory extends FilamentFactory {
    @Override
    public Filament createFilament() {
        return new ABSFilament();
    }
}
