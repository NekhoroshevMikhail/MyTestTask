package DataModel;

import java.util.Comparator;

/**
 * Created by nekho on 02-Oct-16.
 */
public class TaskPriorityComparator implements Comparator<MyTask> {
    @Override
    public int compare(MyTask firstTask, MyTask secondTask) {

        if (firstTask.getPriority() > secondTask.getPriority()){
            return 1;
        }

        if (firstTask.getPriority() == secondTask.getPriority()){
            return 0;
        }

        return -1;
    }
}
