package entities.interfaces;

import java.util.Collection;
import java.util.List;

/**
 * Created by Rodion on 08.06.2015.
 */
public interface ResultHandler<T extends CountResult> {
    String handle(Collection<T> results);
}
