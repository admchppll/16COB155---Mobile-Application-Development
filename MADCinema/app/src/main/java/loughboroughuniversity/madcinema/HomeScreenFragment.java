package loughboroughuniversity.madcinema;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class HomeScreenFragment extends Fragment {

    View myView;
    Button button;
    TextView txtCharge;
    HomeActivity home;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.home_screen_layout, container, false);

        home = (HomeActivity)getActivity();

        txtCharge = (TextView) myView.findViewById(R.id.txtCharge);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = home.registerReceiver(null, filter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
        if (isCharging){
            txtCharge.setText("There are chargers availble at all cinemas");
        } else {
            txtCharge.setText("Come visit us now!");
        }

        button = (Button) myView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Writes Film to Phone
                home.sendNotification(myView);
            }
        });

        return myView;

    }
}
