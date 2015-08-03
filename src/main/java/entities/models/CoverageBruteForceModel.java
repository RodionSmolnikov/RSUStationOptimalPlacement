package entities.models;

import entities.interfaces.Model;
import entities.interfaces.ResultCollector;

import java.util.*;

/**
 * Created by Rodion on 12.05.2015.
 */
public class CoverageBruteForceModel extends Model {

    private CoverageCountResult result = new CoverageCountResult();

    private ResultCollector collector;


    @Override
    public int getThreadNumber() {
        return 0;
    }

    @Override
    public ResultCollector getCollector() {
        return this.collector;
    }

    @Override
    public void setCollector(ResultCollector collector) {
        this.collector = collector;
    }


    public void init () {
        Arrays.sort(p);

        //sepecial type of first and last stations
        double[] specialType = new double[3];
        specialType[0] = 0;
        specialType[1] = 0.00001;
        specialType[2] = end - p[0];



        t = Arrays.copyOf(t, t.length+1);
        t[t.length-1] = specialType;


        //put first special station
        positionMap.add(new AbstractMap.SimpleEntry<Integer, Integer>(0, t.length - 1));
    }

    //parameters

    //station type t[types][3] -
    // 0 - cost,
    // 1 - coverage radius,
    // 2 - connection radius
    //@Param(type = Pa)
    public double t[][];

    //possible placement
    //@Param
    public double p[];

    //@Param
    //end of the sector
    public double end;

    //budget
    //@Param
    public double C;

    //variables

    //key - number of placement, value - number of type
    private LinkedList<Map.Entry<Integer, Integer>> positionMap = new LinkedList<Map.Entry<Integer, Integer>>();

    //key - coordinate of a side of coverage sector of a station.   [left side]_____((|))_____[right side]
    //value - true(left side), false(right side)
    //map is sorted
    private TreeSet<Map.Entry<Double, Boolean>> sideCoverageMap = new TreeSet<Map.Entry<Double, Boolean>>();

    private boolean checkFinishConnection() {
        //connection distance of last station can connect to the end of the sector
        return (p[positionMap.getLast().getKey()] + t[positionMap.getLast().getValue()][2]) > end;
    }

    private double getCoverage() {
        sideCoverageMap.clear();
        int counter = 0, endCounterValue = 0;

        //side - temp variable for memory economy
        double side = 0, coverage = 0;

        for (Map.Entry<Integer, Integer> stations: positionMap) {
            //put to sideCoverageMap left and right sides of coverage for each station on positions
            side = p[stations.getKey()] - t[stations.getValue()][1];
            if (side < 0) {
                counter++;
            } else {
                sideCoverageMap.add(new CoverageEntry<Double, Boolean>(side, true));
            }

            side = p[stations.getKey()] + t[stations.getValue()][1];
            if (side > end) {
                endCounterValue++;
            } else {
                sideCoverageMap.add(new CoverageEntry<Double, Boolean>(side, false));
            }
        }

        //the same variables side for computing system coverage. (( [start]__(_)_)___)---(_(_)_)--(_)--(__________)__[end]
        if (counter > 0) side = 0;

        Iterator<Map.Entry<Double, Boolean>> des = sideCoverageMap.descendingIterator();
        Map.Entry<Double, Boolean> stations;
        while (des.hasNext()) {
            stations = des.next();
            if (stations.getValue()) {
                if (counter == 0) side = stations.getKey();
                counter++;
            } else {
                counter--;
                if (counter == 0) coverage += stations.getKey() - side;
            }
        }

        if (counter != endCounterValue) {
            throw new RuntimeException("error while counting coverage: counter = " + counter +  "endCounterValue = "+ endCounterValue);
        }

        if (counter > 0) {
            coverage += end - side;
        }
        return coverage;
    }

    private double getCoveragePercentage() {
        return 100.0*(getCoverage()/end);
    }

    protected void tryToAddResult() {
        double currentCover = getCoveragePercentage();
        System.out.println("Chain " + positionMap + " cover " + currentCover + " current budget " + this.C);
        if (result.getCoveragePercentage() < currentCover) {
            this.result.writeCoveragePercentage(currentCover);
            this.result.writePositionMap(this.positionMap);
        }
    }

    private double [] getNextPoints() {
        int lastType = positionMap.getLast().getValue();
        int lastStationPositionNumber = positionMap.getLast().getKey();
        double avaluableConnectionAreaBroad = p[lastStationPositionNumber] + t[lastType][2];
        if (lastStationPositionNumber != (p.length-1)) {
            return getPointsBetweenBroads(p[lastStationPositionNumber], avaluableConnectionAreaBroad, false);
        }
        return null;
    }


