package system.impl;

import annotations.Param;
import entities.interfaces.Model;
import entities.interfaces.ParameterContainer;
import entities.interfaces.ResultCollector;
import entities.models.helpentity.Path;
import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;
import entities.models.helpentity.Vertex;
import entities.results.collectors.DefaultResultCollector;
import entities.results.handlers.CoverageModelResultHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import system.ModelFactory;
import system.ParameterRepository;
import utils.CoverageModelHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rodion on 10.06.2015.
 */
public abstract class AbstractCoverageModelFactory implements ModelFactory {

    @Param(type = ParameterTypes.STATION_TYPES)
    public Type[] types;

    @Param(type = ParameterTypes.POSIBLE_PLACEMENT)
    public Placement[] placements;

    @Param(type = ParameterTypes.BUDGET)
    public double budget;

    private HashMap<Placement, List<Vertex>> graph;

    private int threadCount;

    private ParameterContainer container;

    private List<Path> initialPaths;

    private ResultCollector collector;

    public AbstractCoverageModelFactory(ParameterContainer container, int threadCount) {
        this.container = container;
        this.initialPaths = new ArrayList<Path>();
        this.collector = null;
        this.threadCount = threadCount;

        Arrays.sort((Placement [])container.getParameter(ParameterTypes.POSIBLE_PLACEMENT), CoverageModelHelper.getPlacementComparator());
        Arrays.sort((Type [])container.getParameter(ParameterTypes.STATION_TYPES), CoverageModelHelper.getTypeComparator());
        try {
            ParameterRepository.setParams(this, container);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        this.graph = CoverageModelHelper.createGraph(placements, types);

        Path startPath = new Path();
        startPath.addVertex(CoverageModelHelper.findVertexInGraph(placements[0], types[0], graph));
        initialPaths.add(startPath);

        while (initialPaths.size() < this.threadCount) {
            List<Path> nextLevel = new ArrayList<>();
            for (Path current: initialPaths) {
                for (Vertex child: current.getPath().getLast().getChilds()) {
                    if (current.getCost() + child.getType().getCost() <= budget) {
                        Path temp = current.clone();
                        temp.addVertex(child);
                        nextLevel.add(temp);
                    }
                }
            }
            initialPaths = nextLevel;
        }
    }

    public AbstractCoverageModelFactory(ParameterContainer container) {
        this(container, 1);
    }


    @Override
    public ResultCollector getCollector() {
        if (collector == null) {
            return new DefaultResultCollector(new CoverageModelResultHandler());
        }
        return collector;
    }

    public void setCollector(ResultCollector collector) {
        this.collector = collector;
    }

    @Override
    public void putParam(ParameterTypes key, Object value) {
        this.container.putParameter(key, value);
    }

    @Override
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public abstract Model createModelInstance(int threadNumber) throws Exception;

    public Type[] getTypes() {
        return types;
    }

    public Placement[] getPlacements() {
        return placements;
    }

    public double getBudget() {
        return budget;
    }

    public HashMap<Placement, List<Vertex>> getGraph() {
        return graph;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public ParameterContainer getContainer() {
        return container;
    }

    public List<Path> getInitialPaths() {
        return initialPaths;
    }
}
