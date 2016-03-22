package esocial.vallasmobile.app;

import android.os.Bundle;

import com.squareup.picasso.Picasso;

import esocial.vallasmobile.R;
import esocial.vallasmobile.components.TouchImageView;

public class FullScreenImage extends BaseActivity {

    private TouchImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        image = (TouchImageView) findViewById(R.id.touch_image);

        String url="";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            url = extras.getString("url");
        }

        Picasso.with(this)
                .load(url)
                .error(R.drawable.logo_orange)
                .into(image);

    }
}
