package loughboroughuniversity.madcinema;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Adam on 16/05/2017.
 */

public class LocationListAdapter extends ArrayAdapter<LocationItem> {
    private ArrayList<LocationItem> items;
    private Context context;

    public LocationListAdapter(Context context, int resource, ArrayList<LocationItem> items){
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocationItem location = items.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.location_list_item, null);

        TextView Name = (TextView) row.findViewById(R.id.locationInformation);
        Name.setText(location.getName());

        //holder.locationPreferenceButton = (ImageButton)row.findViewById(R.id.locationPreferenceButton);
        //holder.locationPreferenceButton.setTag(holder.locationItem);
        //holder.mapButton = (ImageButton)row.findViewById(R.id.mapButton);
        //holder.mapButton.setTag(holder.locationItem);
        //holder.Name = (TextView)row.findViewById(R.id.locationInformation);
        //row.setTag(holder);
        //setupItem(holder);

        return row;
    }
}
