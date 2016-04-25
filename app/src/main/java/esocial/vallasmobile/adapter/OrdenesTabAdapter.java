package esocial.vallasmobile.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.ordenes.OrdenDetalle;
import esocial.vallasmobile.app.ordenes.OrdenInfoFragment;
import esocial.vallasmobile.app.ordenes.OrdenImagenesFragment;

/**
 * Created by jesus.martinez on 04/01/2016.
 */
public class OrdenesTabAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;
    OrdenDetalle ctx;
    BaseFragment tab1, tab2;


    public OrdenesTabAdapter(FragmentManager fm, int NumOfTabs, OrdenDetalle ctx) {
        super(fm);
        this.ctx = ctx;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null)
                    tab1 = new OrdenInfoFragment();
                return tab1;
            case 1:
                if (tab2 == null)
                    tab2 = new OrdenImagenesFragment();
                return tab2;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
