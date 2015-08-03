package system.impl;

import entities.interfaces.Model;
import entities.interfaces.ParameterContainer;
import entities.interfaces.ResultCollector;
import entities.models.CoverageParallelBruteForceModel;
import entities.models.helpentity.Path;
import system.ParameterRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodion on 11.06.2015.
 */
public class ParallelBruteForceModelFactory extends AbstractCoverageModelFactory {

    public ParallelBruteForceModelFactory(ParameterContainer container, int threadCount) {
        super(container, threadCount);
    }

    @Override
    public Model createModelInstance(int threadNumber) throws Exception {
        List<Path> initialPath = new ArrayList<>();
        for (int i = 0; i < getInitialPaths().size(); i++) {
            if (i % getThreadCount() == threadNumber) {
                initialPath.add(getInitialPaths().get(i));
            }
        }
        CoverageParallelBruteForceModel model = new CoverageParallelBruteForceModel(threadNumber, initialPath);
        ParameterRepository.setParams(model, this.getContainer());
        model.init();
        model.setCollector(getCollector());
        return model;
    }
}
