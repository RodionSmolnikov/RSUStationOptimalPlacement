package system;

import annotations.Param;
import entities.interfaces.Model;
import entities.interfaces.ParameterContainer;
import exception.ModelException;
import system.impl.ParameterTypes;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Created by Rodion on 27.05.2015.
 */
public class ParameterRepository {

    public static void setParams (Object object, ParameterContainer container) throws Exception {
        Field [] modelFields = object.getClass().getFields();
        for(Field field: modelFields) {
            if(field.isAnnotationPresent(Param.class)) {
                ParameterTypes parameter = ((Param) field.getAnnotation(Param.class)).type();
                Object value = container.getParameter(parameter);
                try {
                    field.set(object, value);
                } catch (Exception e) {
                    throw new ModelException("Bad parameter for field " + field.getName(), e);
                }
            }
        }
    }

}
