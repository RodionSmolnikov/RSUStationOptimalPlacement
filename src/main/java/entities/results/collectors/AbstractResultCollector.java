package entities.results.collectors;

import entities.interfaces.ResultCollector;

/**
 * Created by Rodion on 08.06.2015.
 */
public abstract class AbstractResultCollector implements ResultCollector {
    @Override
    public void printResults() {
        pushFlashMessage(getResultHandler().handle(getCountResults()));
    }
}
