package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class FilmListScreenFragment extends Fragment{
    View myView;
    HomeActivity home;


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.films_list_layout, container, false);

        home = (HomeActivity)getActivity();


        int movieReference = 0;

//        imagePosterURL = home.allFilmObjectsArray.get(movieReference).getImg()
        ImageButton moviePoster1 = (ImageButton) myView.findViewById(R.id.moviePosterButton1);
        moviePoster1.setImageResource(0);
        new DownloadImageTask(moviePoster1)
                .execute(home.allFilmObjectsArray.get(movieReference).getImg().replaceAll("\\\\", ""));
        movieReference = 1;
        ImageButton moviePoster2 = (ImageButton) myView.findViewById(R.id.moviePosterButton2);
        moviePoster2.setImageResource(0);
        ImageButton moviePoster3 = (ImageButton) myView.findViewById(R.id.moviePosterButton3);
        moviePoster3.setImageResource(0);
        ImageButton moviePoster4 = (ImageButton) myView.findViewById(R.id.moviePosterButton4);
        moviePoster4.setImageResource(0);

        new DownloadImageTask(moviePoster2)
                .execute(home.allFilmObjectsArray.get(1).getImg().replaceAll("\\\\", ""));
        new DownloadImageTask(moviePoster3)
                .execute(home.allFilmObjectsArray.get(2).getImg().replaceAll("\\\\", ""));
        new DownloadImageTask(moviePoster4)
                .execute(home.allFilmObjectsArray.get(3).getImg().replaceAll("\\\\", ""));



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

}