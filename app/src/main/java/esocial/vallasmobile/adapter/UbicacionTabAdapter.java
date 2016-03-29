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
import esocial.vallasmobile.app.ubicaciones.UbicacionDetalle;
import esocial.vallasmobile.app.ubicaciones.UbicacionImagenesFragment;
import esocial.vallasmobile.app.ubicaciones.UbicacionInfoFragment;
import esocial.vallasmobile.app.ubicaciones.UbicacionMediosFragment;

/**
 * Created by jesus.martinez on 04/01/2016.
 */
public class UbicacionTabAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;
    UbicacionDetalle ctx;
    BaseFragment tab1, tab2, tab3;


    public UbicacionTabAdapter(FragmentManager fm, int NumOfTabs, UbicacionDetalle ctx) {
        super(fm);
        this.ctx = ctx;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null)
                    tab1 = new UbicacionInfoFragment();
                return tab1;
            case 1:
                if (tab2 == null)
                    tab2 = new UbicacionImagenesFragment();
                return tab2;
            case 2:
                if (tab3 == null)
                    tab3 = new UbicacionMediosFragment();
                return tab3;
            default:
                return null;
        }
    }

    public View getTabView(int position, boolean isSelected) {
        // Given you have a custom layout in `res/layout/main_tabml` with a TextView and ImageView
        View v = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ubicacion_tab,
                null, false);
        TextView tv = (TextView) v.findViewById(R.id.ub_tabText);

        tv.setText(ctx.tabTitles[position]);
        if (isSelected) {
            tv.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
        } else {
            tv.setTextColor(ctx.getResources().getColor(R.color.tab_text_color));
        }

        return v;
    }

    public void updateCustomView(View v, int position, boolean isSelected) {
        TextView tv = (TextView) v.findViewById(R.id.ub_tabText);

        tv.setText(ctx.tabTitles[position]);
        if (isSelected) {
            tv.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
        } else {
            tv.setTextColor(ctx.getResources().getColor(R.color.tab_text_color));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}