package entities.models.helpentity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Rodion on 26.05.2015.
 */
public class Type {

    DoubleProperty cost;
    DoubleProperty coverageRadius;
    DoubleProperty connectionRadius;

    public Type(Double cost, Double coverageRadius, Double connectionRadius) {
        this.cost = new SimpleDoubleProperty(cost);
        this.coverageRadius = new SimpleDoubleProperty(coverageRadius);
        this.connectionRadius = new SimpleDoubleProperty(connectionRadius);
    }

    public double getCoverageRadius() {
        return coverageRadius.get();
    }

    public DoubleProperty coverageRadiusProperty() {
        return coverageRadius;
    }

    public void setCoverageRadius(double coverageRadius) {
        this.coverageRadius.set(coverageRadius);
    }

    public double getConnectionRadius() {
        return connectionRadius.get();
    }

    public DoubleProperty connectionRadiusProperty() {
        return connectionRadius;
    }

    public void setConnectionRadius(double connectionRadius) {
        this.connectionRadius.set(connectionRadius);
    }

    public double getCost() {
        return cost.get();
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    @Override
    public String toString() {
        return "Type{" +
                ", cost=" + cost +
                ", coverageRadius=" + coverageRadius +
                ", connectionRadius=" + connectionRadius +
                '}';
    }
}
