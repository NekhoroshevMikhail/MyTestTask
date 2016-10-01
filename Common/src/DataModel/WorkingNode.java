package DataModel;

import Exceptions.BelowZeroException;
import Exceptions.EmptyNameException;

/**
 * Created by nekho on 27-Sep-16.
 */
public class WorkingNode {

    private final String DEFAULT_NODE_NAME = "Default";
    private final int DEFAULT_NODE_PORT = 40000;

    protected String name;
    protected int port;

    //need for serialization
    public WorkingNode(){
        name = DEFAULT_NODE_NAME;
        port = DEFAULT_NODE_PORT;
    }

    public WorkingNode(String nodeName, int nodePort) throws EmptyNameException, BelowZeroException {
        setName(nodeName);
        setPort(nodePort);
    }

    public WorkingNode(WorkingNode node) {
        name = node.getName();
        port = node.getPort();
    }

    public String getName() {
        return name;
    }

    public void setName(String value)
        throws EmptyNameException
    {
        if (value == null || value.length() == 0) {
            throw new EmptyNameException("working node can not has empty name");
        }
        name = value;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int value)
            throws BelowZeroException
    {
        if (value < 0) {
            throw new BelowZeroException("port value can not be below zero");
        }
        port = value;
    }
}
