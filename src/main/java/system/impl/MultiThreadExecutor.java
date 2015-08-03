package system.impl;

import entities.interfaces.Model;
import entities.interfaces.ParameterContainer;
import entities.interfaces.ResultCollector;
import system.Executor;
import system.ModelFactory;

/**
 * Created by Rodion on 27.05.2015.
 */
public class MultiThreadExecutor extends Executor {
    public MultiThreadExecutor(ResultCollector collector, int threadCount) {
        super(collector, threadCount);
    }

    public MultiThreadExecutor(ResultCollector collector) {
        super(collector);
    }

    @Override
    public void action(ModelFactory factory) throws Exception {
        Thread [] pool = new Thread[getThreadCount()];
        for (int i = 0; i < getThreadCount(); i++){
            Model m = factory.createModelInstance(i);
            pool[i] = new Thread(m);
            pool[i].start();
        }
        for (int i = 0; i < pool.length; i++){
            pool[i].join();
        }
    }

    @Override
    public void printExecutionResult() {

    }

}
