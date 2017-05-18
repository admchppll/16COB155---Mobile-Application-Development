package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by darre_000 on 18/05/2017.
 */

public class FavouriteFragment  extends Fragment {
    View myView;


    String textOut = "Loading";
    //edit movieID to decide which movie should be shown
    //    ImageView moviePoster;
    FloatingActionButton floatingBtnFav, floatingBtnShare;
    String videoID, userName, imagePoster, filmDesc, filmName;
    TextView movieDescriptionTextView;
    Boolean loadFavourite;
    int i, ratingFilm, movieReference;
    String movieID;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userName = "no";

        //get HomeActivity context
        HomeActivity home = (HomeActivity) getActivity();

        myView = inflater.inflate(R.layout.favourite_layout, container, false);


        SharedPreferences myPref = getActivity().getSharedPreferences("fave_films", MODE_PRIVATE);
        imagePoster = myPref.getString("poster", "");
        ratingFilm = myPref.getInt("rating", 0);
        filmDesc = myPref.getString("description", "");
        videoID = myPref.getString("filmVid", "");
        filmName = myPref.getString("filmName", "");

        getActivity().setTitle(filmName);

        //POSTER IMAGE
        ImageView moviePoster = (ImageView) myView.findViewById(R.id.moviePosterImgView);
        moviePoster.setImageResource(0);
        new FavouriteFragment.DownloadImageTask(moviePoster)
                .execute(imagePoster.replaceAll("\\\\", ""));

        //RATING
        RatingBar movieRatingBar = (RatingBar) myView.findViewById(R.id.movieRatingBar);
        movieRatingBar.setRating(ratingFilm/2);

        //initiate and set movie decryption to a default description("Loading")
        movieDescriptionTextView = (TextView) myView.findViewById(R.id.movieDescriptionTextView);
        movieDescriptionTextView.setText(filmDesc);

        //Trailer Link
        videoID = home.allFilmObjectsArray.get(movieReference).getTrailer();

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
            Log.d("TEST1","");
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.d("URL", urldisplay);
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
        myEditor.putString("poster", imagePoster);// or putDouble, putString, etc…
        myEditor.putInt("rating", ratingFilm);// or putDouble, putString, etc…
        myEditor.putString("description", filmDesc);// or putDouble, putString, etc…
        myEditor.putString("filmVid", videoID);
        myEditor.commit();
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
