package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.content.Context;
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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static loughboroughuniversity.madcinema.R.*;
import static loughboroughuniversity.madcinema.R.layout.timetable_layout;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class TimetableScreenFragment extends Fragment {
    View myView;
    public ArrayList<String> dateArray = new ArrayList<String>();
    public ArrayList<String> timetableArray = new ArrayList<String>();
    public ArrayList<String> timetableSubArray = new ArrayList<String>();
    private GestureDetectorCompat mDetector;
    int currentDateValue = 0;
    TextView dateOut;
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(timetable_layout, container, false);


        nextDateButtonClick();
        prevDateButtonClick();
        formatJsonObject();
        dateOut = (TextView) myView.findViewById(id.dateOut);

        dateOut.setText(dateArray.get(currentDateValue));

        getInfoForDate();
        timeTableViewPopulate();
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
        HomeActivity home = (HomeActivity)getActivity();
        JSONArray timeArray = home.times;
        for(int i= 0;i<timeArray.length();i++){
            JSONObject time = null;
            try {
                time = timeArray.getJSONObject(i);
                Date OutputDate = null;
                String outputDateString = "TEST";
                try {
                    OutputDate = new SimpleDateFormat("yyyy-mm-dd").parse(time.getString("date"));
                    outputDateString = new SimpleDateFormat("dd-mm-yyyy").format(OutputDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateArray.add(outputDateString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getInfoForDate() {
        HomeActivity home = (HomeActivity) getActivity();
        JSONArray timeArray = home.times;
        timetableArray = new ArrayList<String>();
        timetableSubArray = new ArrayList<String>();
        JSONObject time = null;
        try {
            time = timeArray.getJSONObject(currentDateValue);
            JSONArray filmsJSONArray = time.getJSONArray("film");
            for (int x = 0; x < filmsJSONArray.length(); x++) {
                JSONObject filmOut = filmsJSONArray.getJSONObject(x);
                timetableArray.add(filmOut.getString("FilmName"));
                timetableSubArray.add("Location: "+filmOut.getString("LocationName") +" Time: "+filmOut.getString("Time"));
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
                        android.R.id.text2});

        list.setAdapter(adapter);

    }


}
