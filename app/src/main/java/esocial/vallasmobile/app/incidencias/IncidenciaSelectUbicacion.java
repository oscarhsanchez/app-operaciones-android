package esocial.vallasmobile.app.incidencias;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.UbicacionesAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.app.ubicaciones.UbicacionDetalle;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.UbicacionesListener;
import esocial.vallasmobile.obj.Ubicacion;
import esocial.vallasmobile.tasks.GetUbicacionesTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class IncidenciaSelectUbicacion extends BaseActivity implements UbicacionesListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView list;
    private TextView emptyText;
    private ErrorView errorView;
    private SearchView searchView;
    private Toolbar toolbar;

    private UbicacionesAdapter adapter;
    private ArrayList<Ubicacion> ubicaciones;
    LinearLayoutManager mLayoutManager;

    private Integer from = 0;
    private Integer num = 40;
    private int previousTotal = 20;
    private boolean loading = false;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private String criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_ubicaciones);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.ub_refresh);
        errorView = (ErrorView) findViewById(R.id.location_error_view);
        list = (RecyclerView) findViewById(R.id.location_list);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        emptyText = (TextView) findViewById(R.id.empty_ubicaciones);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        initToolBar();
        setListeners();
        loadMore();
    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(5, 0);
        setSupportActionBar(toolbar);

        ImageView backButton = (ImageView) findViewById(R.id.action_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.parseColor("#bbffffff"));
        searchAutoComplete.setTextColor(Color.parseColor("#DDFFFFFF"));
        searchAutoComplete.setTextSize(16);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchWithCriteria(searchAutoComplete.getText().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSwipeRefreshLayout.setRefreshing(true);
                criteria = "";
                clearListView();
                loadMore();
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));
    }

    private void setListeners() {
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

    private void setAdapterListener() {
        adapter.SetOnItemClickListener(new UbicacionesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(IncidenciaSelectUbicacion.this, IncidenciaSelectMedio.class);
                intent.putExtra("pk_ubicacion", ubicaciones.get(position).pk_ubicacion);
                startActivityForResult(intent, Constants.REQUEST_SELECT_MEDIO);
            }
        });
    }

    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            errorView.setVisibility(View.GONE);
            clearListView();
            mSwipeRefreshLayout.setRefreshing(true);
            loadMore();
        }
    };


    private void loadMore() {
        if (loading) return;
        loading = true;

        new GetUbicacionesTask(this, criteria, from, num, IncidenciaSelectUbicacion.this);
    }

    public void searchWithCriteria(String criteria) {
        clearListView();
        this.criteria = criteria;
        mSwipeRefreshLayout.setRefreshing(true);
        loadMore();
    }

    private void clearListView() {
        from = 0;
        ubicaciones = new ArrayList<>();
        if (adapter != null) adapter.notifyDataSetChanged();
        adapter = null;
        list.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void onGetUbicacionesOK(ArrayList<Ubicacion> ubicaciones) {
        mSwipeRefreshLayout.setRefreshing(false);

        if (this.ubicaciones == null) this.ubicaciones = new ArrayList<>();
        this.ubicaciones.addAll(ubicaciones);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new UbicacionesAdapter(this, this.ubicaciones);
            list.setAdapter(adapter);
            setAdapterListener();
        }


        if (this.ubicaciones.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }

        from += ubicaciones.size();
        loading = false;
    }

    @Override
    public void onGetUbicacionesError(String title, String description) {
        mSwipeRefreshLayout.setRefreshing(false);
        criteria = "";
        list.setVisibility(View.INVISIBLE);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_SELECT_MEDIO && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra("medio", data.getSerializableExtra("medio"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }


}
