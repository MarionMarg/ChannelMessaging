package marion.marguerettaz.channelmessaging;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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
    ImageView logo;
    Handler mHandlerTada;
    Handler mHandlerAnimLeft;
    int mShortDelay;
    int mReallyShortDelay;

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        textId = (EditText) findViewById(R.id.identifiant);
        textMdp = (EditText) findViewById(R.id.mdp);
        textId.setText("mmarg");
        textMdp.setText("marionmarguerettaz");
        btnValider = (Button) findViewById(R.id.valider);
        btnValider.setOnClickListener(this);


        logo = (ImageView) findViewById(R.id.imageView2);

        mHandlerTada = new Handler(); // android.os.handler
        mShortDelay = 4000; //milliseconds

        mHandlerTada.postDelayed(new Runnable(){
            public void run(){
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(5)
                        .playOn(findViewById(R.id.imageView2));
                mHandlerTada.postDelayed(this, mShortDelay);
            }
        }, mShortDelay);
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
            mReallyShortDelay = 300;
            mHandlerAnimLeft = new Handler();
            mHandlerAnimLeft.postDelayed(new Runnable(){
                public void run(){
                    Intent myIntent = new Intent(getApplicationContext(),ChannelListActivity.class);
                    startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, logo, "logo").toBundle());

                }
            }, mReallyShortDelay);

            /*
            Animation animSlideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
            btnValider.startAnimation(animSlideLeft);*/

        }
        else
            Toast.makeText(LoginActivity.this, "Mauvais identifiants", Toast.LENGTH_SHORT).show();

    }



}
