package utils;

import entities.models.helpentity.Path;
import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;
import entities.models.helpentity.Vertex;

import java.util.*;

/**
 * Created by Rodion on 01.06.2015.
 */
public class CoverageModelHelper {

    //20 length
    public static final String SPACE_BAR = "                    ";

    public static final String STATION_TYPE_SET = "STATION_TYPES";
    public static final String PLACEMENT_SET = "PLACEMENTS";
    public static final String START_END_TYPE_NAME = "start_end";

    public static final String TYPE_NAME_PREFIX = "type";
    public static final String PLACEMENT_NAME_PREFIX = "point";

    public static final String START_POINT_NAME = "start_point";
    public static final String END_POINT_NAME = "end_point";

    public static final String COORDINATE_PARAM_NAME = "unsorted_p";
    public static final String COVEARAGE_RADIUS_PARAM_NAME = "r";
    public static final String CONNECTION_RADIUS_PARAM_NAME = "R";
    public static final String COST_PARAM_NAME = "c";

    public static final String BUDGET_PARAM_NAME = "G";

    public static final String STATEMENT_END = ";";
    public static final String NEW_LINE = "\n";

    private static Comparator<Placement> placementComparator = null;
    private static  Comparator<Type> typeComparator = null;

    public static double calculateCoverage (Path path, List<Vertex> graph) {
        return 0;
    }

    public static String constructGLPKDataForCoverageModel (List<Type> types, List<Placement> placements, double budget) {
        StringBuilder builder = new StringBuilder();
        int i;

        appendSet(STATION_TYPE_SET, builder);
        builder.append(START_END_TYPE_NAME).append(" ");
        for(i = 1; i <= types.size(); i++) {
            builder.append(TYPE_NAME_PREFIX).append(i).append(" ");
        }
        builder.append(STATEMENT_END).append(NEW_LINE).append(NEW_LINE);;

        appendSet(PLACEMENT_SET, builder);
        builder.append(START_POINT_NAME).append(" ").append(END_POINT_NAME).append(" ");
        for(i = 1; i < placements.size()-1; i++) {
            builder.append(PLACEMENT_NAME_PREFIX).append(i).append(" ");
        }
        builder.append(STATEMENT_END).append(NEW_LINE).append(NEW_LINE);

        i = 0;
        appendSingleParam(COORDINATE_PARAM_NAME, builder);
        for(Placement p: placements) {
            if (i == 0) {
                appendParamValue(START_POINT_NAME, String.valueOf(p.getCoordinate()), builder);
            } else if (i == placements.size()-1) {
                appendParamValue(END_POINT_NAME, String.valueOf(p.getCoordinate()), builder);
            } else {
                appendParamValue(PLACEMENT_NAME_PREFIX + i, String.valueOf(p.getCoordinate()), builder);
            }
            i++;
        }
        builder.append(STATEMENT_END).append(NEW_LINE).append(NEW_LINE);

        i = 0;
        appendNumberParams(builder, COST_PARAM_NAME, COVEARAGE_RADIUS_PARAM_NAME, CONNECTION_RADIUS_PARAM_NAME);
        for(Type t: types) {
            i++;
            if (t.getCost() == 0) {
                appendNumberParamsValues(START_END_TYPE_NAME, builder, String.valueOf(t.getCost()), String.valueOf(t.getCoverageRadius()),
                        String.valueOf(t.getConnectionRadius()));
            } else {
                appendNumberParamsValues(TYPE_NAME_PREFIX + i, builder, String.valueOf(t.getCost()), String.valueOf(t.getCoverageRadius()),
                        String.valueOf(t.getConnectionRadius()));
            }
        }
        builder.append(STATEMENT_END).append(NEW_LINE).append(NEW_LINE);

        appendSingleParam(BUDGET_PARAM_NAME, builder);
        builder.deleteCharAt(builder.length()-1).append(" ").append(budget);
        builder.append(STATEMENT_END);

        return builder.toString();
    }

    private static void appendSingleParam(String name, StringBuilder out) {
        out.append("param ").append(name).append(" := ").append(NEW_LINE);
    }

    private static void appendNumberParams(StringBuilder out, String ... names) {
        out.append("param ").append(SPACE_BAR.substring("param ".length())).append(":");
        for (String param: names)
            out.append(param).append(SPACE_BAR.substring(param.length()));
        out.append(":=");
        out.append(NEW_LINE);
    }

    private static void appendSet(String name, StringBuilder out) {
        out.append("set ").append(name).append(" := ");
    }

    private static void appendParamValue(String name, String value, StringBuilder builder) {
        builder.append(name).append(SPACE_BAR.substring(name.length())).append(value).append(NEW_LINE);
    }

    private static void appendNumberParamsValues(String name, StringBuilder out, String ... values) {
        out.append(name).append(SPACE_BAR.substring(name.length()));
        for (String param: values)
            out.append(param).append(SPACE_BAR.substring(param.length()));
        out.append(NEW_LINE);
    }

    public static boolean isVertexConnected (Vertex vertex1, Vertex vertex2) {
        double distance = Math.abs(vertex1.getPlacement().getCoordinate() - vertex2.getPlacement().getCoordinate());
        return vertex1.getType().getConnectionRadius() >= distance &&
               vertex2.getType().getConnectionRadius() >= distance;
    }

