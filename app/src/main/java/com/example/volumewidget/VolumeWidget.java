package com.example.volumewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class VolumeWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager mgr, int[] ids) {
        for (int id : ids) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.volume_widget);
            views.setOnClickPendingIntent(R.id.btnUp, actionIntent(context, "UP", 1));
            views.setOnClickPendingIntent(R.id.btnDown, actionIntent(context, "DOWN", 2));
            mgr.updateAppWidget(id, views);
        }
    }

    private PendingIntent actionIntent(Context context, String action, int code) {
        Intent intent = new Intent(context, VolumeWidget.class).setAction(action);
        return PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (!"UP".equals(action) && !"DOWN".equals(action)) return;
        try {
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int dir = "UP".equals(action) ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER;
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, dir, AudioManager.FLAG_SHOW_UI);
            int now = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            Toast.makeText(context, "Media volume: " + now, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
          }
