package esocial.vallasmobile.app;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import esocial.vallasmobile.R;
import esocial.vallasmobile.components.TouchImageView;
import esocial.vallasmobile.obj.Imagen;


/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class FullScreenImage extends BaseActivity {

    private TouchImageView image;
    private String url;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        image = (TouchImageView) findViewById(R.id.touch_image);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
            //Viene de imagenes pendientes
            if (extras.containsKey("imagePosition"))
                data = ((Imagen) VallasApplication.sender.getImages().get(extras.getInt("imagePosition"))).data;

        }

        if (!TextUtils.isEmpty(url)) {
            Picasso.with(this)
                    .load(url)
                    .error(R.drawable.logo_orange)
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            image.setZoom(1);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else if (!TextUtils.isEmpty(data)) {
            byte[] imageAsBytes = Base64.decode(data.getBytes(), Base64.DEFAULT);
            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
