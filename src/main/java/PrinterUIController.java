import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrinterUIController implements Initializable {

    @FXML
    AnchorPane URLTab;
    @FXML
    JFXTextField url;
    @FXML
    JFXButton submit;
    @FXML
    JFXCheckBox removeImages;

    private Alert alert;
    private Stage ProgressUI;
    private Parent ProgressRoot;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startProgress();
    }


    @FXML
    public void getURL(ActionEvent event){
        if(url.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("URL was not found");
            alert.setContentText("Insert a URL");
            alert.showAndWait();
        }

        else if(url.getText().length() < 9){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect URL");
            alert.setContentText("Please enter a valid URL");
            alert.showAndWait();
        }
        else {
            PDFGetter getter = new PDFGetter(url.getText());
            getter.start();
            try {
                submit.isDisabled();
                getter.join();

            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getter.getDocument());
            if (getter.getDocument() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unable to create PDF from URL.");
                alert.setContentText("Please try a different URL.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleDragOver(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    public void handleDrop(DragEvent event){
        List<File> files = event.getDragboard().getFiles();
    }

    public void startProgress(){
        ProgressUI = new Stage();
        try {
            ProgressRoot = FXMLLoader.load(getClass().getResource("ProgressUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProgressUI.setTitle("Loading...");
        ProgressUI.setScene(new Scene(ProgressRoot,185,185));
        ProgressUI.sizeToScene();
        ProgressUI.setResizable(false);
        ProgressUI.setAlwaysOnTop(true);
        ProgressUI.show();

    }




}
