package entities.models.helpentity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rodion on 01.06.2015.
 */
public class Vertex {
    Placement placement;
    Type type;
    List<Vertex> childs;

    private Path myPath;

    public Vertex(Placement placement, Type type) {
        this.placement = placement;
        this.type = type;
        this.childs = new LinkedList<Vertex>();
    }

    public Placement getPlacement() {
        return placement;
    }

    public Type getType() {
        return type;
    }

    public List<Vertex> getChilds() {
        return childs;
    }

    public String getShortString() {
        return "p = " + this.getPlacement() + "t = " + type.getCost();
    }

    public Path getPath() {
        return myPath;
    }

    public void setPath(Path myPath) {
        this.myPath = myPath;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("in ").append(placement.getCoordinate())
               .append(", typed {c= ").append(type.getCost())
                .append(", r= ").append(type.getCoverageRadius())
                .append(", R=").append(type.getConnectionRadius());
        builder.append(" childs: {");
        for (Vertex child: childs) {
            builder.append("\n   ").append("p = ").append(child.placement.getCoordinate())
                    .append("  type =").append(child.getType().getCost())
                    .append(", ").append(child.getType().getConnectionRadius())
                    .append(", ").append(child.getType().getCoverageRadius());
        }

       return builder.toString();
    }

}
