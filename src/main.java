import adapter.*;
import factory.*;
import builder.*;
import observer.*;
import strategy.*;

public class main {
    public static void main(String[] args) {

        System.out.println("--- FACTORY PATTERN ---");

        System.out.println("--- ADAPTER PATTERN: EXTERNAL MARKETPLACE ---");
        FilamentPriceProvider prices = new FilamentPriceAdapter(
                new ExternalPriceService("http://127.0.0.1:8000"));

        Filament pla = FilamentFactory.createFilament("PLA", prices);
        Filament petg = FilamentFactory.createFilament("PETG", prices);
        Filament abs = FilamentFactory.createFilament("ABS", prices);
        Filament tpu = FilamentFactory.createFilament("TPU", prices);

        System.out.println(pla);
        System.out.println();

        System.out.println(petg);
        System.out.println();

        System.out.println(abs);
        System.out.println();

        System.out.println(tpu);



        System.out.println("\nCurrent PLA price: "
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
        System.out.println("\n--- STRATEGY AND OBSERVER ---");

        printer1.addObserver(new ConsoleNotificationObserver());
        printer1.addObserver(new TelegramBotObserver());
        printer1.addObserver(new SoundAlarmObserver());

        printer1.setStrategy(new HighQualityPrintStrategy());
        printer1.performPrint("models/vase.stl", abs, 120);

        printer1.setStrategy(new FastPrintStrategy());
        printer1.performPrint("models/bracket.stl", pla, 40);

        printer2.addObserver(new ConsoleNotificationObserver());
        printer2.performPrint("models/case.stl", abs, 80);
    }
}
