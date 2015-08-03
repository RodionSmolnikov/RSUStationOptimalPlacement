package system.impl;

import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;

/**
 * Created by Rodion on 27.05.2015.
 */
public enum ParameterTypes {

    STATION_TYPES(Type[].class),
    POSIBLE_PLACEMENT(Placement[].class),
    BUDGET(Double.class),
    PATH_BROAD(Integer.class),
    IMPROVABLE_SET_BROAD(Integer.class),
    EPSILON(Double.class);

    Class paramType;

    ParameterTypes(Class clazz) {
        this.paramType = clazz;
    }

    public Class getParamType() {
        return paramType;
    }
}
