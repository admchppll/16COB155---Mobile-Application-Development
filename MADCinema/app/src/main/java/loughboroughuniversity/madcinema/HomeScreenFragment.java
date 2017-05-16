package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class HomeScreenFragment extends Fragment {

    View myView;
    Button button;
    HomeActivity home;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.home_screen_layout, container, false);

        home = (HomeActivity)getActivity();

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
