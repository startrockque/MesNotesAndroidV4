package fabien.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.special.ResideMenu.ResideMenu;

import fabien.activity.MainActivity;
import fabien.mynotes.R;

/**
 * Created by Fabien on 17/06/2015.
 */
public class HomeFragment extends android.support.v4.app.Fragment {
    private View parentView;
    private ResideMenu resideMenu;
    private MainActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        return parentView;
    }
}
