package kr.theminjoo.app;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar hj and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    String URL_LOAD;
    private static final String TAG = SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Intent intent =  getIntent();
        Bundle bundle = intent.getExtras();
        if( bundle != null){
            if(bundle.getString("url") != null && !bundle.getString("url").equalsIgnoreCase("")) {


                 URL_LOAD =  bundle.getString("url");



            }
        }else{

             URL_LOAD =  "";
        }

        Log.d(TAG, "surl: " + URL_LOAD);
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("URL_LOAD",URL_LOAD);
        startActivity(intent);
       finish();

    } 
}
