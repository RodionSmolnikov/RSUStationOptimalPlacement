package entities.results.handlers;

import entities.interfaces.CountResult;
import entities.interfaces.ResultHandler;
import entities.models.helpentity.Path;
import entities.results.countresults.CoverageModelCountResult;

import java.util.Collection;
import java.util.List;

/**
 * Created by Rodion on 10.06.2015.
 */
public class CoverageModelResultHandler implements ResultHandler<CoverageModelCountResult> {
    @Override
    public String handle(Collection<CoverageModelCountResult> results) {
        Path maxPath = null;
        String key = "";
        for (CoverageModelCountResult result: results) {
            for (String pathKey: result.keys()) {
                if (maxPath == null) {
                    maxPath = result.getSolution(pathKey);
                } else {
                    if (maxPath.getAbsoluteCoverage() < result.getSolution(pathKey).getAbsoluteCoverage()) {
                        maxPath =result.getSolution(pathKey);
                        key = pathKey;
                    }
                }
            }
        }

        return key + " " + maxPath.toString();

    }
}
