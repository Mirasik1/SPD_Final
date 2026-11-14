package observer;

public class ConsoleNotificationObserver implements PrintObserver {
    @Override
    public void onPrintFinished(String fileName, String status, double totalCost, double timeMinutes) {
        System.out.println("[Console] Print of \"" + fileName + "\" finished. Status: " + status);
    }
}
