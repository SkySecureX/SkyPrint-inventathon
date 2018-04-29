import com.jfoenix.controls.JFXTabPane;

public class ProgressThread extends AsyncTask {

    JFXTabPane tab;

    public ProgressThread(JFXTabPane tab){
        this.tab = tab;

    }
    @Override
    public void onPreExecute() {

    }

    @Override
    public Object doInBackground(Object[] params) {
        tab.setOpacity(0.0);
        return null;
    }

    @Override
    public void onPostExecute(Object params) {

    }

    @Override
    public void progressCallback(Object[] params) {

    }
}
