package entities.results.collectors;

import entities.interfaces.CountResult;
import entities.interfaces.ResultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodion on 08.06.2015.
 */
public class DefaultResultCollector extends AbstractResultCollector {

    List<CountResult> list = new ArrayList<>();
    ResultHandler handler;

    public DefaultResultCollector(ResultHandler handler) {
        this.handler = handler;
    }

    @Override
    public void pushFlashMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void pushFlashMessage(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void addCountResult(CountResult result) {
        list.add(result);
    }

    @Override
    public List<CountResult> getCountResults() {
        return list;
    }

    @Override
    public ResultHandler getResultHandler() {
        return handler;
    }
}
