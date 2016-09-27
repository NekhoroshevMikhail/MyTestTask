package DataProcessorFactory;

import Exceptions.DataLoaderNotDefinedException;
import XmlDataProcessor.XmlDataProcessor;

/**
 * Created by nekho on 27-Sep-16.
 */
public class DataProcessorCreator {
    public static IDataProcessor CreateDataProcessor(AvailableDataFormats processorType)
        throws DataLoaderNotDefinedException
    {
        switch (processorType)
        {
            case Xml:
                return new XmlDataProcessor();
            default:
                throw new DataLoaderNotDefinedException("you must realize creation of data processor");
        }
    }
}
