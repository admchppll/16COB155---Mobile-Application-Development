package loughboroughuniversity.madcinema;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Array to hold list of locations
    public ArrayList<LocationItem> locationArray = new ArrayList<LocationItem>();
    public ArrayList<FilmObject> allFilmObjectsArray = new ArrayList<FilmObject>();
    String filmScreen = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new HomeScreenFragment())
                .commit();

        //GET DATA
        //-location data
        FetchLocationData locationsGet = new FetchLocationData();
        locationsGet.execute();

        fetchMovieData movieDataGet = new fetchMovieData();
        movieDataGet.execute();

        //-get all film data


        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.filmFragToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Loads Film screen if notification clicked on
        Intent newint = getIntent();
        filmScreen = newint.getStringExtra(DownloadService.EXTRA_FILM);
        if (filmScreen != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FilmListScreenFragment())
                    .commit();
        }


        //Runs Service Automatically Every 60 Minutes
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(this, DownloadService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                60 * 60 * 1000, pintent);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new HomeScreenFragment())
                    .commit();
        } else if (id == R.id.nav_film) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FilmScreenFragment())
                    .commit();

        } else if (id == R.id.nav_films_list) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FilmListScreenFragment())
                    .commit();

        } else if (id == R.id.nav_locations) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new LocationScreenFragment())
                    .commit();

        } else if (id == R.id.nav_timetables) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new TimetableScreenFragment())
                    .commit();
        } else if (id == R.id.favourite) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame
//                            , new TimetableScreenFragment())
//                    .commit();
        } else if (id == R.id.user_guide) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new UserGuideFragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class fetchMovieData extends AsyncTask<Void, Void, String> {
        public String movieDetailsURL = "http://ac-portal.uk/mad/filmInformation.php";


        @Override
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
            Log.i("jitz", "fetchMovieData->onPostExecute");

            try {
                Log.i("jitz", s);
                JSONObject allFilmsJson = new JSONObject(s);
                JSONArray films = allFilmsJson.getJSONArray("films");
                Log.i("jitz", "films: " + films.toString());

//                JSONObject film;
                for (int i = 0; i < films.length(); i++) {
                    JSONObject film = films.getJSONObject(i);
                    Log.i("jitz", "film: " + film.toString());
                    Log.i("jitz", "film.getString(\"name\"): " + film.getString("Name"));
                    //create a temporary Film object that will then be added to an arrayList(allFilmsObjectArray)
                    //temporary = new FilmObject();
                    //set all the properties of temporary FilmObject
                    // Log.i("TE","TEST");
                    FilmObject temporary = new FilmObject(Integer.parseInt(film.getString("ID")),
                            film.getString("Name"),
                            film.getString("Description"),
                            Integer.parseInt(film.getString("Rating")),
                            Integer.parseInt(film.getString("MinimumAge")),
                            film.getString("TrailerLink"),
                            film.getString("ImageLink")
                    );
                    Log.i("jitz", temporary.getName());//this is not printing anything in the log why????
                    //add the temporary Film object to the arrayList(allFilmsObjectArray)
                    allFilmObjectsArray.add(temporary);
                    Log.i("jitz2", "allFilmObjectsArray.get(0).getName()" + allFilmObjectsArray.get(0).getName());
                }

            } catch (JSONException e) {
                Log.d("JSON Exception", e.getLocalizedMessage());
            }
            Log.i("json", s);
        }
    }

    public class FetchLocationData extends AsyncTask<Void, Void, String> {
        public String CONST_LOCATIONURL = "http://ac-portal.uk/mad/locationInfoOut.php";


        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String resultJson = null;

            try {
                // Construct the URL
                URL url = new URL(CONST_LOCATIONURL);

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
                JSONObject json = new JSONObject(s);

                //add locations to list
                JSONArray locations = json.getJSONArray("locations");
                for (int i = 0; i < locations.length(); i++) {
                    JSONObject location = locations.getJSONObject(i);

                    LocationItem temporary = new LocationItem(location.getString("ID"), location.getString("Name"), location.getString("Address") + "," + location.getString("Postcode"));
                    locationArray.add(temporary);
                }

            } catch (JSONException e) {
                Log.d("JSON Exception", e.getLocalizedMessage());
            }
            Log.i("json", s);
        }
    }


    public void sendNotification(View view) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("MAD Cinema")
                        .setContentText("New movie released today!");

        Intent resultIntent = new Intent(this, HomeActivity.class);

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
        mNotificationManager.notify(111, mBuilder.build());
    }

}