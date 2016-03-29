package esocial.vallasmobile.app;

import android.os.Build;
import android.os.Bundle;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import esocial.vallasmobile.R;
import esocial.vallasmobile.components.TouchImageView;

public class FullScreenImage extends BaseActivity {

    private TouchImageView image;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        image = (TouchImageView) findViewById(R.id.touch_image);


        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            url = extras.getString("url");
        }
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
