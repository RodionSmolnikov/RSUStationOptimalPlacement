package view.stationplacement;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Created by Rodion on 08.06.2015.
 */
public class ConsoleView {
    @FXML
    private TextArea console;

    @FXML
    private void initialize() {
        console.setEditable(false);
    }

    public synchronized void append (String string) {
        console.appendText(string);
        console.appendText("\n");
    }

    public synchronized void append (Exception e) {
        append(e.getLocalizedMessage());
        StackTraceElement[] element = e.getStackTrace();
        for (int i=0; i < 5; i++) {
            append(element.toString());
        }
    }
}
