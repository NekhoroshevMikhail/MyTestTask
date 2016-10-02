package DAF;

import DataProcessor.IDataAccessor;
import Exceptions.DataAccessorNotDefinedException;
import XmlDataAccess.XmlDataProcessor;

/**
 * Created by nekho on 27-Sep-16.
 */
public class DataAccessorCreator {
    public static IDataAccessor CreateDataAccessor(AvailableDataFormats processorType)
        throws DataAccessorNotDefinedException
    {
        switch (processorType)
        {
            case Xml:
                return new XmlDataProcessor();
            default:
                throw new DataAccessorNotDefinedException("you must realize creation of data processor");
        }
    }
}
