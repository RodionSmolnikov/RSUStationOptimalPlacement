package system.impl;

import entities.interfaces.Model;
import entities.interfaces.ParameterContainer;
import entities.interfaces.ResultCollector;
import system.Executor;
import system.ModelFactory;

/**
 * Created by Rodion on 06.05.2015.
 */
public class SingleThreadExecutor extends Executor {

    public SingleThreadExecutor(ResultCollector collector) {
        super(collector);
    }

    @Override
    public void action(ModelFactory factory) {
        try {
            Thread execution = new Thread(factory.createModelInstance(1));
            execution.start();
            execution.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printExecutionResult() {
        this.getCollector().printResults();
    }

}
