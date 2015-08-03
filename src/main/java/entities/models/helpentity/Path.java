package entities.models.helpentity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Rodion on 01.06.2015.
 */
public class Path implements Cloneable{
    private LinkedList<Vertex> path;
    private Double cost;
    private Double absoluteCoverage;

    private Double improveCoverage;
    private Double improveCost;

    public Path() {
        this.path = new LinkedList<Vertex>();
        this.cost = 0.0;
        this.absoluteCoverage = 0.0;
        this.improveCost = 0.0;
        this.improveCoverage = 0.0;
    }

    public LinkedList<Vertex> getPath() {
        return path;
    }

    public Double getCost() {
        return cost;
    }

    private void addCost(double addition) {
        cost += addition;
    }

    public void reduceCost(double addition) {
        cost -= addition;
    }

    public void addVertex(Vertex vertex) {
        addCost(vertex.getType().getCost());
        this.path.add(vertex);
    }

    public void removeLastVertex () {
        reduceCost(path.removeLast().getType().getCost());
    }

    public Double getAbsoluteCoverage() {
        return absoluteCoverage;
    }

    public void setAbsoluteCoverage(Double absoluteCoverage) {
        this.absoluteCoverage = absoluteCoverage;
    }

    public void setPath(LinkedList<Vertex> path) {
        this.path = path;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getImproveCoverage() {
        return improveCoverage;
    }

    public void setImproveCoverage(Double improveCoverage) {
        this.improveCoverage = improveCoverage;
    }

    public Double getImproveCost() {
        return improveCost;
    }

    public void setImproveCost(Double improveCost) {
        this.improveCost = improveCost;
    }

    public Double getImprovmentFactor() {
        return improveCost <= 0 ? improveCoverage : improveCoverage/improveCost;
    }


    @Override
    public Path clone() {
        Path instance = new Path();
        instance.cost = this.cost;
        instance.absoluteCoverage = this.absoluteCoverage;
        instance.improveCost = this.improveCost;
        instance.improveCoverage = this.improveCoverage;
        instance.getPath().addAll(this.path);
        return instance;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Path by ")
                .append("{c= ").append(this.getCost())
                .append(", cover = ").append(this.getAbsoluteCoverage())
                .append(", addCover=").append(this.getImproveCoverage())
                .append(", addCost=").append(this.getImproveCost());
        builder.append(" vertex: {");
        for (Vertex child: path) {
            builder.append("\n   ").append("p = ").append(child.placement.getCoordinate())
                    .append("  type =").append(child.getType().getCost())
                    .append(", ").append(child.getType().getCoverageRadius())
                    .append(", ").append(child.getType().getConnectionRadius());

        }
        builder.append("\n");

        return builder.toString();
    }
}
