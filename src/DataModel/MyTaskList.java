package DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nekho on 27-Sep-16.
 */
public class MyTaskList {
    private List<MyTask> _taskList;

    public MyTaskList() {
        _taskList = new ArrayList<MyTask>();
    }

    public List<MyTask> get_taskList() {
        return _taskList;
    }

    public void set_taskList(List<MyTask> value) {
        _taskList = value;
    }
}
