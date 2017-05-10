package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class LocationScreenFragment extends Fragment {
    View myView;


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.location_layout, container, false);
        //locationList
        ListView listView = (ListView) myView.findViewById(R.id.locationList);

        //get HomeActivity context
        HomeActivity home = (HomeActivity)getActivity();

        //output results to list
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                home.locationArray
        );

        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString();
                info = info.substring(info.indexOf("\n")+1);

                Uri gMaps = Uri.parse("geo:0,0?q="+info);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gMaps);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        Log.d("CUSTOM", "Got here");
        return myView;
    }
}

