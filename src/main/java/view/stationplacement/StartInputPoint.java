package view.stationplacement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartInputPoint extends Application {

    public static FXMLLoader inputView = new FXMLLoader(StartInputPoint.class.getResource("inputView.fxml"));

    public static FXMLLoader solverView = new FXMLLoader(StartInputPoint.class.getResource("solverView.fxml"));

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setScene(new Scene((Pane)inputView.load()));
        InsertParamsCoverageModelController controller = (InsertParamsCoverageModelController) inputView.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
