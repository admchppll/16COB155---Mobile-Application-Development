package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;


/**
 * Created by darre_000 on 13/04/2017.
 */

public class LocationScreenFragment extends Fragment {
    View myView;
    ArrayList<LocationItem> locations;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.location_layout, container, false);
        //locationList
        ListView listView = (ListView) myView.findViewById(R.id.locationList);

        //get HomeActivity context
        HomeActivity home = (HomeActivity)getActivity();
        getActivity().setTitle("MAD Cinema");

        ArrayAdapter<LocationItem> adapter = new LocationListAdapter(getActivity(), 0, home.locationArray);
        listView.setAdapter(adapter);

        return myView;
    }
}

