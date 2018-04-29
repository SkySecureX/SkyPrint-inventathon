
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    JFXButton start;
    @FXML
    ImageView logo;


    private Stage PrinterUI;
    private Parent PrinterRoot = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        logo.setImage(new Image("SkyPrint-Logo.png"));
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
            new FadeInLeftTransition(start).playFromStart();
            new FadeInRightTransition(logo).playFromStart();
        });

    }
}


