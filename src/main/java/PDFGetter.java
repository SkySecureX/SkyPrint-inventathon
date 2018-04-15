import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.io.RandomAccessInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDObjectStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class PDFGetter extends Thread {

    private String url;
    private ChromeOptions options;
    private int timeout;
    private volatile PDDocument document; // Guaranteed to appear to all threads.

    public PDFGetter(String url) {
        this.url = url;
        timeout = 10;

        options = new ChromeOptions();
        options.setHeadless(false);
        options.addArguments("--silent");
    }

    public PDDocument getDocument() {
        return document;
    }

    @Override
    public void run() {
        ChromeDriver browser = new ChromeDriver(options);

        browser.get("https://printfriendly.com");

        // Wait until the page loads
        new WebDriverWait(browser, timeout).until(ExpectedConditions.titleContains("PDF"));

        // Get the input box for urls
        WebElement input = browser.findElement(By.name("url"));

        // Send the url for the link and hit return!
        input.sendKeys(url);
        input.sendKeys(Keys.RETURN);

        // Wait until the outputted pdf is there
        try {
            new WebDriverWait(browser, timeout).until(ExpectedConditions.presenceOfElementLocated(By.id("pf-body")));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        List<WebElement> toRemove = browser.findElements(By.xpath("//img[contains(@class, 'flex-width')]"));

        List<WebElement> links =  browser.findElements(By.xpath("//*[@id='pf-body']//a"));

        toRemove.addAll(links);

        try {
            for (WebElement element : toRemove) {
                element.click();
            }
        } catch (WebDriverException e) {
            browser.close();
            Thread.currentThread().interrupt();
            return;
        }
        // Click on the <Print PDF> button
        browser.findElement(By.id("w-pdf")).click();

        // wait until the popup for the download button comes up
        new WebDriverWait(browser, timeout).until(ExpectedConditions.presenceOfElementLocated(By.name("pdf_iframe")));

        // switch to the popup
        browser.switchTo().frame("pdf_iframe");

        String dlBtnXpath = "//*[@id='pf-dialog-pdf']/div/div[1]/a";

        try {
            Thread.sleep(8000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Wait until the button to download is found.
//        new WebDriverWait(browser, timeout).until(ExpectedConditions.presenceOfElementLocated(By.xpath(dlBtnXpath)));

        // Get the download button
        WebElement button = browser.findElement(By.className("pdf-download"));
//        System.out.println("Found Download button");

        // Wait until the download button turns green so we can download it.
        new WebDriverWait(browser, timeout).until(ExpectedConditions.presenceOfElementLocated(By.xpath(dlBtnXpath + "[contains(@class, 'btn-success')]")));

        // Get the download link for the PDF.
        String pdfLocation = button.getAttribute("href");

        browser.close();
        System.out.println(pdfLocation);

        URL pdfURL = null;
        InputStream pdfStream = null;
        byte[] ba1 = new byte[1024];
        FileOutputStream pdfOutputStream;
        int baLength;
        try {


            pdfURL = new URL(pdfLocation);
            pdfStream = pdfURL.openStream();
            pdfOutputStream = new FileOutputStream("download.pdf");


            while ((baLength = pdfStream.read(ba1)) != -1) {
                pdfOutputStream.write(ba1, 0, baLength);
            }

            pdfOutputStream.flush();
            pdfOutputStream.close();

            pdfStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File pdfFile = new File("download.pdf");

        try {
            document = PDDocument.load(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        GetRequest pdf = Unirest.get(pdfLocation);
//        HttpResponse<InputStream> inputStreamHttpResponse = null;
//        try {
//            inputStreamHttpResponse = pdf.asBinary();
//            System.out.println("Input stream response " + inputStreamHttpResponse);
//        }catch (UnirestException e) {
//            e.printStackTrace(System.out);
//        }
//
//        InputStream pdfStream = inputStreamHttpResponse.getBody();
//
//        try {
//            for(int i = 0; i < 200; i++) {
//
//                int x = pdfStream.read();
//                System.out.println("pdfStream.read() = " + x);
//            }
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//        }
//        ProcessBuilder builder = new ProcessBuilder("python", "sel.py", pdfLocation);
//        try {
//            Process py = builder.start();
//            py.waitFor();
//        }catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    }
}
