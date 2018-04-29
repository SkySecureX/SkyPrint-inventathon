import com.jfoenix.controls.JFXTabPane;
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
    @FXML
    JFXTabPane tab;

    private Alert alert;
    private ProgressThread progressThread;
    private CircularProgressIndicator indicator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    public void getPDF(ActionEvent event){

        if(url.getText().isEmpty()) {
            emptyURLError();
        }


        else if(url.getText().length() < 9) {
            incorrectURLError();
        }

        else {

            PDFExtractor getter = new PDFExtractor(url.getText());
            getter.start();
            /*
            progressThread = new ProgressThread(tab);
            progressThread.execute();
            */

            try {
                getter.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (getter.getDocument() == null) {
                pdfError();
            }
            else {
                pdfSuccess();
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


    public void emptyURLError(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("URL was not found");
        alert.setContentText("Insert a URL");
        alert.showAndWait();
    }

    public void incorrectURLError(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Incorrect URL");
        alert.setContentText("Please enter a valid URL");
        alert.showAndWait();
    }

    public void pdfError(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Unable to create PDF from URL.");
        alert.setContentText("Please try a different URL.");
        alert.showAndWait();
    }

    public void pdfSuccess(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Created new PDF from URL");
        alert.setContentText("It is saved in " + System.getProperty("user.dir") + "\\download.pdf");
        alert.showAndWait();
    }

}