    protected double [] getPointsBetweenBroads(double start, double end, boolean saveStart) {
        if (start > end) return null;
        int startIndex=-1, endIndex=-1;
        for (int i=0; i<p.length; i++) {
            if (p[i]>=start && startIndex<0) {
                startIndex = i;
            }
            if (p[i]>=end && endIndex<0) {
                endIndex = i;
            }
        }
        if (startIndex < 0) return null;
        if (end > p[p.length-1]) endIndex = p.length;
        if (start == p[startIndex] && saveStart) {
            return Arrays.copyOfRange(p, startIndex, endIndex);
        } else {
            return Arrays.copyOfRange(p, startIndex + 1, endIndex);
        }
    }

    private void removeLastStation () {
        Map.Entry<Integer, Integer> lastStation = positionMap.removeLast();
        //return cost of station
        this.C += t[lastStation.getValue()][0];
    }

    private void insertStation (int position, int type) {
        positionMap.add(new AbstractMap.SimpleEntry<Integer, Integer>(position, type));
        //return cost of station
        this.C -= t[type][0];
    }

    private boolean canStationsConnect(int position1,int type1,int position2,int type2) {
        double distance = Math.abs(position1-position2);
        return t[type1][2] > distance && t[type2][2] > distance;
    }

    private boolean canBought(int type) {
        return C >= t[type][0];
    }

    private void printType (int type) {
        System.out.println(" cost=" + t[type][0] +"\n coverage radius =" + t[type][1] + "\n connection distanece =" + t[type][2]);
    }

    private void stepToNextPoint () {
        if (checkFinishConnection()) {
            tryToAddResult();
        }
        //for every available position
        double [] nextPoints = getNextPoints();
        if (nextPoints != null && nextPoints.length > 0) {
            System.out.println("points founded");
            for (double positionCoordite : nextPoints) {
                //for every avaluable types
                for (int i = 0; i < t.length-1; i++) {
                    if (canStationsConnect(positionMap.getLast().getKey(), positionMap.getLast().getValue(), Arrays.binarySearch(p, positionCoordite), i) && canBought(i)) {
                        insertStation(Arrays.binarySearch(p, positionCoordite), i);
                        System.out.println(positionMap);
                        printType(i);
                        stepToNextPoint();
                    }
                }
            }
        }
        removeLastStation();
    }

    @Override
    public void run() {
        init();
        stepToNextPoint();
        getCollector().addCountResult(this.result);
    }

    public static void main(String[] args) {
        double p[] = new double[4];
        p[0] = 0;
       // p[1] = 10;
       // p[2] = 12;
        p[1] = 16;
       // p[1] = 31;
        p[2] = 40;
        p[3] = 50;

        double t[][] = new double[2][3];
//      cost            coverage radius   connection radius
        t[0][0] = 200;   t[0][1] = 15;      t[0][2] = 30;
        t[1][0] = 150;   t[1][1] = 10;     t[1][2] = 30;
        //t[2][0] = 100;   t[1][1] = 5;     t[1][2] = 20;
        //t[3][0] = 200;   t[1][1] = 2;     t[1][2] = 10;

        CoverageBruteForceModel model = new CoverageBruteForceModel();
        model.setP(p);
        model.setC(350);
        model.setT(t);
        model.setEnd(50);

//        Executor executor = new SingleThreadExecutor(new ConsoleResultCollector());
//        try {
//            executor.execute(model);
//        } catch (BruteForceException e) {
//            e.printStackTrace();
//        }
    }

    public double[][] getT() {
        return t;
    }

    public void setT(double[][] t) {
        this.t = t;
    }

    public double[] getP() {
        return p;
    }

    public void setP(double[] p) {
        this.p = p;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public double getC() {
        return C;
    }

    public void setC(double c) {
        C = c;
    }

    public LinkedList<Map.Entry<Integer, Integer>> getPositionMap() {
        return positionMap;
    }

    public void setPositionMap(LinkedList<Map.Entry<Integer, Integer>> positionMap) {
        this.positionMap = positionMap;
    }

    public CoverageCountResult getResult() {
        return result;
    }

    public void setResult(CoverageCountResult result) {
        this.result = result;
    }

    private class CoverageEntry<D extends Double, B extends Boolean> extends AbstractMap.SimpleEntry<D, B> implements Comparable {

        public CoverageEntry(D key, B value) {
            super(key, value);
        }

        @Override
        public int compareTo(Object o) {
            CoverageEntry<D, B> entry = (CoverageEntry<D, B>) o;
            if (entry.getKey().intValue() >= this.getKey().intValue()) {
                return 1;
            } else {
                return -1;
            }
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }
}

