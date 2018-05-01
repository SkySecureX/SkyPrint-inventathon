import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Print {

    public static void printDocument(PDDocument document) {

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPageable(new PDFPageable(document));
        if (pj.printDialog()) {
            System.out.println("Printing document...");
            try {
                pj.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
