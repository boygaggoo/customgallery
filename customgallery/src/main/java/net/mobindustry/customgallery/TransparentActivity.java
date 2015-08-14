package net.mobindustry.customgallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import net.mobindustry.customgallery.fragments.FolderFragment;
import net.mobindustry.customgallery.fragments.GalleryFragment;
import net.mobindustry.customgallery.holder.DataHolder;
import net.mobindustry.customgallery.utils.Const;


public class TransparentActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FrameLayout frameLayout;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity);
        frameLayout=(FrameLayout)findViewById(R.id.transparent_layout);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        frameLayout.setBackgroundColor(CustomGallery.getGalleryOptions().getColorBackground());
        DataHolder.setContext(this);

        int choice = getIntent().getIntExtra("choice", 0);

        FrameLayout layout = (FrameLayout) findViewById(R.id.transparent_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (choice) {
            case Const.GALLERY_FRAGMENT: {
                GalleryFragment galleryFragment = new GalleryFragment();
                fragmentTransaction.replace(R.id.transparent_content, galleryFragment);
                break;
            }
            case Const.SELECTED_FOLDER_FRAGMENT: {
                FolderFragment folderFragment = new FolderFragment();
                fragmentTransaction.replace(R.id.transparent_content, folderFragment);
                break;
            }
        }
        fragmentTransaction.commit();

    }

    public void progressBarGone(){
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.transparent_content) instanceof FolderFragment){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            GalleryFragment galleryFragment = new GalleryFragment();
            fragmentTransaction.replace(R.id.transparent_content, galleryFragment);
            fragmentTransaction.commit();
        } else {
            super.onBackPressed();
        }
    }
    
}
