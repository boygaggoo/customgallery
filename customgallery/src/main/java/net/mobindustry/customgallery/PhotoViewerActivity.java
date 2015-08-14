package net.mobindustry.customgallery;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.mobindustry.customgallery.utils.Const;
import net.mobindustry.customgallery.utils.ImageLoaderHelper;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewerActivity extends Activity {
    int gif = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewer_activity);

        ImageView imageView = (ImageView) findViewById(R.id.photo_image_view);
        ImageView back = (ImageView) findViewById(R.id.photo_view_back);

        FrameLayout.LayoutParams params;
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        imageView.setLayoutParams(params);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String path = getIntent().getStringExtra("file_path");
        gif = getIntent().getIntExtra("gif", 0);
        if (gif == 1) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(500, 500);
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
            Glide.with(this).load(path).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else {
            PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
            ImageLoaderHelper.displayImage(Const.IMAGE_LOADER_PATH_PREFIX + path, imageView);
            mAttacher.update();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
