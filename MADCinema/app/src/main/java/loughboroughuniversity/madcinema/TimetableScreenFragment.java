package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
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
    private String[] movieListTimeTable = {"hello","Hello","hi"};
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(timetable_layout, container, false);
        timeTableViewPopulate();

        return myView;

    }

    public void timeTableViewPopulate(){
        HomeActivity home = (HomeActivity)getActivity();
        ListView list = (ListView) myView.findViewById(id.timeTableList);
        
        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        for (int i=0; i<home.timetableArray.size(); i++) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", home.timetableArray.get(i));
            datum.put("subtitle", home.timetableSubArray.get(i));
            data.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), data,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "subtitle"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        list.setAdapter(adapter);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_expandable_list_item_1, home.timetableArray);
//        ListView list = (ListView) myView.findViewById(id.timeTableList);
//        list.setAdapter(adapter);

    }



}
