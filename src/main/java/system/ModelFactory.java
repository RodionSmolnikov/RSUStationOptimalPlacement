package system;

import entities.interfaces.Model;
import entities.interfaces.ResultCollector;
import system.impl.ParameterTypes;

/**
 * Created by Rodion on 10.06.2015.
 */
public interface ModelFactory {

    public void putParam (ParameterTypes key, Object value);

    public void setThreadCount(int threadCount);

    public Model createModelInstance(int threadNumber) throws Exception;

    public ResultCollector getCollector();

    public void setCollector(ResultCollector collector);
}
