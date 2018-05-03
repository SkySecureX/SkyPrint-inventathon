import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrinterUIController implements Initializable {

    @FXML
    AnchorPane UI;
    @FXML
    AnchorPane URLTab;
    @FXML
    JFXTextField url;
    @FXML
    JFXTabPane tab;


    private Alert alert;
    private PDFExtractorThread pdfExtractorThread;
    RingProgressIndicator ringProgressIndicator;



    @Override
    public void initialize(URL location, ResourceBundle resources) { startProgress(); }

    @FXML
    public void getPDF(ActionEvent event){

        if(url.getText().isEmpty()) { emptyURLError(); }

        else if(url.getText().length() < 9) { incorrectURLError(); }
        else {
            pdfExtractorThread = new PDFExtractorThread(url.getText(),this);
            pdfExtractorThread.execute();
        }
    }

    @FXML
    public void handleDragOver(DragEvent event){
        if (event.getDragboard().hasFiles())
            event.acceptTransferModes(TransferMode.ANY);
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

    public void startProgress(){
        ringProgressIndicator = new RingProgressIndicator();
        ringProgressIndicator.setRingWidth(500);
        ringProgressIndicator.makeIndeterminate();
        ringProgressIndicator.setLayoutX(260);
        ringProgressIndicator.setLayoutY(215);
        UI.getChildren().add(ringProgressIndicator);
        ringProgressIndicator.setOpacity(0.0);
        ringProgressIndicator.setDisable(true);
    }

    public void stopProgress(){
        ringProgressIndicator.setOpacity(0.0);
        ringProgressIndicator.setDisable(true);
    }

    public void incorrectURLError(){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Incorrect URL");
        alert.setContentText("Please enter a valid URL");
        alert.showAndWait();
    }

    public void pdfError(){
        alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Unable to create PDF from URL.");
        alert.setContentText("Please try a different URL.");
        alert.showAndWait();
    }

    public void pdfSuccess(PDDocument document) {
        String pdfLocation = System.getProperty("user.dir") + File.separator + "download.pdf";
        alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Created new PDF from URL");
        alert.setContentText("It is saved in " + pdfLocation);
        alert.showAndWait();
        System.out.println(pdfLocation);
        SwingUtilities.invokeLater(() -> {
            Print.printDocument(document);
        });

    }
}
