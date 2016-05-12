package esocial.vallasmobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.incidencias.IncidenciasAsignadasFragment;
import esocial.vallasmobile.app.incidencias.IncidenciasCreadasFragment;
import esocial.vallasmobile.app.ordenes.OrdenesCerradasFragment;
import esocial.vallasmobile.app.ordenes.OrdenesPendientesFragment;

/**
 * Created by jesus.martinez on 04/01/2016.
 */
public class OrdenesMainTabAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;
    BaseFragment tab1, tab2;


    public OrdenesMainTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null)
                    tab1 = new OrdenesPendientesFragment();
                return tab1;
            case 1:
                if (tab2 == null)
                    tab2 = new OrdenesCerradasFragment();
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
