package DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nekho on 27-Sep-16.
 */
public class WorkingNodeList {
    private List<WorkingNode> _workingNodes;

    public WorkingNodeList() {
        _workingNodes = new ArrayList<WorkingNode>();
    }

    public List<WorkingNode> getWorkingNodes() {
        return _workingNodes;
    }

    public void setWorkingNodes(List<WorkingNode> value) {
        _workingNodes = value;
    }

    public void AddNode(WorkingNode node) {
        _workingNodes.add(node);
    }
}
