package entities.results.collectors;

import entities.interfaces.CountResult;
import entities.interfaces.ResultHandler;
import view.stationplacement.ConsoleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodion on 06.05.2015.
 */
public class ConsoleResultCollector extends AbstractResultCollector {

    List<CountResult> results;
    ConsoleView console;
    ResultHandler handler;

    public ConsoleResultCollector(ConsoleView view, ResultHandler handler) {
        this.results = new ArrayList<CountResult>();
        this.console = view;
        this.handler = handler;
    }

    @Override
    public void pushFlashMessage(String message) {
        console.append(message);
    }

    @Override
    public void pushFlashMessage(Exception e) {
        console.append(e);
    }

    @Override
    public void addCountResult(CountResult result) {
        results.add(result);
    }

    @Override
    public List<CountResult> getCountResults() {
        return results;
    }

    @Override
    public ResultHandler getResultHandler() {
        return handler;
    }

}
