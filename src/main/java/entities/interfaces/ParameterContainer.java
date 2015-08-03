package entities.interfaces;

import system.impl.ParameterTypes;

/**
 * Created by Rodion on 27.05.2015.
 */
public interface ParameterContainer {

    Object getParameter(ParameterTypes key);

    void putParameter(ParameterTypes key, Object value);

}
