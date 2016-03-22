package esocial.vallasmobile.app.ubicaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.UbicacionesMediosAdapter;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.listeners.MediosListener;
import esocial.vallasmobile.obj.Medio;
import esocial.vallasmobile.tasks.GetUbicacionesMediosTask;
import esocial.vallasmobile.utils.Dialogs;

public class UbicacionMediosFragment extends BaseFragment implements MediosListener {

    private LinearLayout list;
    private TextView emptyText;
    private ProgressBar progressBar;

    private UbicacionesMediosAdapter adapter;
    private ArrayList<Medio> medios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ubicaciones_medios, container, false);
        list = (LinearLayout) v.findViewById(R.id.ub_medios_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        emptyText = (TextView) v.findViewById(R.id.empty_ub_medios);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetUbicacionesMediosTask(getActivity(),
                ((UbicacionDetalle) getActivity()).getPkUbicacion(), this);
    }

    @Override
    public void onGetMediosOK(ArrayList<Medio> medios) {
        if (!isActivityFinished() && !isRemoving()) {
            progressBar.setVisibility(View.GONE);

            this.medios = medios;

            adapter = new UbicacionesMediosAdapter(getActivity(), this.medios);
            list.removeAllViews();
            for (int i = 0; i < adapter.getCount(); i++) {
                View v = adapter.getView(i, null, null);
                list.addView(v);
            }

            if (this.medios.size() == 0) {
                emptyText.setVisibility(View.VISIBLE);
                list.setVisibility(View.INVISIBLE);
            } else {
                emptyText.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onGetMediosError(String title, String description) {
        progressBar.setVisibility(View.GONE);
        Dialogs.showAlertDialog(getActivity(), title, description);
    }
}
