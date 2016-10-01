package DataModel;

import Exceptions.BelowZeroException;
import Exceptions.EmptyNameException;

/**
 * Created by nekho on 27-Sep-16.
 */
public class WorkingNode {
    protected String name;
    protected int port;

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

    public int getPort() {
        return port;
    }

    public void setPort(short value)
            throws BelowZeroException
    {
        if (value < 0) {
            throw new BelowZeroException("port value can not be below zero");
        }
        port = value;
    }
}
