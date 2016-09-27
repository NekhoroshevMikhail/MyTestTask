package com.company;

import DataModel.MyTask;
import DataModel.MyTaskList;
import DataProcessorFactory.AvailableDataFormats;
import DataProcessorFactory.DataProcessorCreator;
import DataProcessorFactory.IDataProcessor;
import Exceptions.*;

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

    }
}
