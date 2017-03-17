package marion.marguerettaz.channelmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import marion.marguerettaz.channelmessaging.Fragements.ChannelListFragment;
import marion.marguerettaz.channelmessaging.Fragements.MessageFragment;

/**
 * Created by marguerm on 23/01/2017.
 */
public class ChannelListActivity extends AppCompatActivity implements OnDownloadCompleteListener, AdapterView.OnItemClickListener {

    ListView listView;
    public static final String PREFS_NAME = "MyPrefsFile";
    private Channels ch;


    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_showChannel);
        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        HashMap<String, String> infoConnexion = new HashMap<>();
        infoConnexion.put("accesstoken",settings.getString("accesstoken",""));
        AsyncTask login = new AsyncTask(getApplicationContext(),infoConnexion ,"http://www.raphaelbischof.fr/messaging/?function=getchannels",0);
        login.setOnDownloadCompleteListener(this);
        login.execute();


    }
    @Override
    public void onDownloadComplete(String result, int requestCode) {
        Gson gson = new Gson();
        ch = gson.fromJson(result, Channels.class);

        listView.setAdapter(new ChannelAdapter(getApplicationContext(), R.layout.channel_showChannel,R.layout.row_layout, ch.channels));
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChannelListFragment fragA   = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentA_ID);
        MessageFragment fragB= (MessageFragment)getSupportFragmentManager().findFragmentById (R.id.fragmentB_ID);
        if(fragB==null|| !fragB.isInLayout()){
            Intent myIntent = new Intent(getApplicationContext(),ChannelActivity.class);
            myIntent.putExtra("id", ch.channels.get(position).channelID);
            startActivity(myIntent);
        }
        else
        {
            fragB.changeChannelId (ch.channels.get(position).channelID);
        }


    }

}
