package entities.interfaces;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Rodion on 06.05.2015.
 */
public interface CountResult<T> {

    String SOLUTION_KEY = "solution";

    public T getSolution(String key);

    public void addSolution(String key, T solution);

    public Collection<T> getSolutions();

    public  Set<String> keys();

}
