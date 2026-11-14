package observer;

public class TelegramBotObserver implements PrintObserver {
    @Override
    public void onPrintFinished(String fileName, String status, double totalCost, double timeMinutes) {
        System.out.println("[Telegram] Sending message to bot...");
        System.out.printf("[Telegram] File: %s | Status: %s | Time: %.1f min | Cost: %.2f%n",
                fileName, status, timeMinutes, totalCost);
    }
}
