package marion.marguerettaz.channelmessaging;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogRecord;

import android.support.v4.content.FileProvider;

/**
 * Created by marguerm on 27/01/2017.
 */
public class ChannelActivity extends Activity implements  OnDownloadCompleteListener, View.OnClickListener, UploadFileToServer.OnUploadFileListener {

    private static final int PICTURE_REQUEST_CODE = 1;
    private static final int INTENT_PHOTO = 0;
    public int id;
    public Handler handler = new Handler();
    public static final String PREFS_NAME = "MyPrefsFile";
    public ListView listViewMessage;
    public Button valider;
    public EditText messageAEnvoyer;
    public Button photoBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_activity_fragment);


        listViewMessage = (ListView) findViewById(R.id.listViewMessages);
        valider = (Button) findViewById(R.id.valider);
        messageAEnvoyer = (EditText) findViewById(R.id.editTextMessage);
        valider.setOnClickListener((View.OnClickListener) this);
        photoBtn = (Button) findViewById(R.id.buttonPhoto);
        photoBtn.setOnClickListener((View.OnClickListener)this);


        id = getIntent().getIntExtra("id", 0);

        Runnable r = new Runnable() {
            public void run() {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                HashMap<String, String> infoConnexion = new HashMap<>();
                infoConnexion.put("accesstoken",settings.getString("accesstoken",""));
                infoConnexion.put("channelid",String.valueOf(id));
                AsyncTask login = new AsyncTask(getApplicationContext(),infoConnexion ,"http://www.raphaelbischof.fr/messaging/?function=getmessages", 2);
                login.setOnDownloadCompleteListener(ChannelActivity.this);
                login.execute();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r,1000);

    }

    @Override
    public void onDownloadComplete(String result, int requestCode) {
        Gson gson = new Gson();
        if(requestCode == 2) {
            Messages messages = gson.fromJson(result, Messages.class);
            listViewMessage.setAdapter(new ChannelMessageAdapter(getApplicationContext(), R.layout.row_layout2, messages.messages));
        }
        else if (requestCode == 1){
            MessageAEnvoyer messageEnvoye = gson.fromJson(result, MessageAEnvoyer.class);
            if(messageEnvoye.code == 200)
                Toast.makeText(ChannelActivity.this, "Message envoye", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ChannelActivity.this, "Message non envoye", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.valider)
        {
            id = getIntent().getIntExtra("id", 0);
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            HashMap<String, String> infoConnexion = new HashMap<>();
            infoConnexion.put("accesstoken",settings.getString("accesstoken",""));
            infoConnexion.put("channelid",String.valueOf(id));
            infoConnexion.put("message", messageAEnvoyer.getText().toString());
            AsyncTask login = new AsyncTask(getApplicationContext(), infoConnexion, "http://www.raphaelbischof.fr/messaging/?function=sendmessage",1);
            login.setOnDownloadCompleteListener(this);
            login.execute();
            messageAEnvoyer.setText("");
        }
        else if(v.getId() == R.id.buttonPhoto)
        {
            File file = new File(Environment.getExternalStorageDirectory()+"/img.jpg");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri = FileProvider.getUriForFile(ChannelActivity.this, ChannelActivity.this.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, PICTURE_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case PICTURE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    File file = new File(Environment.getExternalStorageDirectory()+"/img.jpg");
                    try {
                        resizeFile(file, this);
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("accesstoken", settings.getString("accesstoken","")));
                        params.add(new BasicNameValuePair("channelid",String.valueOf(id)));
                        UploadFileToServer pictures = new UploadFileToServer(this,file.getPath(), params, this);
                        pictures.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }




    //decodes image and scales it to reduce memory consumption
    private void resizeFile(File f,Context context) throws IOException {
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

        //The new size we want to scale to
        final int REQUIRED_SIZE=400;

        //Find the correct scale value. It should be the power of 2.
        int scale=1;
        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
            scale*=2;

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        int i = getCameraPhotoOrientation(context, Uri.fromFile(f),f.getAbsolutePath());
        if (o.outWidth>o.outHeight)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(i); // anti-clockwise by 90 degrees
            bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
        }
        try {
            f.delete();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) throws IOException {
        int rotate = 0;
        context.getContentResolver().notifyChange(imageUri, null);
        File imageFile = new File(imagePath);
        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        return rotate;
    }

    @Override
    public void onResponse(String result) {
        Toast.makeText(ChannelActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(IOException error) {
        Toast.makeText(ChannelActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
    }
}
