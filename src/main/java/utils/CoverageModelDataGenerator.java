package utils;

import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;

import java.util.List;

/**
 * Created by Rodion on 15.06.2015.
 */
public class CoverageModelDataGenerator {
    private class ElementaryBlock {
        int connectionLeft;
        int conectionRight;
        int coverageLeft;
        int coverageRight;
        double block;
        public double getLeftConnectionBroad () {
            return connectionLeft*block;
        }
        public double getRightConnectionBroad () {
            return conectionRight*block;
        }
        public double getLeftCoverageBroad () {
            return coverageLeft*block;
        }
        public double getRightCoverageBroad () {
            return coverageRight*block;
        }

        private ElementaryBlock(int connectionLeft, int conectionRight, int coverageLeft, int coverageRight, double block) {
            this.connectionLeft = connectionLeft;
            this.conectionRight = conectionRight;
            this.coverageLeft = coverageLeft;
            this.coverageRight = coverageRight;
            this.block = block;
        }
    }

    ElementaryBlock elementBlock;
    int necessaryNumberOfPlacement;
    int necessaryNumberOfTypes;
    double lengthOfRoad;

    int connectionLeft;
    int conectionRight;
    int coverageLeft;
    int coverageRight;

    public CoverageModelDataGenerator(int necessaryNumberOfPlacement, int necessaryNumberOfTypes, double lengthOfRoad, int connectionLeft, int conectionRight, int coverageLeft, int coverageRight) {
        this.necessaryNumberOfPlacement = necessaryNumberOfPlacement;
        this.necessaryNumberOfTypes = necessaryNumberOfTypes;
        this.lengthOfRoad = lengthOfRoad;
        ElementaryBlock block = new ElementaryBlock(connectionLeft, conectionRight, coverageLeft, coverageRight, lengthOfRoad/necessaryNumberOfPlacement);
        this.connectionLeft = connectionLeft;
        this.conectionRight = conectionRight;
        this.coverageLeft = coverageLeft;
        this.coverageRight = coverageRight;
    }

    public List<Placement> generatePlacements() {return null;}

    public List<Type> generateType() {return null;}

}
