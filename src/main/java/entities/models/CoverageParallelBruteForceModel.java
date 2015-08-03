package entities.models;

import annotations.Param;
import entities.interfaces.CountResult;
import entities.interfaces.Model;
import entities.models.helpentity.Path;
import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;
import entities.models.helpentity.Vertex;
import entities.results.countresults.CoverageModelCountResult;
import exception.ModelException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import system.impl.ParameterTypes;
import utils.CoverageModelHelper;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rodion on 08.06.2015.
 */
public class CoverageParallelBruteForceModel extends Model {

    @Param(type = ParameterTypes.STATION_TYPES)
    public Type[] types;

    @Param(type = ParameterTypes.POSIBLE_PLACEMENT)
    public Placement[] placements;

    @Param(type = ParameterTypes.BUDGET)
    public Double budget;

    private List<Path> initialPaths;

    private int threadNumber;

    private Vertex endVertex;

    private Path bestPath;

    public CoverageParallelBruteForceModel(int threadNumber, List<Path> initialPaths) {
        this.threadNumber = threadNumber;
        this.initialPaths = initialPaths;
    }

    public List<Path> getInitialPath() {
        return initialPaths;
    }

    public void setInitialPath(List<Path> initialPaths) {
        this.initialPaths = initialPaths;
    }

    @Override
    public int getThreadNumber() {
        return threadNumber;
    }

    @Override
    public void init() {
        endVertex = new Vertex(placements[placements.length-1], types[0]);
    }

    private void recursiveStep (Path current) {
        for (int i = 1; i< types.length; i++){
            if (current.getCost() + types[i].getCost() <= budget) {
                Placement last = current.getPath().getLast().getPlacement();
                Arrays.binarySearch(placements, last , CoverageModelHelper.getPlacementComparator());
                Placement [] inside = CoverageModelHelper.getPlacementsBetween(last.getCoordinate(), types[i].getConnectionRadius() + last.getCoordinate(), placements);
                for (Placement newPlace: inside) {
                    Vertex newVert = new Vertex(newPlace, types[i]);
                    current.addVertex(newVert);
                    if (CoverageModelHelper.isVertexConnected(newVert, endVertex)){
                        double coverage = CoverageModelHelper.getCoverage(current, placements[placements.length-1].getCoordinate());
                        if (bestPath.getAbsoluteCoverage() < coverage) {
                            bestPath = current.clone();
                            bestPath.setAbsoluteCoverage(coverage);
                        }
                    }
                    recursiveStep(current);
                    current.removeLastVertex();
                }
            }
        }
    }

    @Override
    public void run() {
        getCollector().pushFlashMessage("thread-" + threadNumber +" started \n");
        bestPath = initialPaths.get(0);
        for (Path current: initialPaths) {
            recursiveStep(current);
        }
        CountResult result = new CoverageModelCountResult();
        result.addSolution("thread-" + threadNumber, bestPath);
        getCollector().addCountResult(result);
    }
}