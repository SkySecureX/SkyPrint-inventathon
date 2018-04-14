import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    JFXButton start;

    @FXML
    Label label;

    Stage PrinterUI;
    Parent PrinterRoot = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start();
    }


    @FXML
    public void getStarted(ActionEvent event) throws Exception{

        PrinterUI = new Stage();
        try {
            PrinterRoot = FXMLLoader.load(getClass().getResource("PrinterUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrinterUI.setTitle("SkyPrint");
        PrinterUI.setScene(new Scene(PrinterRoot,700,600));
        PrinterUI.setResizable(false);
        PrinterUI.show();

        Stage sourceStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sourceStage.close();

    }

    public void start(){
        Platform.runLater(()->{
            new sample.FadeInTransition(label).play();
            new sample.FadeInTransition(start).play();
        });
    }
}


