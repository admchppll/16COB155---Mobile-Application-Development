package loughboroughuniversity.madcinema;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.System.in;

/**
 * Created by Adam on 16/05/2017.
 */

public class LocationListAdapter extends ArrayAdapter<LocationItem> {
    private ArrayList<LocationItem> items;
    private Context context;
    String locationName;

    public LocationListAdapter(Context context, int resource, ArrayList<LocationItem> items){
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocationItem location = items.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.location_list_item, null);

        TextView Name = (TextView) row.findViewById(R.id.locationInformation);

        Name.setText(location.getName());

        ImageButton locationPreference = (ImageButton)row.findViewById(R.id.locationPreferenceButton);
        locationPreference.setTag(location.getID());
        if(location.getPreferred() == true){
            locationPreference.setEnabled(false);
            locationPreference.setVisibility(View.INVISIBLE);
        }

        locationPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationPref = row.getContext().getSharedPreferences("pref_location", MODE_PRIVATE).getString("locationID", "");

                final String LocationID = (String) view.getTag();
                final HomeActivity home = (HomeActivity) getContext();
                final View button = view;

                //confirm preference action
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(row.getContext());
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure you want to set this as your preferred location?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        locationName;
                        // User clicked OK button
                        SharedPreferences myPref = row.getContext().getSharedPreferences("pref_location", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.clear();
                        editor.putString("locationID", LocationID);

                        if (LocationID.equals("0")){
                            editor.putString("locationName", "Cheddar");
                        } else if (LocationID.equals("1")){
                            editor.putString("locationName", "Loughborough");
                        } else  if (LocationID.equals("2")){
                            editor.putString("locationName", "Pilkington");
                        } else if (LocationID.equals("3")){
                            editor.putString("locationName", "Haslegrave");
                        }

                        editor.commit();

                        //remove button from screen
//                        button.setVisibility(View.INVISIBLE);
//                        button.setEnabled(false);

                        //set location as preferred in local data for later
                        //(only one preferred location so overwrite previous values)
                        for (int i = 0; i < home.locationArray.size(); i++){
                            if(home.locationArray.get(i).getID().equals(LocationID)){
                                home.locationArray.get(i).setPreferred();
                            }
                            else {
                                home.locationArray.get(i).setNotPreferred();
                            }
                        }

                        Toast.makeText(row.getContext(),"Location Confirmed!", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
                alertDialog.create().show();
            }
        });

        ImageButton mapButton = (ImageButton)row.findViewById(R.id.mapButton);
        mapButton.setTag(location.getAddress());
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mapRedirect", "On Click listener working STEP 1");
                Log.d("mapRedirect", (String) view.getTag());
                String address = (String) view.getTag();
                Uri gMaps = Uri.parse("geo:0,0?q="+address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gMaps);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });

        return row;
    }
}
