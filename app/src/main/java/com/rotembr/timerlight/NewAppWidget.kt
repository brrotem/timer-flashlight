package com.rotembr.timerlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

const val WIDGET_BUTTON ="android.appwidget.action.WIDGET_BUTTON";
/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {



    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }


    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent)
        Log.d("Rotem","on recive"+intent.action);
        if (WIDGET_BUTTON.equals(intent.action)) {
            Log.d("Rotem","on recive inside if");
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val remoteViews: RemoteViews
            val watchWidget: ComponentName
            remoteViews = RemoteViews(context.packageName, R.layout.new_app_widget)
            watchWidget = ComponentName(context, NewAppWidget::class.java)
            remoteViews.setTextViewText(R.id.newappwidget_text, "TESTING")
            appWidgetManager.updateAppWidget(watchWidget, remoteViews)
        }
    }

    protected fun getPendingSelfIntent(context: Context?, action: String?): PendingIntent? {
        Log.d("Rotem","getPendingSelfIntent");
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    //val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    //val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    //views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    //appWidgetManager.updateAppWidget(appWidgetId, views)

    val remoteViews: RemoteViews
    remoteViews = RemoteViews(context.packageName, R.layout.new_app_widget)
    val intent = Intent(WIDGET_BUTTON)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    remoteViews.setOnClickPendingIntent(R.id.newappwidget_text, pendingIntent)
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
}

