package it.rainbowbreeze.playtog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.rainbowbreeze.playtog.R;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class StartGameFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_startgame, container, false);
        return rootView;
    }
}
