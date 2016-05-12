package esocial.vallasmobile.app.ordenes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.OrdenesAdapter;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.OrdenesListener;
import esocial.vallasmobile.obj.Orden;
import esocial.vallasmobile.tasks.GetOrdenesTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class OrdenesCerradasFragment extends BaseFragment implements OrdenesListener,
        MainTabActivity.OnOrdenesTabFragmentListener {

    private RecyclerView list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView emptyText;
    private ErrorView errorView;
    private LinearLayout filtroUbicacion;
    private AppCompatImageButton btnRemoveFilter;

    private OrdenesAdapter adapter;
    private ArrayList<Orden> ordenes;

    private Boolean searchByLocation = false;
    private String criteria = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ordenes_cerradas, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.orden_refresh);
        errorView = (ErrorView) v.findViewById(R.id.order_error_view);
        list = (RecyclerView) v.findViewById(R.id.order_list);
        emptyText = (TextView) v.findViewById(R.id.empty_orders);
        filtroUbicacion = (LinearLayout) v.findViewById(R.id.filtroUbicacion);
        btnRemoveFilter = (AppCompatImageButton) v.findViewById(R.id.btn_remove_filter);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Listener para comunicarnos con tabactivity
        ((MainTabActivity)getActivity()).setOrdenesCerradasListener(this);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        setListener();
        new GetOrdenesTask(getActivity(), criteria,
                searchByLocation ? VallasApplication.currentLocation : null, "2",
                this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getVallasApplication().getRefreshOrdenes()) {
            getVallasApplication().setRefreshOrdenes(false);
            loadOrdenes();
        }
    }

    private void setListener(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadOrdenes();
            }

        });

        btnRemoveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByLocation = false;
                filtroUbicacion.setVisibility(View.GONE);
                list.setPadding(10, 0, 10, 0);
                loadOrdenes();
            }
        });
    }

    private void loadOrdenes(){
        if(ordenes!=null) ordenes.clear();
        if(adapter!=null) adapter.SetOnItemClickListener(null);
        adapter = null;
        errorView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);

        new GetOrdenesTask(getActivity(), criteria,
                searchByLocation ? VallasApplication.currentLocation : null, "2",
                OrdenesCerradasFragment.this);
    }

    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            loadOrdenes();
        }
    };

    @Override
    public void onGetOrdersOK(ArrayList<Orden> orders) {
        mSwipeRefreshLayout.setRefreshing(false);

        this.ordenes = orders;

        adapter = new OrdenesAdapter(getActivity(), orders);
        list.setAdapter(adapter);
        adapter.SetOnItemClickListener(new OrdenesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(ordenes.size()>0) {
                    Intent intent = new Intent(getActivity(), OrdenDetalle.class);
                    intent.putExtra("orden", ordenes.get(position));
                    getActivity().startActivityForResult(intent, Constants.REQUEST_ORDEN);
                }
            }
        });


        if(this.ordenes.size()==0){
            emptyText.setVisibility(View.VISIBLE);
        }else{
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetOrdersError(String title, String description) {
        criteria="";
        mSwipeRefreshLayout.setRefreshing(false);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }


    //-------------MainTabListener--------------//
    @Override
    public void onSearch(String criteria, boolean location) {
        this.criteria = criteria;
        searchByLocation = location;
        if(searchByLocation) {
            filtroUbicacion.setVisibility(View.VISIBLE);
            list.setPadding(10, getResources().getDimensionPixelSize(R.dimen.vertical_list_filter_pad), 10, 0);
        }else {
            filtroUbicacion.setVisibility(View.GONE);
            list.setPadding(10, 0, 10, 0);
        }
        loadOrdenes();
    }
}
