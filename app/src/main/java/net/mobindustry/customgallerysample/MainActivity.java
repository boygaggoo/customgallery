package net.mobindustry.customgallerysample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.alytar.customgallery.R;

import net.mobindustry.customgallery.CustomGallery;
import net.mobindustry.customgallery.GalleryOptions;

import java.util.List;


public class MainActivity extends Activity {

    private Button buttonGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonGallery = (Button) findViewById(R.id.buttonGallery);

        CustomGallery.startCreateThumbsService(this);
        GalleryOptions galleryOptions=new GalleryOptions(this);
        //galleryOptions.setColorToolbar(Color.GREEN);
       // galleryOptions.setColorSelectedToolbar(Color.RED);
       // galleryOptions.setColorBackground(Color.YELLOW);
        //galleryOptions.setColorBackgroundGridView(Color.BLACK);
        //galleryOptions.setTextColorInButtons(Color.BLUE);
        //galleryOptions.setTextColorInToolbar(Color.CYAN);
        //galleryOptions.setDrawableNavigationIconToolbar(getResources().getDrawable(R.drawable.abc_btn_check_material));
        //galleryOptions.setDrawablePlaceHolder(getResources().getDrawable(R.drawable.photo_test));


        final CustomGallery gallery = new CustomGallery(this,galleryOptions, new CustomGallery.GalleryClickCallBack() {
            @Override
            public void sendClicked(List<String> list) {
                for (int i = 0; i < list.size(); i++) {
                    Log.e("Log","path "+list.get(i));
                }
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery.startGallery();
            }
        });
    }
}
