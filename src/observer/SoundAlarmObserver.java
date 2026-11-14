package observer;

public class SoundAlarmObserver implements PrintObserver {
    @Override
    public void onPrintFinished(String fileName, String status, double totalCost, double timeMinutes) {
        System.out.println("[Printer] BEEP BEEP BEEP - " + fileName + " is done!");
    }
}
