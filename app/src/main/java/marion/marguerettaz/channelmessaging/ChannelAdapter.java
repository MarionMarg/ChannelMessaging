package marion.marguerettaz.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by marguerm on 23/01/2017.
 */
public class ChannelAdapter extends ArrayAdapter<Channel>{

    TextView textView;
    TextView textView2;
    //private final Channel values;
    public ChannelAdapter(Context context, int resource, int textViewResourceId, List<Channel> objects) {
        super(context, resource, textViewResourceId, objects);
        //this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        textView = (TextView) rowView.findViewById(R.id.textView);
        textView.setText(getItem(position).name);
        textView2 = (TextView) rowView.findViewById(R.id.textView2);
        textView2.setText("Nombre d'utilisateurs connectés : " + getItem(position).connectedusers);
        return rowView;
    }
}
