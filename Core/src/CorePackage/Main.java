package CorePackage;

import DAF.*;
import DataModel.*;
import DataProcessor.IDataProcessor;
import Exceptions.*;

/**
 * Created by nekho on 28-Sep-16.
 */
public class Main {
    public static void main(String[] args) {
        // write your code here
        try {
            IDataProcessor result = DataProcessorCreator.CreateDataProcessor(AvailableDataFormats.Xml);

            MyTask task = new MyTask();
            try {
                task.setName("MyTAskName");
                task.setMaximumTaskTime((short) 20 );
                task.setMinimumTaskTime((short) 10);
                task.setPriority((short) 1);

            } catch (EmptyNameException ex) {
                System.exit(0);
            }
            catch (BelowZeroException e) {
                System.exit(0);
            }

            MyTaskList tasksList = new MyTaskList();
            tasksList.AddTask(task);

            result.SaveAllTasks(tasksList);

        } catch (DataLoaderNotDefinedException ex) {


        }
        System.exit(0);

    }
}
