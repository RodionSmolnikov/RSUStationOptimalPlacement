package system;

import entities.interfaces.Model;
import entities.interfaces.ParameterContainer;
import entities.interfaces.ResultCollector;
import system.impl.MultiThreadExecutor;
import system.impl.SingleThreadExecutor;

/**
 * Created by Rodion on 06.05.2015.
 */
public class ExecutorFactory {

    private ExecutorFactory() {
    }

    private static ExecutorFactory INSTANCE = null;

    public static ExecutorFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExecutorFactory();
        }
        return INSTANCE;
    }

    public Executor getExecutor (ResultCollector collector, boolean inParallel, Object ... params) {
        if (inParallel) {
            return new MultiThreadExecutor(collector, (Integer) params[0]);
        }
        return new SingleThreadExecutor(collector);
    }
}
