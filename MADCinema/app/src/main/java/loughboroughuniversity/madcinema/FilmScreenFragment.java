package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class FilmScreenFragment extends Fragment {
    View myView;


    String textOut = "Loading";
    //edit movieID to decide which movie should be shown
    String movieID = "2";
//    ImageView moviePoster;
    FloatingActionButton floatingBtnFav, floatingBtnShare;
    String videoID, userName, filmName;
    TextView movieDescriptionTextView;


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userName = "no";

        int movieReference = 1;
        //get HomeActivity context
        HomeActivity home = (HomeActivity) getActivity();

        myView = inflater.inflate(R.layout.film_layout, container, false);

        //POSTER IMAGE
        ImageView moviePoster = (ImageView) myView.findViewById(R.id.moviePosterImgView);
        moviePoster.setImageResource(0);
        new DownloadImageTask(moviePoster)
                .execute(home.allFilmObjectsArray.get(movieReference).getImg().replaceAll("\\\\", ""));

        //RATING
        RatingBar movieRatingBar = (RatingBar) myView.findViewById(R.id.movieRatingBar);
        movieRatingBar.setRating(home.allFilmObjectsArray.get(movieReference).getRating());

        //initiate and set movie decryption to a default description("Loading")
        movieDescriptionTextView = (TextView) myView.findViewById(R.id.movieDescriptionTextView);
        movieDescriptionTextView.setText(home.allFilmObjectsArray.get(movieReference).getDescription());


        //set listener to floating share button
        floatingBtnShare = (FloatingActionButton) myView.findViewById(R.id.floatingBtnShare);
        floatingBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Writes Film to Phone
                shareFilm();
            }
        });

        //set listener to floating favourite button
        floatingBtnFav = (FloatingActionButton) myView.findViewById(R.id.floatingBtnFav);
        floatingBtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Writes Film to Phone
                saveFilm();
            }
        });

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


    public void saveFilm() {
        SharedPreferences myPref = getActivity().getSharedPreferences("fave_films", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPref.edit();
        myEditor.clear();
        myEditor.putString("filmname", filmName);// or putDouble, putString, etc…
        myEditor.commit();
    }

    public void loadFilm() {
        SharedPreferences myPref = getActivity().getSharedPreferences("fave_films", MODE_PRIVATE);
        userName = myPref.getString("filmname", "");
    }

    public void shareFilm() { //Share youtube video of film
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "Check out this films trailer! https://www.youtube.com/watch?v=" + videoID;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Film Trailer!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
    }
}
