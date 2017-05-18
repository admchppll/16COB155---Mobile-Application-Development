package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static loughboroughuniversity.madcinema.R.*;
import static loughboroughuniversity.madcinema.R.layout.timetable_layout;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class TimetableScreenFragment extends Fragment {
    View myView;
    public ArrayList<String> dateArray = new ArrayList<String>();
    public ArrayList<String> hiddenDateArray = new ArrayList<String>();
    public ArrayList<String> timetableArray = new ArrayList<String>();
    public ArrayList<String> timetableSubArray = new ArrayList<String>();
    JSONArray times = new JSONArray();
    String locationPref;
    String prefLocationName;
    int currentDateValue = 0;
    TextView dateOut;
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(timetable_layout, container, false);
        HomeActivity home = (HomeActivity)getActivity();
        locationPref = home.getSharedPreferences("pref_location", MODE_PRIVATE).getString("locationID", "");
        prefLocationName= "";
        if (locationPref.trim().equals("")){
            prefLocationName ="";
        }else {
            prefLocationName = home.locationArray.get(Integer.parseInt(locationPref)).getName();
        }

        getActivity().setTitle("MAD Cinema");

        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        FetchTimetableData timetableGet = new FetchTimetableData();
        timetableGet.execute();

        nextDateButtonClick();
        prevDateButtonClick();

        myView.setOnTouchListener(new OnSwipeTouchListener(myView.getContext()) {
            @Override
            public void onSwipeLeft() {
                currentDateValue= currentDateValue-1;
                if(currentDateValue <= -1){
                    currentDateValue = dateArray.size()-1;
                }


                dateOut.setText(dateArray.get(currentDateValue));
                getInfoForDate();
                timeTableViewPopulate();
            }
            public void onSwipeRight(){
                currentDateValue= currentDateValue-1;
                if(currentDateValue <= -1){
                    currentDateValue = dateArray.size()-1;
                }


                dateOut.setText(dateArray.get(currentDateValue));
                getInfoForDate();
                timeTableViewPopulate();


            }
        });

        return myView;

    }


    public void formatJsonObject(){
        JSONArray timeArray = times;
        dateArray.clear();
        String favouriteLocation = prefLocationName;
        for(int i= 0;i<timeArray.length();i++){

            JSONObject time = null;
            try {
                time = timeArray.getJSONObject(i);
                boolean isFavouriteLocationSelected = false;
                if(favouriteLocation.equals("")) {
                    isFavouriteLocationSelected = true;
                }else {
                    JSONArray filmsJSONArray = time.getJSONArray("film");
                    for (int x = 0; x < filmsJSONArray.length(); x++) {
                        JSONObject filmObject = filmsJSONArray.getJSONObject(x);
                        if (favouriteLocation.equals(filmObject.getString("LocationName"))) {
                            isFavouriteLocationSelected = true;
                            break;
                        }
                    }
                }

                Date OutputDate = null;
                String outputDateString = "TEST";
                try {
                    OutputDate = new SimpleDateFormat("yyyy-mm-dd").parse(time.getString("date"));
                    outputDateString = new SimpleDateFormat("dd-mm-yyyy").format(OutputDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(isFavouriteLocationSelected) {
                    dateArray.add(outputDateString);
                }
                hiddenDateArray.add(outputDateString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getInfoForDate() {
        JSONArray timeArray = times;
        timetableArray = new ArrayList<String>();
        timetableSubArray = new ArrayList<String>();
        String favouriteLocation = prefLocationName;
        JSONObject time = null;
        try {
            int currentTimeInt = 0;
            for (int y=0; y<hiddenDateArray.size();y++){
                if(hiddenDateArray.get(y).equals(dateArray.get(currentDateValue))){
                    currentTimeInt = y;
                    break;
                }
            }
            time = timeArray.getJSONObject(currentTimeInt);
            JSONArray filmsJSONArray = time.getJSONArray("film");
            for (int x = 0; x < filmsJSONArray.length(); x++) {
                JSONObject filmOut = filmsJSONArray.getJSONObject(x);
                if(favouriteLocation.equals("")) {
                    timetableArray.add(filmOut.getString("FilmName"));
                    timetableSubArray.add("Location: " + filmOut.getString("LocationName") + " Time: " + filmOut.getString("Date"));
                }else if (favouriteLocation.equals(filmOut.getString("LocationName"))){
                    timetableArray.add(filmOut.getString("FilmName"));
                    timetableSubArray.add("Location: " + filmOut.getString("LocationName") + " Time: " + filmOut.getString("Date"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void nextDateButtonClick(){
        Button nextButtonAction = (Button) myView.findViewById(id.nextDateButton);
        nextButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDateValue= currentDateValue+1;
                if(currentDateValue >= dateArray.size()){
                    currentDateValue = 0;
                }


                dateOut.setText(dateArray.get(currentDateValue));
                getInfoForDate();
                timeTableViewPopulate();
            }
        });


    }

    public void prevDateButtonClick(){
        Button prevButtonAction = (Button) myView.findViewById(id.prevDateButton);
        prevButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDateValue= currentDateValue-1;
                if(currentDateValue <= -1){
                    currentDateValue = dateArray.size()-1;
                }


                dateOut.setText(dateArray.get(currentDateValue));
                getInfoForDate();
                timeTableViewPopulate();
            }
        });


    }

    public void timeTableViewPopulate(){
        ListView list = (ListView) myView.findViewById(id.timeTableList);

        list.setOnTouchListener(new OnSwipeTouchListener(list.getContext()) {
            @Override
            public void onSwipeLeft() {
                currentDateValue= currentDateValue-1;
                if(currentDateValue <= -1){
                    currentDateValue = dateArray.size()-1;
                }


                dateOut.setText(dateArray.get(currentDateValue));
                getInfoForDate();
                timeTableViewPopulate();
            }
            public void onSwipeRight(){
                currentDateValue= currentDateValue-1;
                if(currentDateValue <= -1){
                    currentDateValue = dateArray.size()-1;
                }


                dateOut.setText(dateArray.get(currentDateValue));
                getInfoForDate();
                timeTableViewPopulate();


            }
        });

        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        for (int i=0; i<timetableArray.size(); i++) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", timetableArray.get(i));
            datum.put("subtitle", timetableSubArray.get(i));
            data.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), data,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "subtitle"},
                new int[] {android.R.id.text1,
                        android.R.id.text2}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);
                textView2.setTextColor(Color.BLACK);

                return view;
            }

        };

        list.setAdapter(adapter);

    }
    public class FetchTimetableData extends AsyncTask<Void, Void, String> {
        public String CONST_TIME_OUT_URL = "http://ac-portal.uk/mad/filmTimeOut.php";

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
                URL url = new URL(CONST_TIME_OUT_URL);

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
                times = json.getJSONArray("filmTime");
                formatJsonObject();
                dateOut = (TextView) myView.findViewById(id.dateOut);

                dateOut.setText(dateArray.get(currentDateValue));

                getInfoForDate();
                timeTableViewPopulate();

            } catch (JSONException e){
                Log.d("JSON Exception", e.getLocalizedMessage());
            }
        }
    }

}
