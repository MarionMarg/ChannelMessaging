package marion.marguerettaz.channelmessaging;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by marguerm on 23/01/2017.
 */
public class ChannelListActivity extends Activity implements OnDownloadCompleteListener, AdapterView.OnItemClickListener {

    ListView listView;
    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_activity_activity);
        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        HashMap<String, String> infoConnexion = new HashMap<>();
        infoConnexion.put("accesstoken",settings.getString("accesstoken",""));
        AsyncTask login = new AsyncTask(getApplicationContext(),infoConnexion ,"http://www.raphaelbischof.fr/messaging/?function=getchannels");
        login.setOnDownloadCompleteListener(this);
        login.execute();


    }
    public void onDownloadComplete(String result) {
        Gson gson = new Gson();
        Channels ch = gson.fromJson(result, Channels.class);

        listView.setAdapter(new ChannelAdapter(getApplicationContext(), R.layout.channel_activity_activity ,R.layout.row_layout, ch.channels));
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
