package esocial.vallasmobile.app.ubicaciones;

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
import esocial.vallasmobile.adapter.UbicacionesAdapter;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.UbicacionesListener;
import esocial.vallasmobile.obj.Ubicacion;
import esocial.vallasmobile.tasks.GetUbicacionesTask;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

public class UbicacionesFragment extends BaseFragment implements UbicacionesListener,
        MainTabActivity.OnFragmentInteractionListener{

    private final int REQUEST_SELECT_UBI = 1000;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView list;
    private TextView emptyText;
    private ErrorView errorView;

    private UbicacionesAdapter adapter;
    private ArrayList<Ubicacion> ubicaciones;
    LinearLayoutManager mLayoutManager;

    private Integer from = 0;
    private Integer num = 40;
    private int previousTotal = 20;
    private boolean loading = false;
    private boolean boolFirstSearchDone = false;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int selectedPosition = 0;
    private String criteria;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ubicaciones, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.ub_refresh);
        errorView = (ErrorView) v.findViewById(R.id.location_error_view);
        list = (RecyclerView) v.findViewById(R.id.location_list);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        emptyText = (TextView) v.findViewById(R.id.empty_locations);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Listener para comunicarnos con tabactivity
        ((MainTabActivity)getActivity()).setUbicacionesListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        setListeners();
    }

    private void setListeners(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearListView();
                loadMore();
            }

        });

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = list.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadMore();
                }
            }
        });

    }

    private void setAdapterListener(){
        adapter.SetOnItemClickListener(new UbicacionesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedPosition = position;
                Intent intent = new Intent(getActivity(), UbicacionDetalle.class);
                intent.putExtra("ubicacion", ubicaciones.get(position));
                getActivity().startActivityForResult(intent, REQUEST_SELECT_UBI);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!getActivity().isFinishing() && !boolFirstSearchDone) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadMore();
                }
            });
        }
    }

    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            errorView.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);
            clearListView();
            loadMore();
        }
    };


    private void loadMore() {
        if (loading) return;
        loading = true;
        boolFirstSearchDone = true;

        new GetUbicacionesTask(getActivity(), criteria, from, num, UbicacionesFragment.this);
    }


    private void clearListView() {
        from = 0;
        criteria="";
        ubicaciones = new ArrayList<>();
        if (adapter != null) adapter.notifyDataSetChanged();
        adapter = null;
        list.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void onGetUbicacionesOK(ArrayList<Ubicacion> ubicaciones) {
        mSwipeRefreshLayout.setRefreshing(false);

        if(this.ubicaciones ==null) this.ubicaciones = new ArrayList<>();
        this.ubicaciones.addAll(ubicaciones);

        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new UbicacionesAdapter(getActivity(), this.ubicaciones);
            list.setAdapter(adapter);
            setAdapterListener();
        }


        if(this.ubicaciones.size()==0){
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        }else{
            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }

        from += ubicaciones.size();
        loading = false;
    }

    @Override
    public void onGetUbicacionesError(String title, String description) {
        mSwipeRefreshLayout.setRefreshing(false);
        criteria="";
        list.setVisibility(View.INVISIBLE);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SELECT_UBI){
            adapter.modifyUbicacion(selectedPosition, (Ubicacion) data.getSerializableExtra("ubicacion"));
        }
    }


    @Override
    public void onSearch(String criteria) {
        clearListView();
        this.criteria = criteria;
        mSwipeRefreshLayout.setRefreshing(true);
        loadMore();
    }
}
