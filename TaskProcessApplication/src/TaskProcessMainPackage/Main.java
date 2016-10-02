package TaskProcessMainPackage;

/**
 * Created by nekho on 29-Sep-16.
 */
public class Main {
    public static void main(String[] args) {
        TaskManager taskProcessor = new TaskManager(Integer.decode(args[0]));
        Thread taskProcessorThread = new Thread(taskProcessor);
        taskProcessorThread.start();
    }
}
