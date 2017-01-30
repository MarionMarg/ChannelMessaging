package marion.marguerettaz.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by marguerm on 20/01/2017.
 */
public class LoginActivity extends Activity implements View.OnClickListener, OnDownloadCompleteListener {

    EditText textId;
    EditText textMdp;
    TextView textViewId;
    TextView textViewMdp;
    Button btnValider;

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textId = (EditText) findViewById(R.id.identifiant);
        textMdp = (EditText) findViewById(R.id.mdp);
        textId.setText("mmarg");
        textMdp.setText("marionmarguerettaz");
        textViewId = (TextView) findViewById(R.id.textIdentifian);
        textViewMdp = (TextView) findViewById(R.id.textViewMdp);
        btnValider = (Button) findViewById(R.id.valider);
        btnValider.setOnClickListener(this);
    }


    //asynctask puis crée evenement
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.valider)
        {

            HashMap<String, String> infoConnexion = new HashMap<>();
            infoConnexion.put("username",textId.getText().toString());
            infoConnexion.put("password",textMdp.getText().toString());
            AsyncTask login = new AsyncTask(getApplicationContext(), infoConnexion, "http://www.raphaelbischof.fr/messaging/?function=connect", 01);
            login.setOnDownloadCompleteListener(this);
            login.execute();
        }
    }

    @Override
    public void onDownloadComplete(String result, int requestCode) {
        Gson gson = new Gson();
        Connect connect1 = gson.fromJson(result, Connect.class);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("accesstoken", connect1.accesstoken);
        editor.commit();
        //Pour récupérer les settings.getString("accesstoken","");
        if(connect1.code == 200) {
            Intent myIntent = new Intent(getApplicationContext(),ChannelListActivity.class);
            startActivity(myIntent);
        }
        else
            Toast.makeText(LoginActivity.this, "Mauvais identifiants", Toast.LENGTH_SHORT).show();
    }


}
