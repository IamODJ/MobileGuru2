package mobileguru.ganeshkhind.kvgk.mobileguru;

/**
 * Created by Vishakha on 21-08-2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);// Set the volume of played media to maximum.
                audioManager.setStreamVolume (
                        AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        0);
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    public void onBackPressed() {

    }
}