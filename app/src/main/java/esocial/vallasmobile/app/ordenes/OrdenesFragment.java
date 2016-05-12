package esocial.vallasmobile.app.ordenes;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.OrdenesMainTabAdapter;
import esocial.vallasmobile.app.BaseFragment;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class OrdenesFragment extends BaseFragment {

    public String tabTitles[];
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ordenes, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.ord_pager);
        tabLayout = (TabLayout) v.findViewById(R.id.ord_tab_layout);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabTitles = new String[]{getString(R.string.pendiente), getString(R.string.cerradas)};

        initTabLayout();
    }

    private void initTabLayout() {

        final OrdenesMainTabAdapter adapter = new OrdenesMainTabAdapter(getActivity().getSupportFragmentManager(),
                tabTitles.length);
        viewPager.setAdapter(adapter);

        //add tabs
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setCustomView(customView(i)));
        }


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private View customView(int position){
        View view = getActivity().getLayoutInflater().inflate(R.layout.text_tab,null);
        TextView textView = (TextView) view.findViewById(R.id.ub_tabText);
        textView.setText(tabTitles[position]);
        return view;
    }
}
