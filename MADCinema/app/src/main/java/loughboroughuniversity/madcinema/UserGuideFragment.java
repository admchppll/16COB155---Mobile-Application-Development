package loughboroughuniversity.madcinema;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by darre_000 on 17/05/2017.
 */

public class UserGuideFragment extends Fragment {
    View myView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.user_guide_layout, container, false);
        return myView;
    }
}
