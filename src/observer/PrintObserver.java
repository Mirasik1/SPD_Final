package observer;

public interface PrintObserver {
    void onPrintFinished(String fileName, String status, double totalCost, double timeMinutes);
}
