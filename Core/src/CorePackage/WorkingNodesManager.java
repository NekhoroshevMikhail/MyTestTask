package CorePackage;

import DataModel.WorkingNode;
import DataModel.WorkingNodeList;

import java.io.IOException;

/**
 * Created by nekho on 29-Sep-16.
 */
public class WorkingNodesManager {

    WorkingNodeList _nodesToRun;

    public WorkingNodesManager(WorkingNodeList nodesToRun) {
        _nodesToRun = nodesToRun;
    }

    public void RunAllNodes() {

        for (WorkingNode node: _nodesToRun) {
            RunNode(node.getName(), Integer.toString(node.getPort()));
        }
    }

    private void RunNode(String name, String port) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "TaskProcessApplication.jar", name, port);
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
