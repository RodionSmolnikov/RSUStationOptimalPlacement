package entities.models;

import entities.interfaces.CountResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rodion on 13.05.2015.
 */
@Deprecated
public class CoverageCountResult implements CountResult {
    private Map<Integer, Integer> positionMap;

    private double coveragePercentage;

    public CoverageCountResult() {
        this.positionMap = new HashMap<Integer, Integer>();
    }

    public Map<Integer, Integer> getPositionMap() {
        return positionMap;
    }

    public void writePositionMap(List<Map.Entry<Integer, Integer>> positionMap) {
        this.positionMap.clear();
        for (Map.Entry<Integer,Integer> station: positionMap){
            this.positionMap.put(station.getKey(), station.getValue());
        }
    }

    public double getCoveragePercentage() {
        return coveragePercentage;
    }

    public void writeCoveragePercentage(double coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("End of calculation\n");
        builder.append("final coverage = ").append(coveragePercentage).append("%\n");
        for(Map.Entry<Integer, Integer> station: positionMap.entrySet()) {
            builder.append("In position ").append(station.getKey()).append(" station type ").append(station.getValue()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public Object getSolution(String key) {
        return null;
    }

    @Override
    public void addSolution(String key, Object solution) {

    }

    @Override
    public Set getSolutions() {
        return null;
    }

    @Override
    public Set<String> keys() {
        return null;
    }
}
