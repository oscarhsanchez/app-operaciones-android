package esocial.vallasmobile.app.incidencias;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.OrdenesAdapter;
import esocial.vallasmobile.adapter.UbicacionesMediosAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.ordenes.OrdenDetalle;
import esocial.vallasmobile.app.ubicaciones.UbicacionDetalle;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.MediosListener;
import esocial.vallasmobile.obj.Medio;
import esocial.vallasmobile.tasks.GetUbicacionesMediosTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class IncidenciaSelectMedio extends BaseActivity implements MediosListener {

    private RecyclerView list;
    private TextView emptyText;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private UbicacionesMediosAdapter adapter;
    private ArrayList<Medio> medios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_medios);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list = (RecyclerView) findViewById(R.id.medios_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emptyText = (TextView) findViewById(R.id.empty_ub_medios);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setItemAnimator(new DefaultItemAnimator());

        String pk_ubicacion = "";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            pk_ubicacion = extras.getString("pk_ubicacion");
        }

        initToolBar();

        new GetUbicacionesMediosTask(this, pk_ubicacion, this);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(5, 0);
        setSupportActionBar(toolbar);

        ImageView backButton = (ImageView) findViewById(R.id.inc_action_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onGetMediosOK(ArrayList<Medio> medios) {
        progressBar.setVisibility(View.GONE);

        this.medios = medios;

        adapter = new UbicacionesMediosAdapter(this, this.medios);
        list.setAdapter(adapter);

        adapter.SetOnItemClickListener(new UbicacionesMediosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(IncidenciaSelectMedio.this.medios.size()>0) {
                    Intent intent = new Intent();
                    intent.putExtra("medio", IncidenciaSelectMedio.this.medios.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        if (this.medios.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetMediosError(String title, String description) {
        progressBar.setVisibility(View.GONE);
        Dialogs.showAlertDialog(this, title, description);
    }
}
