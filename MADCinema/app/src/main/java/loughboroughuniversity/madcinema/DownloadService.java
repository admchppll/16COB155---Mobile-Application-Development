package loughboroughuniversity.madcinema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import loughboroughuniversity.madcinema.HomeActivity;
import loughboroughuniversity.madcinema.R;

public class DownloadService extends Service {
    public static String EXTRA_FILM;
    public String[] dateFilmArray = new String[4];

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

        new fetchMovieData().execute();

        return super.onStartCommand(intent, flags, startId);
    }
    public void sendNotification(String textOut) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("MAD Cinema")
                        .setContentText(textOut);

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
    public class fetchMovieData extends AsyncTask<Void, Void, String> {
        String movieDetailsURL = "http://ac-portal.uk/mad/dateFilm.php";
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String resultJson = null;

            try {
                // Construct the URL
                URL url = new URL(movieDetailsURL);

                // Create the request and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                resultJson = buffer.toString();
                return resultJson;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get data
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject filmDate = new JSONObject(s);
                JSONArray filmDates = filmDate.getJSONArray("films");

//                JSONObject film;
                for (int i = 0; i < filmDates.length(); i++) {
                    JSONObject film = filmDates.getJSONObject(i);
                    String temporary = film.getString("AddedDate");
                    dateFilmArray[i] = temporary;
                }

            } catch (JSONException e) {
                Log.d("JSON Exception", e.getLocalizedMessage());
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            String currentDate = dateFormat.format(date).toString();
            //Need if statement to find if new film released
            if (dateFilmArray.length == 0){
                sendNotification("View our wide variety of films!");
            } else {
                for (int k = 0; k < dateFilmArray.length; k++) {
                    if (dateFilmArray[k].equals(currentDate)) {
                        sendNotification("A New film has been released");
                    } else {
                        sendNotification("View our wide variety of films!");
                    }
                }
            }
        }
    }

}