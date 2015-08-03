package system;

import entities.interfaces.Model;
import entities.interfaces.ParameterContainer;
import entities.interfaces.ResultCollector;

/**
 * Created by Rodion on 04.04.2015.
 */
public abstract class Executor {

    private ResultCollector collector;
    private int threadCount;
    public static final int DEFAULT_THREAD_COUNT = 4;

    protected Executor(ResultCollector collector, int threadCount) {
        this.collector = collector;
        this.threadCount = threadCount;
    }

    protected Executor(ResultCollector collector) {
        this.collector = collector;
        this.threadCount = DEFAULT_THREAD_COUNT;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public ResultCollector getCollector() {
        return collector;
    }

    public void execute(ModelFactory factory) throws Exception {
        factory.setCollector(getCollector());
        long time = System.currentTimeMillis();
        getCollector().pushFlashMessage("Start execution");
        action(factory);
        getCollector().pushFlashMessage("End execution. Execution time = " + (System.currentTimeMillis() - time) + " ms");
        getCollector().printResults();
        printExecutionResult();
    }

    public abstract void action(ModelFactory model) throws Exception;

    public abstract void printExecutionResult();

}