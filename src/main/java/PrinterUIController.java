import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrinterUIController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
