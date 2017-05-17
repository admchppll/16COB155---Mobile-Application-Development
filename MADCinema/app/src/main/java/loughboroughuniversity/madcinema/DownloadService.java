package loughboroughuniversity.madcinema;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import loughboroughuniversity.madcinema.HomeActivity;
import loughboroughuniversity.madcinema.R;

public class DownloadService extends Service {
    public static String EXTRA_FILM;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        //Need if statement to find if new film released


        sendNotification();
        return super.onStartCommand(intent, flags, startId);
    }
    public void sendNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("MAD Cinema")
                        .setContentText("New movie might of been released today!");

        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.putExtra(EXTRA_FILM, "FILM");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(111,mBuilder.build());
    }

}