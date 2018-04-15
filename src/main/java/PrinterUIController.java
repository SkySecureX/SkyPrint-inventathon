import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrinterUIController implements Initializable {

    @FXML
    AnchorPane URLTab;
    @FXML
    JFXTextField url;

    private Alert alert;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void getURL(ActionEvent event){
        if(url.getText().equals("")){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("URL was not found");
            alert.setContentText("Insert a URL");
            alert.showAndWait();
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




}
