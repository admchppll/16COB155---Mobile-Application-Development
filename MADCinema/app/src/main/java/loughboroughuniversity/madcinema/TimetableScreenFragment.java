package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by darre_000 on 13/04/2017.
 */

public class TimetableScreenFragment extends Fragment {
    View myView;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.timetable_layout, container, false);

        return myView;

    }
}
