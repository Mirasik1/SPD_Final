import adapter.*;
import decorator.*;
import factory.*;
import builder.*;
import observer.*;
import strategy.*;

public class main {
    public static void main(String[] args) {

        System.out.println("--- FACTORY METHOD PATTERN ---");

        System.out.println("--- ADAPTER PATTERN: EXTERNAL MARKETPLACE ---");
        FilamentPriceProvider prices = new FilamentPriceAdapter(
                new ExternalPriceService("http://127.0.0.1:8000"));

        Filament pla = new PLAFactory().createFilament(prices);
        Filament petg = new PETGFactory().createFilament(prices);
        Filament abs = new ABSFactory().createFilament(prices);
        Filament tpu = new TPUFactory().createFilament(prices);

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

        System.out.println("\n--- DECORATOR PATTERN: CUSTOM PRINT SETTINGS ---");
        PrintJobConfig customSettings = new FineLayerDecorator(
                new InfillDecorator(
                        new SupportsDecorator(new BasePrintJobConfig())));
        printer1.setStrategy(new StandardPrintStrategy());
        printer1.performPrint("models/benchy.stl", pla, 50, customSettings);

        System.out.println("\n--- PRINT CALCULATION CHECKS ---");
        PrintCalculator calculator = new PrintCalculator();
        PrintMetrics base = calculator.calculate(50, pla, new StandardPrintStrategy(), new BasePrintJobConfig());
        PrintMetrics decorated = calculator.calculate(50, pla, new StandardPrintStrategy(), customSettings);
        double expectedBaseWeight = 50 * pla.getDensity();
        double expectedDecoratedWeight = expectedBaseWeight * 1.20 * 1.30;
        verify("Standard time", 300, base.timeMinutes());
        verify("Standard material", expectedBaseWeight, base.weightGrams());
        verify("Standard cost", expectedBaseWeight / 1000 * pla.getPricePerKg(), base.cost());
        verify("Decorated time", 579.6, decorated.timeMinutes());
        verify("Decorated material", expectedDecoratedWeight, decorated.weightGrams());
        verify("Decorated cost", expectedDecoratedWeight / 1000 * pla.getPricePerKg(), decorated.cost());

        printer2.addObserver(new ConsoleNotificationObserver());
        printer2.performPrint("models/case.stl", abs, 80);
    }

    private static void verify(String label, double expected, double actual) {
        if (Math.abs(expected - actual) > 0.000001) {
            throw new IllegalStateException(label + ": expected " + expected + ", got " + actual);
        }
        System.out.println("PASS: " + label);
    }
}
