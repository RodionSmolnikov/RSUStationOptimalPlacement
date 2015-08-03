package view.stationplacement;


import entities.results.collectors.ConsoleResultCollector;
import entities.filemanager.InputParamsForCoverageModelManager;
import entities.interfaces.ParameterContainer;
import entities.paramcontainers.DefaultParamContainer;
import entities.results.handlers.CoverageModelResultHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import system.Executor;
import system.ExecutorFactory;
import system.impl.ParallelBruteForceModelFactory;
import system.impl.ParameterTypes;
import utils.CoverageModelHelper;
import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by Rodion on 02.06.2015.
 */
public class SolverViewController {
    Stage stage;

    @FXML
    private TextArea console;
    @FXML
    private TextArea GLPKdata;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField loadFileName;

    @FXML
    private TextField saveFileName;

    @FXML
    private Button generateFileNameButton;

    @FXML
    private Button loadButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadChooseButton;

    @FXML
    private TableView<Placement> placementsTable;
    @FXML
    private TableColumn<Placement, Double> placements;

    @FXML
    private TableView<Type> typesTable;
    @FXML
    private TableColumn<Placement, Double> coverageRadius;
    @FXML
    private TableColumn<Placement, Double> cost;
    @FXML
    private TableColumn<Placement, Double> connectionRadius;

    @FXML
    private MenuItem bruteForceStart2;
    @FXML
    private MenuItem bruteForceStart4;
    @FXML
    private MenuItem heuristicStart;
    @FXML
    private MenuItem makeGLPKData;

    private double budget = 0;

    private ObservableList<Placement> placementList;

    private ObservableList<Type> typesList;

    private File loadedFile = null;

    @FXML
    public void initialize() {
        placements.setCellValueFactory(new PropertyValueFactory<Placement, Double>("coordinate"));

        coverageRadius.setCellValueFactory(new PropertyValueFactory<Placement, Double>("coverageRadius"));
        connectionRadius.setCellValueFactory(new PropertyValueFactory<Placement, Double>("connectionRadius"));
        cost.setCellValueFactory(new PropertyValueFactory<Placement, Double>("cost"));

        this.loadChooseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //saveFileName.setText(InputParamsForCoverageModelManager.getInstance().generateDefaultFileNameToSave());
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Input File");
                fileChooser.setInitialDirectory(InputParamsForCoverageModelManager.getInstance().getCurrentDir());
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    loadedFile = file;
                    loadFileName.setText(file.getName());
                }
            }
        });

        this.loadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (loadedFile != null) {
                    loadFile(loadedFile);
                } else {
                    loadFile();
                }

            }
        });

        this.saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                saveFile();
            }
        });

        this.generateFileNameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                saveFileName.setText(InputParamsForCoverageModelManager.getInstance().generateDefaultFileNameToSave());
            }
        });

        this.heuristicStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader(StartInputPoint.class.getResource("console.fxml"));
                try {

                    Stage newStage = new Stage();
                    Scene scene = new Scene((Pane)loader.load());
                    ConsoleView controller = loader.getController();
                    newStage.setScene(scene);
                    newStage.show();

                    Executor executor = ExecutorFactory.getInstance().getExecutor(new ConsoleResultCollector(controller, new CoverageModelResultHandler()) ,false);
                    executor.execute(null);
                } catch (Exception e) {
                    log("can't load console.fxml");
                    e.printStackTrace();
                }

            }
        });

        this.bruteForceStart2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader(StartInputPoint.class.getResource("console.fxml"));
                try {

                    Stage newStage = new Stage();
                    Scene scene = new Scene((Pane) loader.load());
                    ConsoleView controller = loader.getController();
                    newStage.setScene(scene);
                    newStage.show();

                    Executor executor = ExecutorFactory.getInstance().getExecutor(new ConsoleResultCollector(controller, new CoverageModelResultHandler()), true, Integer.valueOf(2));
                    executor.execute(new ParallelBruteForceModelFactory(getParamContainer(), executor.getThreadCount()));
                } catch (Exception e) {
                    log("can't load console.fxml");
                    e.printStackTrace();
                }

            }
        });

    }

    public void refresh() {
        placementsTable.setItems(placementList);
        typesTable.setItems(typesList);
        budgetField.setText(String.valueOf(budget));
        GLPKdata.setText(CoverageModelHelper.constructGLPKDataForCoverageModel(this.getTypesList(), this.getPlacementList(), this.getBudget()));
    }

    private void saveFile () {
        InputParamsForCoverageModelManager manager = InputParamsForCoverageModelManager.getInstance();
        try {
            manager.setCurrentFileName(saveFileName.getText().trim());
            manager.saveFile(getParamContainer());
            log("File " + manager.getCurrentFileName() + " successfully saved");
        } catch (IOException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadFile () {
        File file =  new File(loadFileName.getText().trim());
        loadFile(file);
    }

    private void loadFile (File file) {
        InputParamsForCoverageModelManager manager = InputParamsForCoverageModelManager.getInstance();
        try {
            deployParamContainer(manager.loadFile(file));
            refresh();
            log(manager.getCurrentFileName() + " successfully loaded");
        } catch (IOException e) {
            log(e.getMessage());
            e.printStackTrace();
        }
    }

    private ParameterContainer getParamContainer() {
        DefaultParamContainer container = new DefaultParamContainer();
        container.putParameter(ParameterTypes.POSIBLE_PLACEMENT, this.getPlacementList().toArray(new Placement[getPlacementList().size()]));
        container.putParameter(ParameterTypes.STATION_TYPES, this.getTypesList().toArray(new Type[getTypesList().size()]));
        container.putParameter(ParameterTypes.BUDGET, this.getBudget());
        return container;
    }

    private void deployParamContainer (ParameterContainer container) {
        this.setTypesList(FXCollections.observableArrayList((Type[]) container.getParameter(ParameterTypes.STATION_TYPES)));
        this.setPlacementList(FXCollections.observableArrayList((Placement[]) container.getParameter(ParameterTypes.POSIBLE_PLACEMENT)));
        this.setBudget((Double) container.getParameter(ParameterTypes.BUDGET));
    }

    private void log (String message) {
        this.console.appendText(message + "\n");
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ObservableList<Type> getTypesList() {
        return typesList;
    }

    public void setTypesList(ObservableList<Type> typesList) {
        this.typesList = typesList;
    }

    public ObservableList<Placement> getPlacementList() {
        return placementList;
    }

    public void setPlacementList(ObservableList<Placement> placementList) {
        this.placementList = placementList;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
