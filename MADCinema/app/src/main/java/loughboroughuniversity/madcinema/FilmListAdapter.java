package loughboroughuniversity.madcinema;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Bundle;
import android.app.FragmentManager;
import android.app.Fragment;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by liam on 18/05/2017.
 */

public class FilmListAdapter extends ArrayAdapter<FilmObject> {
    private ArrayList<FilmObject> items;
    private Context context;
    private Fragment fragment;;

    public FilmListAdapter(Context context, int resource, ArrayList<FilmObject> items, FilmListScreenFragment fragment){
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.fragment = fragment;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HomeActivity home;

        //home = parent.getContext();//(HomeActivity)getActivity();


        FilmObject film = items.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View film_item = inflater.inflate(R.layout.film_list_item, null);

        //set film poster
        //ImageButton filmPoster = (ImageButton)film_item.findViewById(R.id.moviePosterButton);
        //filmPoster.setImageResource(0);
        /*Log.d("Film Loading", "Image loading");
        new DownloadImageTask(filmPoster)
                .execute(items.get(film.getId()-1).getImg().replaceAll("\\\\", ""));
        Log.d("Film Loading", "Image loading finished");*/

        //set tag to id of film to retrieve film screen

        //set film name
        //new DownloadImageTask(moviePoster2)
        //        .execute(home.allFilmObjectsArray.get(1).getImg().replaceAll("\\\\", ""));


        TextView Name = (TextView) film_item.findViewById(R.id.movieTitle);
        Name.setText(film.getName());

        Name.setTag(film.getId());


        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int FilmID = (int) view.getTag();
                final View button = view;


                Bundle bundle2 = new Bundle();
                bundle2.putInt("film", FilmID);
                Log.d("film id", Integer.toString(FilmID));
                Fragment fragmentFavourite = new FilmScreenFragment();
                fragmentFavourite.setArguments(bundle2);
                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , fragmentFavourite).addToBackStack( "tag2" )
                        .commit();

                //do action here to redirect to
            }
        });

        return film_item;
    }

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
    }*/
}
