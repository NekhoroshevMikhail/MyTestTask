package TaskProcessMainPackage;

/**
 * Created by nekho on 29-Sep-16.
 */
public class Main {
    public static void main(String[] args) {
        TaskProcessor taskProcessor = new TaskProcessor(args[0], Integer.decode(args[1]));
        Thread taskProcessorThread = new Thread(taskProcessor);
        taskProcessorThread.start();
    }
}
