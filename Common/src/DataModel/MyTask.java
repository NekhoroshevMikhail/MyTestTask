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
    private final int POSSIBLE_MINIMUM_TASK_TIME = 5;
    private final int POSSIBLE_MAXIMUM_TASK_TIME = 25;
    private final int DEFAULT_PRIORITY = 0;


    private String _name;
    private int _priority;
    private int _minimumTaskTime;
    private int _maximumTaskTime;

    public MyTask() {
        _name = DEFAULT_TASK_NAME;
        _priority = DEFAULT_PRIORITY;
        _minimumTaskTime = POSSIBLE_MINIMUM_TASK_TIME;
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

    public int getPriority() {
        return _priority;
    }

    public void setPriority(int value)
            throws BelowZeroException
    {
        if (value < 0) {
            throw new BelowZeroException("Incorrect priority value");
        }
        _priority = value;
    }

    public int getMinimumTaskTime()
    {
        return _minimumTaskTime;
    }

    public void setMinimumTaskTime(int value)
            throws OutOfPossibleRangeException, IncorrectRangeException
    {
        if (IsValueInAvailableRange(value)) {
            if (value > _maximumTaskTime) {
                throw new IncorrectRangeException("minimum value of task time can not be more, then maximum value");
            }
            _minimumTaskTime = value;
        }
    }

    public int getMaximumTaskTime()
    {
        return _maximumTaskTime;
    }

    public void setMaximumTaskTime(int value)
        throws OutOfPossibleRangeException, IncorrectRangeException
    {
        if (IsValueInAvailableRange(value)) {
            if (value < _minimumTaskTime) {
                throw new IncorrectRangeException("maximum value of task time can not be less, then minimum value");
            }

            _maximumTaskTime = value;
        }
    }

    private Boolean IsValueInAvailableRange(int value)
            throws OutOfPossibleRangeException
    {
        if (value > POSSIBLE_MAXIMUM_TASK_TIME || value < POSSIBLE_MINIMUM_TASK_TIME) {
            throw new OutOfPossibleRangeException("value does not match range. Minimum value is " + POSSIBLE_MINIMUM_TASK_TIME +
                    ", maximum value is - " + POSSIBLE_MAXIMUM_TASK_TIME + ", current value == " + value);
        }
        return true;
    }
}
