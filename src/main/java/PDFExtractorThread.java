import org.apache.pdfbox.pdmodel.PDDocument;

public class PDFExtractorThread extends AsyncTask {

    private PrinterUIController printerUIController;
    private PDFExtractor document;


    public PDFExtractorThread(String url, PrinterUIController printerUIController){
        document = new PDFExtractor(url,printerUIController);
        this.printerUIController = printerUIController;
    }

    public PDDocument getDocument() {
        return document.getDocument();
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public Object doInBackground(Object[] params) {
        document.getPDF();
        return null;
    }

    @Override
    public void onPostExecute(Object params) {
        if(getDocument()!=null){
            printerUIController.stopProgress();
            printerUIController.pdfSuccess();
            printerUIController.tab.setOpacity(1.0);
        }
        else{
            printerUIController.stopProgress();
            printerUIController.pdfError();
            printerUIController.tab.setOpacity(1.0);
        }
    }

    @Override
    public void progressCallback(Object[] params) {
        System.out.println("Progress");
    }


}
