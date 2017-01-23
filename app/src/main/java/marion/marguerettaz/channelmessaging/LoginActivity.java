package marion.marguerettaz.channelmessaging;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textId = (EditText) findViewById(R.id.identifiant);
        textMdp = (EditText) findViewById(R.id.mdp);
        textViewId = (TextView) findViewById(R.id.textIdentifian);
        textViewMdp = (TextView) findViewById(R.id.textViewMdp);
    }


    //asynctask puis crée evenement
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.valider)
        {

            HashMap<String, String> infoConnexion = new HashMap<>();
            infoConnexion.put("ide",textId.getText().toString());
            infoConnexion.put("mdp",textMdp.getText().toString());
            AsyncTask login = new AsyncTask(getApplicationContext(), infoConnexion);
            login.setOnDownloadCompleteListener(this);
            login.execute();
        }
    }

    @Override
    public void onDownloadComplete(String result) {
        Gson gson = new Gson();
        Connect connect1 = gson.fromJson(result, Connect.class);
        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();

    }
}
