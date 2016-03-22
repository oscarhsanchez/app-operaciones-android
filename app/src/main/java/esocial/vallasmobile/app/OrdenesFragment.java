package esocial.vallasmobile.app;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.listeners.OrdenesListener;
import esocial.vallasmobile.obj.Order;
import esocial.vallasmobile.tasks.GetOrdenesTask;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

public class OrdenesFragment extends BaseFragment implements OrdenesListener {

    private RecyclerView list;
    private ProgressBar progressBar;
    private TextView emptyText;
    private ErrorView errorView;

   // private OfertasAdapter adapter;
    private ArrayList<Order> orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ordenes, container, false);

        errorView = (ErrorView) v.findViewById(R.id.order_error_view);
        list = (RecyclerView) v.findViewById(R.id.order_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        emptyText = (TextView) v.findViewById(R.id.empty_orders);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new GetOrdenesTask(getActivity(), this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            errorView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            orders.clear();
            new GetOrdenesTask(getActivity(), OrdenesFragment.this);
        }
    };

    @Override
    public void onGetOrdersOK(ArrayList<Order> orders) {
        progressBar.setVisibility(View.GONE);

        this.orders = orders;

        /*adapter = new OfertasAdapter(getActivity(), offers);
        list.setAdapter(adapter);*/

        if(this.orders.size()==0){
            emptyText.setVisibility(View.VISIBLE);
        }else{
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetOrdersError(String title, String description) {
        progressBar.setVisibility(View.GONE);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }
}
