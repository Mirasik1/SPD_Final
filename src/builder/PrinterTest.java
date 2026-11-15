package builder;

import adapter.ExternalPriceService;
import adapter.FilamentPriceAdapter;
import decorator.BasePrintJobConfig;
import decorator.FineLayerDecorator;
import decorator.InfillDecorator;
import decorator.PrintJobConfig;
import decorator.SupportsDecorator;
import factory.Filament;
import factory.FilamentFactory;
import observer.PrintObserver;
import strategy.FastPrintStrategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class PrinterTest {
    private static final class ResultObserver implements PrintObserver {
        String file;
        String status;
        double cost;
        double minutes;
        int calls;

        @Override
        public void onPrintFinished(String fileName, String status, double totalCost, double timeMinutes) {
            this.file = fileName;
            this.status = status;
            this.cost = totalCost;
            this.minutes = timeMinutes;
            calls++;
        }
    }

    private static void equal(double expected, double actual) {
        if (Math.abs(expected - actual) > 0.000001) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            Filament pla = FilamentFactory.createFilament("PLA");
            pla.setPricePerKg(18.5);
            Printer printer = new PrinterBuilder("Test printer").build();
            ResultObserver result = new ResultObserver();
            printer.addObserver(result);

            printer.performPrint("models/benchy.stl", pla, 50);
            check("SUCCESS".equals(result.status), "Default print must succeed");
            check("benchy.stl".equals(result.file), "Filename must be passed to observer");
            equal(300, result.minutes);
            equal(1.147, result.cost);
            check(output.toString(StandardCharsets.UTF_8).replace(',', '.').contains("filament: 62.0 g"),
                    "Default material consumption must be 62 grams");

            PrintJobConfig config = new FineLayerDecorator(
                    new InfillDecorator(new SupportsDecorator(new BasePrintJobConfig())));
            printer.performPrint("models/benchy.stl", pla, 50, config);
            equal(579.6, result.minutes);
            equal(96.72 / 1000 * 18.5, result.cost);
            check(output.toString(StandardCharsets.UTF_8).replace(',', '.').contains("filament: 96.7 g"),
                    "Decorators must affect material consumption");

            printer.setStrategy(new FastPrintStrategy());
            printer.performPrint("models/fast.stl", pla, 50);
            equal(180, result.minutes);

            Filament abs = FilamentFactory.createFilament("ABS");
            printer.performPrint("models/abs.stl", abs, 50);
            check("FAILED".equals(result.status), "ABS without enclosure must fail");
            equal(0, result.minutes);
            equal(0, result.cost);
            check(result.calls == 4, "Each print attempt must notify exactly once");

            try {
                printer.performPrint("models/bad.stl", pla, -1);
                throw new AssertionError("Negative volume must be rejected");
            } catch (IllegalArgumentException expected) {
                check(result.calls == 4, "Invalid input must not notify a print result");
            }

            // Port 1 is intentionally not serving the marketplace API.
            Filament fallback = FilamentFactory.createFilament("PLA",
                    new FilamentPriceAdapter(new ExternalPriceService("http://127.0.0.1:1")));
            equal(FilamentFactory.createFilament("PLA").getPricePerKg(), fallback.getPricePerKg());
        } finally {
            System.setOut(originalOut);
        }
        System.out.println("PASS: default, decorators, strategy, failure, validation, offline prices");
    }
}
