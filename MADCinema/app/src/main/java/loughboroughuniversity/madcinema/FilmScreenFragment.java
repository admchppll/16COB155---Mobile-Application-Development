package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import static android.R.attr.value;
import static android.content.Context.MODE_PRIVATE;
import static android.provider.Telephony.Mms.Part.FILENAME;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class FilmScreenFragment extends Fragment {
    View myView;


    String textOut = "Loading";
    //edit movieID to decide which movie should be shown
    String movieID = "2";
    ImageView moviePoster;
    Button btnFave, btnShare;
    String videoID, userName, filmName;
    TextView movieDescriptionTextView;


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        textOut = "Test";
        Log.i("myTag", "onCreateView started");
        userName = "no";

        myView = inflater.inflate(R.layout.film_layout, container, false);

        movieDescriptionTextView = (TextView) myView.findViewById(R.id.movieDescriptionTextView);
        movieDescriptionTextView.setText(textOut);

        btnFave = (Button) myView.findViewById(R.id.btnFave);
        btnFave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Writes Film to Phone
                saveFilm();
            }
        });

        btnShare = (Button) myView.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Writes Film to Phone
                shareFilm();
            }
        });


        moviePoster = (ImageView) myView.findViewById(R.id.moviePosterImgView);
        moviePoster.setImageResource(0);

        GetMovieDetails movieDetails = new GetMovieDetails();
        movieDetails.execute();

        Log.i("myTag", "about to return view");
        return myView;

    }

    //Async task to load the movie poster
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            try {
                bmImage.setImageBitmap(result);
            } catch (Exception e) {
                Log.d("Image Error", e.getLocalizedMessage());
            }
        }
    }

    //async task that is called from DownloadImageTask
    //to get movie details like name, description, poster URL...
    public class GetMovieDetails extends AsyncTask<Void, Void, String> {

        public String dynamic_movieDetailURL = "http://ac-portal.uk/mad/filmInfoDetailedOut.php?q=" + movieID;


        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            Log.i("myTag", "doInBackground started");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String resultJson = null;

            try {
                // Construct the URL
                URL url = new URL(dynamic_movieDetailURL);

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
                    Log.i("myTag", "buffer.length() == 0");
                    return null;
                }
                resultJson = buffer.toString();
                Log.i("myTag", "returning resultJason ");
                return resultJson;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get data
                return null;
            } finally {
                if (urlConnection != null) {
                    Log.i("myTag", "urlConnection != null");
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


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView movieDescriptionTextView = (TextView) myView.findViewById(R.id.movieDescriptionTextView);


            try {
                JSONObject json = new JSONObject(s);
                //add locations to list

                JSONArray filmInfo = json.getJSONArray("filmInfo");
                JSONObject movieDetailObject = filmInfo.getJSONObject(0);

                filmName = movieDetailObject.getString("Name");

                textOut = movieDetailObject.getString("Description");
                movieDescriptionTextView.setText(textOut);

                videoID = movieDetailObject.getString("Trailer");

                new DownloadImageTask(moviePoster)
                        .execute(movieDetailObject.getString("Img").replaceAll("\\\\", ""));

            } catch (JSONException e) {
                Log.d("JSON Exception", e.getLocalizedMessage());
            }
            Log.i("json", s);
        }
    }
    public void saveFilm() {
        SharedPreferences myPref = getActivity().getSharedPreferences("fave_films",MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPref.edit();
        myEditor.clear();
        myEditor.putString("filmname", filmName);// or putDouble, putString, etc…
        myEditor.commit();
    }
    public void loadFilm(){
        SharedPreferences myPref = getActivity().getSharedPreferences("fave_films", MODE_PRIVATE);
        userName = myPref.getString("filmname", "");
    }
    public void shareFilm() { //Share youtube video of film
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "Check out this films trailer! https://www.youtube.com/watch?v=" + videoID;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Film Trailer!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
    }
}
