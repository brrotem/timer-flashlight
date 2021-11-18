package com.rotembr.timerlight;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.RemoteViews;



/**
 * Implementation of App Widget functionality.
 */
public class TimerLightWidget extends AppWidgetProvider {
    public static String WIDGET_BUTTON ="android.appwidget.action.WIDGET_BUTTON";
    public static String TIMER_UPDATE ="android.appwidget.action.TIMER_UPDATE";
     public static Boolean isFlashOn=false;
    private static CountDownTimer cdt;
    static boolean timerRun = true;
    public static RemoteViews views;

    private static String remainingTime;
    static int mTimer;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
       // mTimer = sharedPref.getInt(context.getString(R.string.last_timer), 10);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_light_widget);
        //views.setTextViewText(R.id.textView2, remainingTime);
        views.setOnClickPendingIntent(R.id.appwidget_text,
                getPendingSelfIntent(context, WIDGET_BUTTON));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action) {

        Intent intent = new Intent(context, TimerLightWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (WIDGET_BUTTON.equals(intent.getAction())) {

            if (isFlashOn){
                turnOffFlash(context);
            }else
            {
                turnOnFlash(context);
            }

        }
        //TODO update timer on widget
        /*else if (TIMER_UPDATE.equals(intent.getAction())){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_light_widget);
            views.setTextViewText(R.id.textView2, remainingTime);
            // Instruct the widget manager to update the widget
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            mgr.updateAppWidget(appWidgetId, views);
        }*/



    }


    private void turnOnFlash(Context context) {
        isFlashOn = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try{

                CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String[] list = manager.getCameraIdList();
                manager.setTorchMode(list[0], true);
            }
            catch (Exception cae){
                Log.e("'widget'", cae.getMessage());
                cae.printStackTrace();
            }
        }
        startTimer(context);


    }

    private void turnOffFlash(Context context) {
        if (isFlashOn) {

            isFlashOn = false;

            // changing button/switch image
            cdt.cancel();



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                    manager.setTorchMode(manager.getCameraIdList()[0], false);
                } catch (Exception cae) {
                    Log.e("widget", cae.getMessage());
                    cae.printStackTrace();
                }
            }

        }
    }




    private void startTimer(Context context) {
        final SharedPreferences preference= getDefaultSharedPreferences(context);
        final boolean countDown= preference.getBoolean("count_down",true);
        //final int mTimer= preference.getInt(context.getString(R.string.last_timer),10);
         //TO DO configure the timer
        int mTimer=10;
        // updateTimer();
        if (mTimer != 0) {
            cdt = new CountDownTimer(mTimer * 1000, 1000) {

                public void onTick(long millisUntilFinished) {

                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    if (countDown) {
                        remainingTime = String.format("%02d:%02d", millisUntilFinished / 60000, millisUntilFinished / 1000 % 60);
                    }
                    else
                    {
                        remainingTime = String.format("%02d:%02d", (mTimer-millisUntilFinished /1000) /60,(mTimer- millisUntilFinished / 1000)%60);
                    }
                    updateText(remainingTime,context);
                }

                public void onFinish() {
                    Log.i("widget", "DONE");
                    timerRun=false;
                    turnOffFlash(context);


                    // mTextField.setText("done!");


                }
            }.start();
        }
    }

    void updateText(String remainTime,Context context ) {
        Log.i("widget", "remainTime"+remainTime);
        //
       /* RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_light_widget);
        views.setTextViewText(R.id.textView2, remainTime);
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        mgr.updateAppWidget(appWidgetId, views);*/

    }
}

