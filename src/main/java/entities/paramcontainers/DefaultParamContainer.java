package entities.paramcontainers;

import entities.interfaces.ParameterContainer;
import system.impl.ParameterTypes;

import java.util.HashMap;

/**
 * Created by Rodion on 02.06.2015.
 */
public class DefaultParamContainer implements ParameterContainer {

    private HashMap<ParameterTypes, Object> map = new HashMap<ParameterTypes, Object>();

    @Override
    public Object getParameter(ParameterTypes key) {
        return this.map.get(key);
    }

    @Override
    public void putParameter(ParameterTypes key, Object value) {
        map.put(key, value);
    }
}
