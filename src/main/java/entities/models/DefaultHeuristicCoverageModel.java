package entities.models;

import annotations.Param;
import entities.results.collectors.DefaultResultCollector;
import entities.interfaces.Model;
import entities.models.helpentity.Path;
import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;
import entities.models.helpentity.Vertex;
import entities.paramcontainers.DefaultParamContainer;
import entities.results.handlers.CoverageModelResultHandler;
import exception.ModelException;
import system.Executor;
import system.ExecutorFactory;
import system.impl.ParameterTypes;
import utils.CoverageModelHelper;

import java.util.*;

/**
 * Created by Rodion on 28.05.2015.
 */
public class DefaultHeuristicCoverageModel extends Model {

    @Param(type = ParameterTypes.STATION_TYPES)
    public Type[] types;

    @Param(type = ParameterTypes.POSIBLE_PLACEMENT)
    public Placement[] placements;

    @Param(type = ParameterTypes.BUDGET)
    public Double budget;

    Type startEndType;

    public HashMap<Placement, List<Vertex>> graph;

    private Path improveVariant;

    public ArrayList<Path> paths;

    //algorithm params
    //@Param(type = ParameterTypes.PATH_BROAD)
    public int PATH_BROAD = 50;

    //@Param(type = ParameterTypes.IMPROVABLE_SET_BROAD)
    public int IMPROVABLE_SET_BROAD = 10;

    //@Param(type = ParameterTypes.EPSILON)
    public double EPSILON = 0.01;


    @Override
    public void run() {
        //Phase 0, initialization
        init();

        //Phase 1, create graph
        initGraph();

        //Phase 2, find paths
        //findPaths();

        //Phase 3, get set of improvable paths
        //getSerchedPaths();

        //Phase 4
        //improvePath();
    }

    @Override
    public int getThreadNumber() {
        return 0;
    }

    public  void init() {
        paths = new ArrayList<>();
    }
    //Phase 1
    private void initGraph() {
        graph = CoverageModelHelper.createGraph(placements, types);
    }

    //Phase 2
    private void findPaths() {
        LinkedList<Vertex> greys = new LinkedList<Vertex>();
        LinkedList<Vertex> white = new LinkedList<Vertex>();

        for (List<Vertex> vertexes : graph.values())
            white.addAll(vertexes);

        Vertex startVertex = CoverageModelHelper.findVertexInGraph(placements[0], types[0], graph);
        Vertex endVertex = CoverageModelHelper.findVertexInGraph(placements[placements.length - 1], types[0], graph);

        white.remove(startVertex);

        greys.add(startVertex);
        startVertex.setPath(new Path());
        paths.add(startVertex.getPath());
        startVertex.getPath().addVertex(startVertex);

        while (!greys.isEmpty()) {
            Vertex current = greys.getFirst();
            //current.getPath().addCost(current.getType().getCost());
            if (!current.getChilds().isEmpty()) {
                for (Vertex vertex : current.getChilds()) {
                    if (white.contains(vertex)) {
                        if ((current.getPath().getCost() + vertex.getType().getCost()) <= budget) {
                            if (vertex.equals(endVertex)) {
                                Path pathToAdd = current.getPath().clone();
                                pathToAdd.addVertex(endVertex);
                                paths.add(pathToAdd);
                                if (paths.size() == PATH_BROAD) return;
                            }
                            white.remove(vertex);
                            if (!greys.contains(vertex)) greys.add(vertex);
                            vertex.setPath(current.getPath().clone());
                            vertex.getPath().addVertex(vertex);

                        }
                    } else {
                        if (current.getPath().getCost() <= budget && current.getChilds().isEmpty()) {
                            paths.add(current.getPath());
                            if (paths.size() == PATH_BROAD) return;
                        }
                    }
                    greys.remove(current);
                }
            }
        }
    }

    //Phase 3 - get paths with max distance
    private void getSerchedPaths () {
        paths = paths.size() >= IMPROVABLE_SET_BROAD ? (ArrayList<Path>) paths.subList(0,IMPROVABLE_SET_BROAD):
                                                       (ArrayList<Path>) paths.subList(0,paths.size());

    }

    //Phase 4 - make the path better by improvement
    private void improvePaths (){
        for (Path currentPath: paths) {
            do {
                improveVariant = currentPath.clone();
                //improvementByPlacingNewStation();
                //improvementByVarientingStationTypeOrPlacement();
            } while (improveVariant.equals(currentPath) );
        }
    }

    protected boolean isVariantBetterVarints(Path newVariat) {
        return false;
    }


    private Path improvementByPlacingNewStation() {

        for (Placement p: graph.keySet()) {
            for (Vertex v: graph.get(p)) {
                if (improveVariant.getCost() + v.getType().getCost() <= budget) {

                }
            }
        }
        return null;
    }

    private Path improvementByVarientingStationTypeOrPlacement() {
        return null;
    }

    protected double calculateDifferenceBetweenPaths(Path path1, Path path2) {
        return 0;
    }


    public static void main(String[] args) {
        DefaultParamContainer paramContainer = new DefaultParamContainer();
        Type[] types = new Type[] {new Type(1.2, 3.2, 3.1)};
        paramContainer.putParameter(ParameterTypes.STATION_TYPES, types);

        Placement[] p = new Placement[] {new Placement(3.2)};
        paramContainer.putParameter(ParameterTypes.POSIBLE_PLACEMENT, p);

        paramContainer.putParameter(ParameterTypes.BUDGET, Double.valueOf("1"));



    }
}
