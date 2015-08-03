package view.stationplacement;

import entities.models.helpentity.Placement;
import entities.models.helpentity.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class InsertParamsCoverageModelController {
    @FXML
    private TextArea console;

    @FXML
    private TableView<Placement> placementsTable;
    @FXML
    private TableColumn<Placement, Double> coordinateColumn;

    @FXML
    private TableView<Type> typesTable;
    @FXML
    private TableColumn<Placement, Double> coverageRadiusColumn;
    @FXML
    private TableColumn<Placement, Double> costColumn;
    @FXML
    private TableColumn<Placement, Double> connectionRadiusColumn;

    @FXML
    private Button addCoordinateButton;
    @FXML
    private Button deleteCoordinateButton;
    @FXML
    private Button addTypeButton;
    @FXML
    private Button sortPlacementsButton;
    @FXML
    private Button validatePlacementsButton;
    @FXML
    private Button validateAllButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button addBudgetButton;
    @FXML
    private Button deleteTypeButton;


    @FXML
    private TextField inputCoordinate;
    @FXML
    private TextField inputTypeCost;
    @FXML
    private TextField inputTypeCoverageRadius;
    @FXML
    private TextField inputTypeConnectionRadius;
    @FXML
    private TextField inputBudget;
    @FXML
    private TextField money;

    private Stage stage;

    private int placementCounter = 1;

    private int typeCounter = 1;

    private double budget = 0;

    private ObservableList<Placement> placementList = FXCollections.observableArrayList();

    private ObservableList<Type> typesList = FXCollections.observableArrayList();



    @FXML
    private void initialize() {
        placementsTable.setItems(placementList);
        typesTable.setItems(typesList);
        coordinateColumn.setCellValueFactory(new PropertyValueFactory<Placement, Double>("coordinate"));
        coverageRadiusColumn.setCellValueFactory(new PropertyValueFactory<Placement, Double>("coverageRadius"));
        connectionRadiusColumn.setCellValueFactory(new PropertyValueFactory<Placement, Double>("connectionRadius"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Placement, Double>("cost"));

        this.addCoordinateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addCoordinate();
            }
        });

        this.deleteCoordinateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deleteCoordinate();
            }
        });

        this.sortPlacementsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sortPlacements();
            }
        });

        this.validatePlacementsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                validatePlacements();
            }
        });

        this.addTypeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addType();
            }
        });

        this.validateAllButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                validate();
            }
        });

        this.addBudgetButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addBudget();
            }
        });

        this.deleteTypeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deleteType();
            }
        });

        this.submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                submit();
            }
        });
     }

    private void log (String message) {
        this.console.appendText(message + "\n");
    }

    private void addCoordinate() {
        Double value;
        try {
            value = Double.valueOf(this.inputCoordinate.getText());
        } catch (NumberFormatException e) {
            log("bad coordinate " + this.inputCoordinate.getText());
            return;
        }
        placementList.addAll(new Placement(value));

    }

    private void deleteCoordinate() {
        Placement placement = placementsTable.getFocusModel().getFocusedItem();
        if (placement != null)
            placementList.remove(placement);
    }

    private void sortPlacements() {
        int size = placementList.size();
        if (size > 0) {
            ObservableList<Placement> sortedList = FXCollections.observableArrayList();
            while (!placementList.isEmpty()) {
                Placement temp = placementList.get(0);
                for (Placement placement : placementList) {
                    if (temp.getCoordinate() >= placement.getCoordinate()) {
                        temp = placement;
                    }
                }
                Placement out = new Placement(placementList.remove(placementList.indexOf(temp)).getCoordinate());
                sortedList.addAll(out);
            }

            placementList.addAll(sortedList);
        }
    }

    private void validatePlacements () {
        boolean hasZero = false;
        for (int i = 0; i < placementList.size(); i++) {
            Placement temp = placementList.get(i);
            if (temp.getCoordinate() >= 0) {
                if (!hasZero)
                hasZero = temp.getCoordinate() == 0;
                for (int j = i+1; j < placementList.size(); j++) {
                    if (temp.getCoordinate() == placementList.get(j).getCoordinate()) {
                        log("double value " + temp.getCoordinate()+ ". Remove one.");
                        placementList.remove(placementList.get(j));
                        j--;
                    }
                }
            } else {
                log("negative value " + temp.getCoordinate()+ ". Remove one.");
                placementList.remove(temp);
                i--;
            }
        }
        if(!hasZero) {
            placementList.addAll(new Placement(0.0));
            log("no zero value - added");
        }
        sortPlacements();
        log("sector length = " + placementList.get(placementList.size()-1).getCoordinate() + " count of possible placements = " + placementList.size());
    }

    private void addType() {
        try {
            double cost = Double.valueOf(inputTypeCost.getText());
            double connectionRadius = Double.valueOf(inputTypeConnectionRadius.getText());
            double coverageRadius = Double.valueOf(inputTypeCoverageRadius.getText());
            typesList.addAll(new Type(cost, coverageRadius, connectionRadius));
        } catch (NumberFormatException e) {
            log("bad type params: " + inputTypeCost.getText()  + " " + inputTypeCoverageRadius.getText() + " " + inputTypeConnectionRadius.getText());
        }
    }

    private void validate() {
        boolean hasStartEndType = false;
        validatePlacements();
        double length = placementList.isEmpty() ? 0 : placementList.get(placementList.size()-1).getCoordinate();

        for (int i = 0; i < typesList.size(); i++) {
            Type current = typesList.get(i);
            if (current.getCost() >= 0 && current.getCoverageRadius() >= 0 && current.getConnectionRadius() >= 0) {
                if (current.getCost() == 0 && current.getCoverageRadius() == 0) {
                    if (!hasStartEndType) {
                        hasStartEndType = true;
                        if (current.getConnectionRadius() < length) {
                            current.setConnectionRadius(length);
                        }
                        continue;
                    }
                }

                if ((current.getCost() == 0) || (current.getCoverageRadius() == 0) || (current.getConnectionRadius()==0)) {
                    log("Invalid zero value removed");
                    typesList.remove(current);
                    i--;
                    continue;
                }

                for (int j = i+1; j < typesList.size(); j++) {
                    Type temp = typesList.get(j);
                    if (current.getConnectionRadius() == temp.getConnectionRadius() &&
                            current.getCost() == temp.getCost() &&
                            current.getCoverageRadius() == temp.getCoverageRadius())
                    {
                        typesList.remove(temp);
                        j--;
                    }
                }
            } else {
                log("There are negative values. Removed.");
                typesList.remove(current);
                i--;
            }

        }
        if(!hasStartEndType) {
            typesList.addAll(new Type(0.0,0.0,length));
            log("no start_end value - added");
        }
        typeCounter = 1;
        if (budget == 0) {
            log("Warn: budget = 0");
        }
    }

    private void deleteType() {
        Type type = typesTable.getFocusModel().getFocusedItem();
        if (type != null)
            typesList.remove(type);
    }

    private void addBudget() {
        try {
            budget = Double.valueOf(inputBudget.getText());
            money.setText(String.valueOf(budget));
        } catch (NumberFormatException e) {
            log("bad buget" + inputBudget.getText());
        }
    }

    public void submit() {
        try {
            validate();
            this.getStage().close();
            Stage newStage = new Stage();
            Scene scene = new Scene((Pane)StartInputPoint.solverView.load());
            SolverViewController controller = StartInputPoint.solverView.getController();
            controller.setBudget(getBudget());
            controller.setTypesList(getTypesList());
            controller.setPlacementList(getPlacementList());
            controller.refresh();
            newStage.setScene(scene);

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public ObservableList<Placement> getPlacementList() {
        return placementList;
    }

    public void setPlacementList(ObservableList<Placement> placementList) {
        this.placementList = placementList;
    }

    public ObservableList<Type> getTypesList() {
        return typesList;
    }

    public void setTypesList(ObservableList<Type> typesList) {
        this.typesList = typesList;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
