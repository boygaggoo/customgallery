package net.mobindustry.customgallery;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mobindustry.customgallery.fragments.PageFragment;
import net.mobindustry.customgallery.holder.ListFoldersHolder;
import net.mobindustry.customgallery.utils.Const;
import net.mobindustry.customgallery.utils.ImagesObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewPagerActivity extends FragmentActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private int photos = ListFoldersHolder.getList().size();
    private ImageView image;
    private int photoNumber;
    private FrameLayout send;
    private FrameLayout cancel;
    private TextView textSend;
    private TextView textCancel;
    private LinearLayout linearLayout;

    public int getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(int photoNumber) {
        this.photoNumber = photoNumber;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PhotoViewPagerActivity.this, TransparentActivity.class);
        intent.putExtra("choice", Const.SELECTED_FOLDER_FRAGMENT);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_pager_activity);
        pager = (ViewPager) findViewById(R.id.pager);
        send = (FrameLayout) findViewById(R.id.buttonSendPhoto);
        cancel = (FrameLayout) findViewById(R.id.buttonCancelPhoto);
        send.setBackground(CustomGallery.getGalleryOptions().getButtonBackground());
        cancel.setBackground(CustomGallery.getGalleryOptions().getButtonBackground());
        textSend=(TextView)findViewById(R.id.photoTextButtonSend);
        textSend.setTextColor(CustomGallery.getGalleryOptions().getTextColorInButtons());
        textCancel=(TextView)findViewById(R.id.photoTextButtonCancel);
        textCancel.setTextColor(CustomGallery.getGalleryOptions().getTextColorInButtons());
        toolbar = (Toolbar) findViewById(R.id.toolbar_photo);
        image = (ImageView) findViewById(R.id.photoBig);
        linearLayout=(LinearLayout)findViewById(R.id.layoutViewPager);
        linearLayout.setBackgroundColor(CustomGallery.getGalleryOptions().getColorBackground());
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(ListFoldersHolder.getCurrentSelectedPhoto());
        pager.setOffscreenPageLimit(4);
        setPhotoNumber(ListFoldersHolder.getCurrentSelectedPhoto());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFoldersHolder.setListForSending(null);
                Intent intent = new Intent();
                intent.putExtra("choice", Const.SEND_FOLDER_FRAGMENT);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                    list.add(ListFoldersHolder.getListForSending().get(i).getPath());
                }
                CustomGallery.getCallBack().sendClicked(list);
                finish();
            }
        });

        if (!ListFoldersHolder.getList().get(getPhotoNumber()).isCheck()) {
            image.setImageResource(R.drawable.circle);
        } else {
            image.setImageResource(R.drawable.ic_attach_check);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int positionForUser = position + 1;
                setPhotoNumber(position);
                toolbar.setTitle(positionForUser + " of " + photos);
                if (!ListFoldersHolder.getList().get(getPhotoNumber()).isCheck()) {
                    image.setImageResource(R.drawable.circle);
                } else {
                    image.setImageResource(R.drawable.ic_attach_check);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorToolbar());
        toolbar.setNavigationIcon(CustomGallery.getGalleryOptions().getDrawableNavigationIconToolbar());
        int positionForUser = ListFoldersHolder.getCurrentSelectedPhoto() + 1;
        toolbar.setTitle(positionForUser + " of " + photos);
        toolbar.setTitleTextColor(CustomGallery.getGalleryOptions().getTextColorInToolbar());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoViewPagerActivity.this, TransparentActivity.class);
                intent.putExtra("choice", Const.SELECTED_FOLDER_FRAGMENT);
                startActivity(intent);
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ListFoldersHolder.getList().get(getPhotoNumber()).isCheck()) {
                    image.setImageResource(R.drawable.ic_attach_check);
                    ListFoldersHolder.getList().get(getPhotoNumber()).setCheck(true);
                    if (ListFoldersHolder.getListForSending() == null) {
                        ListFoldersHolder.setListForSending(new ArrayList<ImagesObject>());
                        String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                        ImagesObject imagesObject = new ImagesObject();
                        imagesObject.setPath(path);
                        ListFoldersHolder.getListForSending().add(imagesObject);
                    } else {
                        String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                        List<ImagesObject> list = ListFoldersHolder.getListForSending();
                        ImagesObject imagesObject = new ImagesObject();
                        imagesObject.setPath(path);
                        list.add(imagesObject);
                        ListFoldersHolder.setListForSending(list);
                    }
                    ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getListForSending().size());
                } else {
                    if (ListFoldersHolder.getCheckQuantity() < 10) {
                        image.setImageResource(R.drawable.circle);
                        ListFoldersHolder.getList().get(getPhotoNumber()).setCheck(false);
                        if (ListFoldersHolder.getListForSending() == null) {
                            ListFoldersHolder.setListForSending(new ArrayList<ImagesObject>());
                            String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                            for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                                if ((ListFoldersHolder.getListForSending().get(i)).getPath().equals(path)) {
                                    ListFoldersHolder.getListForSending().remove(ListFoldersHolder.getListForSending().get(i));
                                }

                            }

                        } else {
                            String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                            for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                                if ((ListFoldersHolder.getListForSending().get(i)).getPath().equals(path)) {
                                    ListFoldersHolder.getListForSending().remove(ListFoldersHolder.getListForSending().get(i));
                                }
                            }
                        }
                        ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getListForSending().size());
                    }
                }
            }
        });

    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position, ListFoldersHolder.getList().get(position).getFile().getAbsolutePath());
        }

        @Override
        public int getCount() {
            return ListFoldersHolder.getList().size();
        }
    }

}
