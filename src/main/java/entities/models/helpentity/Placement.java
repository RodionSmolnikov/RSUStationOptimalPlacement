package entities.models.helpentity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Rodion on 26.05.2015.
 */
public class Placement {

    private DoubleProperty coordinate;

    public Placement(Double coordinate) {

        this.coordinate = new SimpleDoubleProperty(coordinate);
    }


    public double getCoordinate() {
        return coordinate.get();
    }

    public DoubleProperty coordinateProperty() {
        return coordinate;
    }

    public void setCoordinate(double coordinate) {
        this.coordinate.set(coordinate);
    }

    @Override
    public String toString() {
        return "Placement{" +
                ", coordinate=" + coordinate +
                '}';
    }
}
