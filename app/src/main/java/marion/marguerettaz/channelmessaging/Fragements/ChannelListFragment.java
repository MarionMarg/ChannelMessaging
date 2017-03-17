package marion.marguerettaz.channelmessaging.Fragements;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import marion.marguerettaz.channelmessaging.AsyncTask;
import marion.marguerettaz.channelmessaging.ChannelActivity;
import marion.marguerettaz.channelmessaging.ChannelAdapter;
import marion.marguerettaz.channelmessaging.ChannelListActivity;
import marion.marguerettaz.channelmessaging.Channels;
import marion.marguerettaz.channelmessaging.OnDownloadCompleteListener;
import marion.marguerettaz.channelmessaging.R;

/**
 * Created by marguerm on 27/02/2017.
 */
public class ChannelListFragment extends Fragment implements OnDownloadCompleteListener, AdapterView.OnItemClickListener{

        ListView listView;
        public static final String PREFS_NAME = "MyPrefsFile";
        private Channels ch;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View v = inflater.inflate(R.layout.channel_list_activity_fragment, container);
            listView = (ListView) v.findViewById(R.id.listView);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
            HashMap<String, String> infoConnexion = new HashMap<>();
            infoConnexion.put("accesstoken",settings.getString("accesstoken",""));
            AsyncTask login = new AsyncTask(getActivity(),infoConnexion ,"http://www.raphaelbischof.fr/messaging/?function=getchannels",0);
            login.setOnDownloadCompleteListener(this);
            login.execute();

        }


        @Override
        public void onDownloadComplete(String result, int requestCode) {
            Gson gson = new Gson();
            ch = gson.fromJson(result, Channels.class);

            listView.setAdapter(new ChannelAdapter(getActivity(), R.layout.channel_showChannel,R.layout.row_layout, ch.channels));
            listView.setOnItemClickListener((ChannelListActivity)getActivity());


        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent myIntent = new Intent(getActivity(),ChannelActivity.class);
            myIntent.putExtra("id", ch.channels.get(position).channelID);
            startActivity(myIntent);
        }
    }


