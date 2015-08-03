package entities.results.countresults;

import entities.interfaces.CountResult;
import entities.models.helpentity.Path;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rodion on 10.06.2015.
 */
public class CoverageModelCountResult implements CountResult<Path> {

    private Map<String, Path> results = new HashMap<String, Path>();

    @Override
    public Path getSolution(String key) {
        return results.get(key);
    }

    @Override
    public void addSolution(String key, Path solution) {
        results.put(key, solution);
    }

    @Override
    public Collection<Path> getSolutions() {
        return results.values();
    }

    @Override
    public Set<String> keys() {
        return results.keySet();
    }
}
