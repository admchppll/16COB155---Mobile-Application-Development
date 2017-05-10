package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class LocationScreenFragment extends Fragment {
    View myView;
    public String CONST_URL = "http://ac-portal.uk/mad/locationInfoOut.php";
    public ArrayList<String> locationArray = new ArrayList<String>();

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.location_layout, container, false);
        //locationList
        ListView listView = (ListView) myView.findViewById(R.id.locationList);

        FetchLocationData fetch = new FetchLocationData();
        fetch.execute();

        //output results to list
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                locationArray
        );

        listView.setAdapter(listViewAdapter);
        Log.d("CUSTOM", "Got here");
        return myView;
    }

    private class FetchLocationData extends AsyncTask<Void, Void, String> {

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
                URL url = new URL(CONST_URL);

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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
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
            } finally{
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
                for(int i = 0; i < locations.length(); i++){
                    JSONObject location = locations.getJSONObject(i);
                    locationArray.add(location.getString("Name"));
                }

            } catch (JSONException e){
                Log.d("JSON Exception", e.getLocalizedMessage());
            }

            Log.i("json", s);
        }
    }
}
