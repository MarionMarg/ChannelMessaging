package marion.marguerettaz.channelmessaging;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by marguerm on 30/01/2017.
 */
public class AsyncTaskStorage extends android.os.AsyncTask<Long, Integer, String> {

    private Context myContext;
    public String fileURL;
    public String fileName;

    public ArrayList<OnDownloadCompleteListener> listeners = new ArrayList<>();

    public AsyncTaskStorage(Context myContext, String url, String name) {
        this.myContext = myContext;
        this.fileURL = url;
        this.fileName = name;
    }



    public String downloadFromUrl(String fileURL, String fileName) {
        try {
            URL url = new URL(fileURL);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fileName);
            file.createNewFile();
            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();
            /* Define InputStreams to read from the URLConnection.*/
            InputStream is = ucon.getInputStream();
            /* Read bytes to the Buffer until there is nothing more to
            read(- 1) and write on the fly in the file.*/
            FileOutputStream fos = new FileOutputStream(file);
            final int BUFFER_SIZE = 23 * 1024;
            BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
            byte[] baf = new byte[BUFFER_SIZE];
            int actual = 0;
            while (actual != -1) {
                fos.write(baf, 0, actual);
                actual = bis.read(baf, 0, BUFFER_SIZE);
            }
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            return "Nok";
        }


    }


    @Override
    protected String doInBackground(Long... params) {
        return downloadFromUrl(fileURL, fileName);
    }

    @Override
    protected void onPostExecute(String result)
    {
        for (OnDownloadCompleteListener listener : listeners)
        {
            listener.onDownloadComplete(result, 0);
        }
    }

    public void setOnDownloadCompleteListener(OnDownloadCompleteListener listener)
    {
        listeners.add(listener);
    }



}
