import factory.*;
import builder.*;

public class main {
    public static void main(String[] args) {

        System.out.println("--- FACTORY PATTERN ---");

        Filament pla = FilamentFactory.createFilament("PLA");
        Filament petg = FilamentFactory.createFilament("PETG");
        Filament abs = FilamentFactory.createFilament("ABS");
        Filament tpu = FilamentFactory.createFilament("TPU");

        System.out.println(pla);
        System.out.println();

        System.out.println(petg);
        System.out.println();

        System.out.println(abs);
        System.out.println();

        System.out.println(tpu);



        pla.setPricePerKg(17.5);

        System.out.println("\nUpdated PLA price: "
                + pla.getPricePerKg());



        System.out.println("\n--- BUILDER PATTERN ---");

        Printer printer1 = new PrinterBuilder("Bambu Lab X1 Carbon")
                .setNozzleDiameter(0.4)
                .setMaxTemps(300, 110)
                .withAMS(true)
                .withEnclosure(true)
                .withHardenedNozzle(true)
                .setBedType("Textured PEI")
                .build();

        Printer printer2 = new PrinterBuilder("Ender 3 V2")
                .build();

        System.out.println(printer1);
        System.out.println(printer2);



        System.out.println("\n--- PRINTER AND FILAMENT ---");

        System.out.println("Printer: " + printer1.getModelName());
        System.out.println("Filament: " + abs.getName());

        System.out.println("Required nozzle temperature: "
                + abs.getNozzleTemp());

        System.out.println("Maximum nozzle temperature: "
                + printer1.getMaxNozzleTemp());

        System.out.println("Enclosure required: "
                + abs.isRequiresEnclosure());

        System.out.println("Printer has enclosure: "
                + printer1.isHasEnclosure());
    }
}
