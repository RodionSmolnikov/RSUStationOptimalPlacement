package entities.filemanager;

import entities.interfaces.ParameterContainer;
import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;
import entities.paramcontainers.DefaultParamContainer;
import system.impl.ParameterTypes;
import utils.CoverageModelHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rodion on 04.06.2015.
 */
public class InputParamsForCoverageModelManager extends AbstractFileManager {

    private static final String DEFAULT_EXTENION = "inp";
    private static final String DEFAULT_FACTORY_NAME = "CoverageModelInput";

    private static InputParamsForCoverageModelManager INSTANCE = null;

    private InputParamsForCoverageModelManager(String extention, String factoryName) {
        super(extention, factoryName);
    }

    private InputParamsForCoverageModelManager () {
        this(DEFAULT_EXTENION, DEFAULT_FACTORY_NAME);
    }

    public static InputParamsForCoverageModelManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InputParamsForCoverageModelManager();
        }
        return INSTANCE;
    }

    @Override
    protected void writeInputParamsToFile(FileOutputStream stream, ParameterContainer container) throws IOException {
        //use math prog Format
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        writer.write(CoverageModelHelper.constructGLPKDataForCoverageModel(
            Arrays.asList((Type[])container.getParameter(ParameterTypes.STATION_TYPES)),
            Arrays.asList((Placement[]) container.getParameter(ParameterTypes.POSIBLE_PLACEMENT)),
            (Double) container.getParameter(ParameterTypes.BUDGET)));
        writer.close();
    }

    @Override
    protected ParameterContainer getParamsFromFile(FileInputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        boolean typeBlock = false;
        boolean placementBlock = false;
        DefaultParamContainer container = new DefaultParamContainer();
        List<Type> types = new ArrayList<Type>();
        List<Placement> placements = new ArrayList<Placement>();
        int counter = 0;

            while (reader.ready()) {

            String line = reader.readLine();
            if (line.startsWith(CoverageModelHelper.STATEMENT_END)){
                typeBlock = false;
                placementBlock = false;
                counter = 0;
            }
            if (typeBlock) {
                String [] elements = line.split("[\\s]+");
                 types.add(new Type(Double.valueOf(elements[1]), Double.valueOf(elements[2]), Double.valueOf(elements[3])));
            }
            if (placementBlock) {
                String [] elements = line.split("[\\s]+");
                placements.add(new Placement(Double.valueOf(elements[1])));
            }
            if (line.startsWith("param")) {
                if (line.contains(CoverageModelHelper.COORDINATE_PARAM_NAME))
                    placementBlock = true;
                else if (line.contains(CoverageModelHelper.CONNECTION_RADIUS_PARAM_NAME) &&
                        line.contains(CoverageModelHelper.COVEARAGE_RADIUS_PARAM_NAME) &&
                        line.contains(CoverageModelHelper.COST_PARAM_NAME))
                    typeBlock = true;
                else {
                    String [] elements = line.split("\\s+");
                    Double budget = Double.valueOf(elements[elements.length-1].substring(0, elements[elements.length-1].length()-2));
                    container.putParameter(ParameterTypes.BUDGET, budget);
                }
            }
        }
        container.putParameter(ParameterTypes.POSIBLE_PLACEMENT, placements.toArray(new Placement[]{}));
        container.putParameter(ParameterTypes.STATION_TYPES, types.toArray(new Type[] {}));
        try {
            return container;
        } finally {
            reader.close();
        }
    }


}