    public static double getCoverage(Path path, double length) {
        List<Vertex> vertexes = path.getPath();
        TreeSet<CoverageBroad> broadSet = new TreeSet<CoverageBroad>();
        int c = 0;

        for (Vertex vertex: vertexes) {
            broadSet.add(new CoverageBroad(true, vertex.getPlacement().getCoordinate() - vertex.getType().getCoverageRadius()));
            broadSet.add(new CoverageBroad(false, vertex.getPlacement().getCoordinate() + vertex.getType().getCoverageRadius()));
        }

        double start = 0;
        double coverage = 0;
        boolean started = false;
        for (CoverageBroad broad: broadSet) {
            if(broad.isLeftBroad) {
                c++;
            } else {
                c--;
            }

            if ((c > 0) && (!started)) {
                if (broad.getCoordinate() > 0)
                    start = broad.getCoordinate();
                started = true;
            }
            if (c == 0) {
                if (broad.getCoordinate() < length)
                    coverage += broad.getCoordinate() - start;
                else
                    coverage += length - start;
                started = false;
            }
        }
        return coverage;
    }

    /**placements must be already sorted
     * p1 exclusive, p2 inclusive
     * @param p1
     * @param p2
     * @param all
     * @return
     */
    public static Placement[] getPlacementsBetween(double p1, double p2, Placement[] all) {
        Placement placement1 = new Placement(p1);
        Placement placement2 = new Placement(p2);
        int firstIndex = Arrays.binarySearch(all, placement1, getPlacementComparator());
        int secondIndex = Arrays.binarySearch(all, placement2, getPlacementComparator());
        return Arrays.copyOfRange(all, Math.abs(++firstIndex), Math.abs(++secondIndex));
    }

    private static class CoverageBroad implements Comparable {

        double coordinate;
        boolean isLeftBroad;

        private CoverageBroad(boolean isLeftBroad, double coordinate) {
            this.isLeftBroad = isLeftBroad;
            this.coordinate = coordinate;
        }

        public double getCoordinate() {
            return coordinate;
        }

        public boolean isLeftBroad() {
            return isLeftBroad;
        }

        @Override
        public int compareTo(Object o) {
            if (((CoverageBroad) o).getCoordinate() > this.getCoordinate()) {
                return -11;
            } else if (((CoverageBroad) o).getCoordinate() < this.getCoordinate()) {
                return 1;
            } else if (this.isLeftBroad()) {
                if (((CoverageBroad) o).isLeftBroad()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (((CoverageBroad) o).isLeftBroad()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    public static HashMap<Placement, List<Vertex>> createGraph(Placement[] placements, Type[] types) {
        HashMap<Placement, List<Vertex>> graph = new HashMap<Placement, List<Vertex>>();

        int i, j;

        putToGraph(new Vertex(placements[0], types[0]), graph);

        putToGraph(new Vertex(placements[placements.length-1], types[0]),graph);


        //memory n*m, start with only real type
        for (i = 1; i < types.length; i++) {
            for (j = 1; j < placements.length-1; j++) {
                putToGraph(new Vertex(placements[j], types[i]), graph);
            }
        }

        //n*n
        for(Placement placement: graph.keySet()) {
            for (Vertex vertex: graph.get(placement)) {
                calculateChildsFor(vertex, graph);
            }
        }

        return graph;
    }

    private static void putToGraph (Vertex vertex, HashMap<Placement, List<Vertex>> graph) {
        List<Vertex> types = graph.get(vertex.getPlacement());
        if (types == null) {
            types =  new ArrayList<Vertex>();
            graph.put(vertex.getPlacement(), types);
        }
        types.add(vertex);
    }

    public static Vertex findVertexInGraph(Placement p, Type t, HashMap<Placement, List<Vertex>> graph) {
        for (Vertex vertex: graph.get(p)) {
            if (vertex.getType().equals(t)) {
                return vertex;
            }
        }
        return null;
    }

    private static void calculateChildsFor (Vertex vertex, HashMap<Placement, List<Vertex>> graph) {
        for(Placement placement: graph.keySet()) {
            if (placement.getCoordinate() > vertex.getPlacement().getCoordinate()) {
                for (Vertex tempVertex: graph.get(placement)) {
                    if (isVertexConnected(vertex, tempVertex)) {
                        vertex.getChilds().add(tempVertex);
                    }
                }
            }
        }
    }

    public static Comparator<Placement> getPlacementComparator () {
            return new Comparator<Placement>() {
            @Override
            public int compare(Placement o1, Placement o2) {
                if (o1.getCoordinate() > o2.getCoordinate())
                    return 1;
                if (o1.getCoordinate() < o2.getCoordinate())
                    return -1;
                return 0;
            }
        };
    }

    public static Comparator<Type> getTypeComparator () {
      return new Comparator<Type>() {
            @Override
            public int compare(Type o1, Type o2) {
                if (o1.getCost() > o2.getCost())
                    return 1;
                if (o1.getCost() < o2.getCost())
                    return -1;
                return 0;
            }
        };
    }

    public static void main(String[] args) {
        Placement[] p = new Placement[]{
                new Placement(1.0),
                new Placement(5.0),
                new Placement(2.0),
                new Placement(11.0),
                new Placement(3.0),
                new Placement(4.0),
                new Placement(0.0),
                new Placement(21.0),
                new Placement(14.0),
                new Placement(15.0),
                new Placement(7.0),
        };

//        Arrays.sort(p, getPlacementComparator());
//
//        Type [] t = new Type[] {
//            new Type(0.0, 5.0, 15.0),
//            new Type(100.0, 5.0, 5.0),
//            new Type(200.0, 3.0, 10.0),
//
//        };
//
//        List<Vertex> graph = createGraph(p, t);
//        for (Vertex v: graph) {
//           if(v.getPlacement().equals(p[1]))
//               System.out.println(v);
//        }

    }

}
