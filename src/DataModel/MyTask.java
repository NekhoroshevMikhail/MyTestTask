package DataModel;

import Exceptions.BelowZeroException;
import Exceptions.EmptyNameException;
import Exceptions.IncorrectRangeException;
import Exceptions.OutOfPossibleRangeException;

/**
 * Created by nekho on 27-Sep-16.
 */
public class MyTask {

    private final String DEFAULT_TASK_NAME = "DefaultTaskName";
    private final short POSSIBLE_MINIMUM_TASK_TIME = 5;
    private final short POSSIBLE_MAXIMUM_TASK_TIME = 25;
    private final short DEFAULT_PRIORITY = 0;

    private String _name;
    private short _priority;
    private short _minimummTaskTime;
    private short _maximumTaskTime;

    public MyTask() {
        _name = DEFAULT_TASK_NAME;
        _priority = DEFAULT_PRIORITY;
        _minimummTaskTime = POSSIBLE_MINIMUM_TASK_TIME;
        _maximumTaskTime = POSSIBLE_MAXIMUM_TASK_TIME;
    }

    public String getName() {
        return _name;
    }

    public void setName(String value)
        throws EmptyNameException
    {
        if (value == null || value.length() == 0) {
            throw new EmptyNameException("task must has not empty name!");
        }
        _name = value;
    }

    public short getPriority() {
        return _priority;
    }

    public void setPriority(short value)
            throws BelowZeroException
    {
        if (value < 0) {
            throw new BelowZeroException("Incorrect priority value");
        }
        _priority = value;
    }

    public short getMinimummTaskTime()
    {
        return _minimummTaskTime;
    }

    public void setMinimummTaskTime(short value)
            throws OutOfPossibleRangeException, IncorrectRangeException
    {
        if (IsValueInAvailableRange(value)) {
            if (value > _maximumTaskTime) {
                throw new IncorrectRangeException("minimum value of task time can not be more, then maximum value");
            }
            _minimummTaskTime = value;
        }
    }

    public short getMaximumTaskTime() {
        return _maximumTaskTime;
    }

    public void setMaximumTaskTime(short value)
        throws OutOfPossibleRangeException, IncorrectRangeException
    {
        if (IsValueInAvailableRange(value)) {
            if (value < _minimummTaskTime) {
                throw new IncorrectRangeException("maximum value of task time can not be less, then minimum value");
            }

            _maximumTaskTime = value;
        }
    }

    private Boolean IsValueInAvailableRange(short value)
            throws OutOfPossibleRangeException
    {
        if (value > POSSIBLE_MAXIMUM_TASK_TIME || value < POSSIBLE_MINIMUM_TASK_TIME) {
            throw new OutOfPossibleRangeException("value does not match range. Minimum value is " + POSSIBLE_MINIMUM_TASK_TIME +
                    ", maximum value is - " + POSSIBLE_MAXIMUM_TASK_TIME + ", current value == " + value);
        }
        return true;
    }
}
