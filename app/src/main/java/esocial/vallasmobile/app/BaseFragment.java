package esocial.vallasmobile.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tr.xip.errorview.ErrorView;

/**
 * Created by jesus.martinez on 21/01/2016.
 */
public class BaseFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public VallasApplication getVallasApplication(){
        if(isActivityFinished())
            return null;
        else
            return ((VallasApplication)getActivity().getApplicationContext());
    }

    public boolean isActivityFinished(){
        if(getActivity()==null || getActivity().isFinishing())
            return true;
        else
            return false;
    }
}
