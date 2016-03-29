package esocial.vallasmobile.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.incidencias.IncidenciasFragment;
import esocial.vallasmobile.app.ubicaciones.UbicacionesFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.app.ordenes.OrdenesFragment;

/**
 * Created by jesus.martinez on 04/01/2016.
 */
public class MainTabPagerAdapter extends FragmentStatePagerAdapter {

    public static int[] imageDefaultResId = {R.drawable.tabbar_orders_default,
            R.drawable.tabbar_incidents_default, R.drawable.tabbar_location_default};
    private int[] imageSelectedResId = {R.drawable.tabbar_orders_active,
            R.drawable.tabbar_incidents_active,  R.drawable.tabbar_location_active};

    int mNumOfTabs;
    MainTabActivity ctx;
    BaseFragment tab1, tab2, tab3;


    public MainTabPagerAdapter(FragmentManager fm, int NumOfTabs, MainTabActivity ctx) {
        super(fm);
        this.ctx = ctx;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null)
                    tab1 = new OrdenesFragment();
                return tab1;
            case 1:
                if (tab2 == null)
                    tab2 = new IncidenciasFragment();
                return tab2;
            case 2:
                if (tab3 == null)
                    tab3 = new UbicacionesFragment();
                return tab3;
            default:
                return null;
        }
    }

    public View getTabView(int position, boolean isSelected) {
        // Given you have a custom layout in `res/layout/main_tabml` with a TextView and ImageView
        View v = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.main_tab,
                null, false);
        TextView tv = (TextView) v.findViewById(R.id.tabText);
        ImageView img = (ImageView) v.findViewById(R.id.tabImage);

        tv.setText(ctx.tabTitles[position]);
        if (isSelected) {
            img.setImageResource(imageSelectedResId[position]);
            tv.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
        } else {
            img.setImageResource(imageDefaultResId[position]);
            tv.setTextColor(ctx.getResources().getColor(R.color.tab_text_color));
        }

        return v;
    }

    public void updateCustomView(View v, int position, boolean isSelected) {
        TextView tv = (TextView) v.findViewById(R.id.tabText);
        ImageView img = (ImageView) v.findViewById(R.id.tabImage);

        tv.setText(ctx.tabTitles[position]);
        if (isSelected) {
            img.setImageResource(imageSelectedResId[position]);
            tv.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
        } else {
            img.setImageResource(imageDefaultResId[position]);
            tv.setTextColor(ctx.getResources().getColor(R.color.tab_text_color));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
