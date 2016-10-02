package XmlDataAccess;

import DataProcessor.IDataAccessor;
import DataModel.MyTaskList;
import DataModel.WorkingNodeList;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/**
 * Created by nekho on 27-Sep-16.
 */
public class XmlDataProcessor implements IDataAccessor {

    private final String WORKING_NODES_FILE_NAME = "WorkingNodes.xml";
    private final String TASKS_LIST_FILE_NAME = "Tasks.xml";

    public MyTaskList GetAllTasks() {
        return GetAllTasks(TASKS_LIST_FILE_NAME);
    }
    @Override
    public MyTaskList GetAllTasks(String filePath) {
        try {
            MyTaskList result;
            FileInputStream fileStream = new FileInputStream(filePath);
            BufferedInputStream bufferedStream = new BufferedInputStream(fileStream);
            XMLDecoder decoder = new XMLDecoder(bufferedStream);
            result = (MyTaskList) decoder.readObject();
            return result;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void SaveAllTasks(MyTaskList listOfTasks) {
        try{
            FileOutputStream fileStream = new FileOutputStream(TASKS_LIST_FILE_NAME);
            BufferedOutputStream bufferedStream = new BufferedOutputStream(fileStream);
            XMLEncoder xmlEncoder = new XMLEncoder(bufferedStream);
            xmlEncoder.writeObject(listOfTasks);
            xmlEncoder.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public WorkingNodeList GetAllWorkingNodes() {
        return GetAllWorkingNodes(WORKING_NODES_FILE_NAME);
    }

    @Override
    public WorkingNodeList GetAllWorkingNodes(String filePath) {
        try {
            WorkingNodeList result;
            FileInputStream fileStream = new FileInputStream(filePath);
            BufferedInputStream bufferedStream = new BufferedInputStream(fileStream);
            XMLDecoder decoder = new XMLDecoder(bufferedStream);
            result = (WorkingNodeList) decoder.readObject();
            return result;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void SaveAllWorkingNodes(WorkingNodeList listOfWorkingNodes) {
        try{
            FileOutputStream fileStream = new FileOutputStream(WORKING_NODES_FILE_NAME);
            BufferedOutputStream bufferedStream = new BufferedOutputStream(fileStream);
            XMLEncoder xmlEncoder = new XMLEncoder(bufferedStream);
            xmlEncoder.writeObject(listOfWorkingNodes);
            xmlEncoder.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
