package esocial.vallasmobile.tasks;

import android.util.Log;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasImagenesListener;
import esocial.vallasmobile.listeners.OrdenesImagenesListener;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.obj.OrdenImagen;
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 25/04/2016.
 */
public class ImageSender implements OrdenesImagenesListener, IncidenciasImagenesListener, UbicacionesImagenesListener {

    ArrayList<Object> images;
    int count = 0;
    int i = 0;
    boolean showMessage = false;
    boolean isRunning = false;

    public ImageSender() {
        images = new ArrayList<>();
    }

    public ArrayList<Object> getImages(){
        if(images!=null)
            return images;
        else
            return null;
    }

    public int getImagesCount(){
        if(images!=null)
            return images.size();
        else
            return 0;
    }

    public void addImage(IncidenciaImagen image) {
        images.add(image);
    }

    public void addImage(OrdenImagen image) {
        images.add(image);
    }

    public void addImage(UbicacionImagen image) {
        images.add(image);
    }

    public void sendImages(Boolean showMessage) {
        if(isRunning) return;
        isRunning = true;

        count = images.size();
        i = 0;
        this.showMessage = showMessage;
        sendImage();
    }

    public void sendImage() {
        if (count > 0) {
            if (images.get(i) instanceof OrdenImagen) {
                new PostOrdenImagenTask(VallasApplication.context, (OrdenImagen)images.get(i), this);
            } else if (images.get(i) instanceof IncidenciaImagen) {
                new PostIncidenciaImagenTask(VallasApplication.context, (IncidenciaImagen)images.get(i), this);
            } else if (images.get(i) instanceof UbicacionImagen) {
                new PostUbicacionImagenTask(VallasApplication.context, (UbicacionImagen)images.get(i), this);
            }
        }
    }

    @Override
    public void onPostOrderImageOK() {
        requestOk();
    }

    @Override
    public void onPostOrderImageError(String title, String description) {
        count--;
        if (count != 0) {
            i++;
            sendImage();
        }
    }

    @Override
    public void onPostImageOK() {
        requestOk();
    }

    @Override
    public void onPostImageError(String title, String description) {
        count--;
        if (count != 0) {
            i++;
            sendImage();
        }
    }

    @Override
    public void onPostIncidenciaImageOK() {
        requestOk();
    }

    @Override
    public void onPostIncidenciaImageError(String title, String description) {
        count--;
        if (count != 0) {
            i++;
            sendImage();
        }
    }


    private void requestOk(){
        count--;
        images.remove(i);
        if (count == 0) {
            isRunning = false;

            if(showMessage) {
                Dialogs.showAlertDialog(VallasApplication.context, null,
                        VallasApplication.context.getString(R.string.imagenes_enviadas));
                showMessage = false;
            }
            if(Constants.isDebug)
                Log.d("ENVIO DE IMAGENES", "COMPLETADO EL ENVIO DE IMAGENES EN BACKGROUND");
        }else{
            if(count-i != 0)
                sendImage();
            else
                isRunning = false;
        }
    }

    @Override
    public void onGetOrdenesImagenesOK(ArrayList<OrdenImagen> images) {
    }

    @Override
    public void onGetOrdenesImagenesError(String title, String description) {
    }

    @Override
    public void onGetUbicacionesImagenesOK(ArrayList<UbicacionImagen> images) {
    }

    @Override
    public void onGetUbicacionesImagenesError(String title, String description) {
    }

    @Override
    public void onGetIncidenciasImagenesOK(ArrayList<IncidenciaImagen> images) {
    }

    @Override
    public void onGetIncidenciasImagenesError(String title, String description) {
    }


}
