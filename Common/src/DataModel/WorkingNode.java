package DataModel;

import Exceptions.BelowZeroException;
import Exceptions.EmptyNameException;

/**
 * Created by nekho on 27-Sep-16.
 */
public class WorkingNode {
    private String _name;
    private short _port;

    public String getName() {
        return _name;
    }

    public void setName(String value)
        throws EmptyNameException
    {
        if (value == null || value.length() == 0) {
            throw new EmptyNameException("working node can not has empty name");
        }
        _name = value;
    }

    public short getPort() {
        return _port;
    }

    public void setPort(short value)
            throws BelowZeroException
    {
        if (value < 0) {
            throw new BelowZeroException("port value can not be below zero");
        }
        _port = value;
    }
}
