package entities.interfaces;

import java.util.List;

/**
 * Created by Rodion on 06.05.2015.
 */
public interface ResultCollector {

    public void pushFlashMessage(String message);

    public void pushFlashMessage(Exception e);

    public void addCountResult (CountResult result);

    public void printResults();

    public List<CountResult> getCountResults ();

    public ResultHandler getResultHandler();

}
