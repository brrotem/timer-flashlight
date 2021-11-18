package com.rotembr.timerlight;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;

import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG ="MainActivityFragment" ;
    private ToggleButton ten_button,twenty_button,thirty_button, forty_button,fifty_button,sixty_button;
    private ImageButton on_button;
    private  View  mview;
    private Camera camera;
    private int mTimer=0;
    private boolean hasFlash;
    android.hardware.Camera.Parameters params;
    MediaPlayer mp;
    private Boolean isFlashOn =false;
    private CountDownTimer cdt;
    private TextView TimeText;
    private NotificationManager mNotifyMgr;
    boolean timerRun=false;
    int mNotificationId = 001;
    /*public MainActivityFragment() {

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_main, container, false);
        return mview;



    }

    @Override
    public void onStart() {
        ten_button= (ToggleButton) mview.findViewById(R.id.ten_button);
        twenty_button= (ToggleButton) mview.findViewById(R.id.twenty_button);
        thirty_button= (ToggleButton) mview.findViewById(R.id.thirty_button);
        forty_button= (ToggleButton) mview.findViewById(R.id.forty_button);
        fifty_button= (ToggleButton) mview.findViewById(R.id.fifty_button);
        sixty_button= (ToggleButton) mview.findViewById(R.id.sixty_button);
        on_button= (ImageButton) mview.findViewById(R.id.onButton);
        TimeText =(TextView) mview.findViewById(R.id.TimeText);
        TimeText.setText("00:00");

        ten_button.setOnClickListener(this);
        twenty_button.setOnClickListener(this);
        thirty_button.setOnClickListener(this);
        forty_button.setOnClickListener(this);
        fifty_button.setOnClickListener(this);
        sixty_button.setOnClickListener(this);
        on_button.setOnClickListener(this);

        getCamera();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
       // Log.i(TAG,"on click");
        switch (v.getId()){
            case (R.id.onButton): {
                if (isFlashOn) {
                    Log.i(TAG, "off Button");
                    turnOffFlash();
                }
                else
                {
                    Log.i(TAG, "on Button");
                    turnOnFlash();
                    startTimer();
                }
            }
            case (R.id.ten_button):
            case (R.id.twenty_button):
            case (R.id.thirty_button):
            case (R.id.forty_button):
            case (R.id.fifty_button):
            case (R.id.sixty_button):
                updateTimer();

        }


    }



    /*
    * Turning On flash
    */

    private void turnOnFlash() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try{

                CameraManager manager = (CameraManager) getActivity().getBaseContext().getSystemService(Context.CAMERA_SERVICE);
                String[] list = manager.getCameraIdList();
                manager.setTorchMode(list[0], true);
            }
            catch (Exception cae){
                Log.e(TAG, cae.getMessage());
                cae.printStackTrace();
            }
        }
        isFlashOn = true;
        // changing button/switch image
        toggleButtonImage();
        SharedPreferences preference= getDefaultSharedPreferences(getContext());
        if(preference.getBoolean("notifications_new_message",true))
        {
            CreateNotification();
        }

    }
    private void CreateNotification()
    {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notifiaction_content_text));

        // Sets an ID for the notification

// Gets an instance of the NotificationManager service

         mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    private void updateNotification()
    {

// Gets an instance of the NotificationManager service

    }
    private void dismissNotification()
    {

        mNotifyMgr.cancel(mNotificationId);


    }


    private void turnOffFlash() {
        if (isFlashOn) {

            isFlashOn = false;

            // changing button/switch image

            if (cdt!=null) cdt.cancel();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                manager.setTorchMode(manager.getCameraIdList()[0], false);
            } catch (Exception cae) {
                Log.e(TAG, cae.getMessage());
                cae.printStackTrace();
            }
        }
        toggleButtonImage();
            dismissNotification();
        }
    }

    private void getCamera() {



        if (camera == null) {
            try {
                this.camera = Camera.open(0);
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e(TAG,"Camera Error. Failed to Open. Error: "+e.getMessage());
            }
        }
    }
    private void toggleButtonImage() {
        if(isFlashOn)
        {
            on_button.setImageResource(R.drawable.button_turn_on_01);
        }
        else
        {
            on_button.setImageResource(R.drawable.button_log_off_01);
        }

    }

    private void startTimer() {
        final SharedPreferences preference= getDefaultSharedPreferences(getContext());
        final boolean countDown= preference.getBoolean("count_down",true);
        timerRun=true;
       // updateTimer();
        if (mTimer != 0) {
            cdt = new CountDownTimer(mTimer * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    String remainingTime;
                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    if (countDown) {
                         remainingTime = String.format("%02d:%02d", millisUntilFinished / 60000, millisUntilFinished / 1000 % 60);
                    }
                    else
                    {
                         remainingTime = String.format("%02d:%02d", (mTimer-millisUntilFinished /1000) /60,(mTimer- millisUntilFinished / 1000)%60);
                    }
                    updateText(remainingTime );
                }

                public void onFinish() {
                    Log.i(TAG, "DONE");
                    timerRun=false;
                    turnOffFlash();
                    updateTimer();
                    if (preference.getBoolean("close_app",true))
                    getActivity().finish();

                    // mTextField.setText("done!");


                }
            }.start();
        }
    }

    void updateTimer()
    {
        if (timerRun) return;
        mTimer=0;
        if (ten_button.isChecked()) mTimer += 10;
        if (twenty_button.isChecked()) mTimer += 20;
        if (thirty_button.isChecked()) mTimer += 30;
        if (forty_button.isChecked()) mTimer += 40;
        if (fifty_button.isChecked()) mTimer += 50;
        if (sixty_button.isChecked()) mTimer += 60;

        String result = String.format("%02d:%02d", mTimer / 60, mTimer % 60);
        Log.i(TAG, "mTimer=" + mTimer+"string="+result);
        TimeText.setText(result);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(this.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.last_timer), mTimer);
        editor.apply();

    }

    void updateText(long l) {

        String result = String.format("%02d:%02d", l/60000, l % 60000);
        TimeText.setText(result);
    }

    void updateText(String remainTime ) {


        TimeText.setText(remainTime);
    }
}
