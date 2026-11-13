package factory;

public class FilamentFactory {
    public static Filament createFilament(String type){
        if (type==null || type.isBlank()){
            throw new IllegalArgumentException("Filament type cannot be empty.");
        }
        return switch (type.trim().toUpperCase()){
            case "PLA" -> new PLAFilament();
            case "PETG" -> new PETGFilament();
            case "ABS" -> new ABSFilament();
            case "TPU" -> new TPUFilament();

            default -> throw new IllegalArgumentException("Unknown filament type: " + type);
        };
    }
}
