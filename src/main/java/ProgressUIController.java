import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressUIController implements Initializable {

    @FXML
    AnchorPane root;
    private RingProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressIndicator = new RingProgressIndicator();
        progressIndicator.makeIndeterminate();
        root.getChildren().add(progressIndicator);
        new ProgressThread(progressIndicator).start();
    }
}
