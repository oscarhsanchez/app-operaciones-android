package esocial.vallasmobile.app.ordenes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.OrdenesAdapter;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.OrdenesListener;
import esocial.vallasmobile.obj.Orden;
import esocial.vallasmobile.tasks.GetOrdenesTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

public class OrdenesFragment extends BaseFragment implements OrdenesListener,
                                    MainTabActivity.OnOrdenesFragmentInteractionListener{

    private RecyclerView list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView emptyText;
    private ErrorView errorView;

    private OrdenesAdapter adapter;
    private ArrayList<Orden> ordenes;

    private String criteria = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ordenes, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.orden_refresh);
        errorView = (ErrorView) v.findViewById(R.id.order_error_view);
        list = (RecyclerView) v.findViewById(R.id.order_list);
        emptyText = (TextView) v.findViewById(R.id.empty_orders);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Listener para comunicarnos con tabactivity
        ((MainTabActivity)getActivity()).setOrdenesListener(this);

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
        new GetOrdenesTask(getActivity(), criteria, this);
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
    }

    private void loadOrdenes(){
        if(ordenes!=null) ordenes.clear();
        adapter = null;
        errorView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);

        new GetOrdenesTask(getActivity(), criteria, OrdenesFragment.this);
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
                Intent intent = new Intent(getActivity(), OrdenDetalle.class);
                intent.putExtra("orden", ordenes.get(position));
                getActivity().startActivityForResult(intent, Constants.REQUEST_ORDEN);
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
    public void onSearch(String criteria) {
        this.criteria = criteria;
        loadOrdenes();
    }
}
