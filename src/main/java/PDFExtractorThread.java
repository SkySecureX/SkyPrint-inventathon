import java.io.File;

public class PDFExtractorThread extends AsyncTask {

    private PrinterUIController printerUIController;
    private PDFExtractor extractor;


    public PDFExtractorThread(String url, PrinterUIController printerUIController){
        extractor = new PDFExtractor(url, printerUIController);
        this.printerUIController = printerUIController;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public Object doInBackground(Object[] params) {
        extractor.getPDF();
        return null;
    }

    @Override
    public void onPostExecute(Object params) {
        File document = extractor.getDocument();
        String docName = extractor.getDocumentName();
        if (document != null) {
            printerUIController.tab.setOpacity(1.0);
            printerUIController.stopProgress();
            printerUIController.pdfSuccess(document, docName);
        }
        else{
            printerUIController.tab.setOpacity(1.0);
            printerUIController.stopProgress();
            printerUIController.pdfError();
        }
    }

    @Override
    public void progressCallback(Object[] params) {
        System.out.println("Progress");
    }


}
