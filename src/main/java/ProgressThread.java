import javafx.application.Platform;


public class ProgressThread extends Thread {

    RingProgressIndicator progressIndicator;
    int progress = 0;

    public ProgressThread(RingProgressIndicator progressIndicator){
        this.progressIndicator = progressIndicator;
    }

    @Override
    public void run(){

        while(true){
            try{
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            progressIndicator.setProgress(progress);

            progress+=1;
            if(progress>100){
                Thread.currentThread().interrupt();
                return;
            }

        }

    }

}
