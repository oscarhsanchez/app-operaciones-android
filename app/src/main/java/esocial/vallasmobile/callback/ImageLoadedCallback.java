package esocial.vallasmobile.callback;

import android.widget.ImageView;

import com.squareup.picasso.Callback;

/**
 * Created by jesus.martinez on 18/03/2016.
 */
public class ImageLoadedCallback implements Callback {
    ImageView image;

    public ImageLoadedCallback(ImageView mImage) {
        image = mImage;
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onError() {
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }
}