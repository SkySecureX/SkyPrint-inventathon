import org.apache.pdfbox.pdmodel.PDDocument;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class PDFExtractor{

    private String url;
    private ChromeOptions options;
    private int timeout;

    private PrinterUIController printerUIController;
    private volatile PDDocument document;
    private URL pdfURL;
    private InputStream pdfStream;
    private volatile boolean pdfCreated;


    public PDFExtractor(String url, PrinterUIController printerUIController) {

        this.url = url;
        this.printerUIController = printerUIController;
        options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--silent");
        timeout = 10;
        pdfCreated = false;
    }

    public PDDocument getDocument() {
        return document;
    }

    public void getPDF(){

        printerUIController.tab.setOpacity(0.0);
        printerUIController.ringProgressIndicator.setDisable(false);
        printerUIController.ringProgressIndicator.setOpacity(1.0);
        ChromeDriver browser = new ChromeDriver(options);
        System.out.println("Browser loaded");

        browser.get("https://printfriendly.com");
        System.out.println("Page Loaded");

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

        List<WebElement> toRemove = Collections.emptyList();

        //-- Finds images that contain the flex-width class
        toRemove = browser.findElements(By.xpath("//img[contains(@class, 'flex-width')]"));
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//span[contains(@class, 'caption')]")));
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//span[contains(@class, 'credit')]")));

        //-- finds universal elements
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//a[starts-with(.,'http')]")));
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'trial-link')]")));
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*//*[contains(@class, 'trial-link')]")));
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*//*[contains(@class, 'count-link')]")));
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'count-link')]")));
        toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'article-link')]")));


        //-- News Week Script
        try {
            toRemove.add(browser.findElement(By.xpath("//*[@id=\"pf-content\"]/div/p[7]")));
            toRemove.add(browser.findElement(By.xpath("//*[@id=\"pf-content\"]/div/div[2]/div[2]/div[1]/div[2]/a")));
            toRemove.add(browser.findElement(By.xpath("//*[@id=\"pf-content\"]/div/div[2]/div[1]")));
            toRemove.add(browser.findElement(By.xpath("//*[@id=\"pf-content\"]/div/div[2]/div[2]")));
        }catch(WebDriverException e){
            System.out.println(e.getMessage());
        }

        //--Guardian Script -- removes submetas
        try {
            toRemove.addAll(browser.findElements(By.className("submeta__label")));
            toRemove.addAll(browser.findElements(By.className("submeta__section-labels")));
            toRemove.addAll(browser.findElements(By.className("submeta__links")));
            toRemove.addAll(browser.findElements(By.className("submeta__link-item")));
            toRemove.addAll(browser.findElements(By.className("submeta__syndication\n")));
        }catch(WebDriverException e){
            System.out.println(e.getMessage());
        }


        //-- CNN Script
        try{

            //-- Must Watched, Just Watched, Video Titles removed
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'js-video__end-slate__replay-text video__end-slate__replay-text')]")));
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'video__end-slate__replay-text')]")));
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'video__end-slate__tertiary-title')]")));
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'video__end-slate__tertiary-title')]")));
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'cd__headline')]")));
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'cd__headline-title')]")));
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'el__storyelement__header')]")));
            toRemove.addAll(browser.findElements(By.xpath("//*[@id='pf-body']//*[contains(@class, 'el__storyelement__gray')]")));


        }
        catch (WebDriverException e){
            System.out.println(e.getMessage());
        }






        for (WebElement element : toRemove) {
            try {
                element.click();
                System.out.println("Clicked element: " + element.getText());
            } catch (WebDriverException e) {
                System.out.println(e.getMessage());
            }
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

        // Get the download button
        WebElement button = browser.findElement(By.className("pdf-download"));

        // Wait until the download button turns green so we can download it.
        new WebDriverWait(browser, timeout).until(ExpectedConditions.presenceOfElementLocated(By.xpath(dlBtnXpath + "[contains(@class, 'btn-success')]")));

        // Get the download link for the PDF.
        String pdfLocation = button.getAttribute("href");

        browser.close();

        System.out.println(pdfLocation);


        byte[] ba1 = new byte[1024];
        FileOutputStream pdfOutputStream;
        int baLength;

        try{
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
        pdfCreated = true;


        try {
            document = PDDocument.load(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
