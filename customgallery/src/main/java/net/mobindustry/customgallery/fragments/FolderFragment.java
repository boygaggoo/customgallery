package net.mobindustry.customgallery.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import net.mobindustry.customgallery.CustomGallery;
import net.mobindustry.customgallery.PhotoViewPagerActivity;
import net.mobindustry.customgallery.R;
import net.mobindustry.customgallery.TransparentActivity;
import net.mobindustry.customgallery.adapters.FolderAdapter;
import net.mobindustry.customgallery.holder.ListFoldersHolder;
import net.mobindustry.customgallery.utils.Const;
import net.mobindustry.customgallery.utils.Utils;

import java.util.ArrayList;


public class FolderFragment extends Fragment {

    private GridView gridList;
    private FolderAdapter folderAdapter;
    private FrameLayout buttonSend;
    private FrameLayout buttonCancel;
    private TextView textSend;
    private TextView textCancel;
    private Toolbar toolbar;
    private String nameHolder = "";
    private FragmentTransaction ft;
    private boolean checkToolbar;
    private TransparentActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.folder_fragment, container, false);
        nameHolder = ListFoldersHolder.getNameHolder();
        buttonSend = (FrameLayout) view.findViewById(R.id.buttonSendFolder);
        buttonCancel = (FrameLayout) view.findViewById(R.id.buttonCancelFolder);
        buttonSend.setBackground(CustomGallery.getGalleryOptions().getButtonBackground());
        buttonCancel.setBackground(CustomGallery.getGalleryOptions().getButtonBackground());
        textSend=(TextView)view.findViewById(R.id.folderTextButtonSend);
        textSend.setTextColor(CustomGallery.getGalleryOptions().getTextColorInButtons());
        textCancel=(TextView)view.findViewById(R.id.folderTextButtonCancel);
        textCancel.setTextColor(CustomGallery.getGalleryOptions().getTextColorInButtons());
        gridList = (GridView) view.findViewById(R.id.gridPhotos);
        gridList.setBackgroundColor(CustomGallery.getGalleryOptions().getColorBackgroundGridView());
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_folder);
        activity = (TransparentActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.progressBarGone();
        folderAdapter = new FolderAdapter(getActivity(), new FolderAdapter.LoadPhotos() {
            @Override
            public void load() {
                if (ListFoldersHolder.getCheckQuantity() > 0) {
                    checkToolbar=true;
                    toolbar.setTitle(String.valueOf(ListFoldersHolder.getCheckQuantity()));
                    toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorSelectedToolbar());
                } else {
                    checkToolbar=false;
                    toolbar.setTitle(nameHolder);
                    toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorToolbar());
                }
            }

        });

        checkDevice();
        folderAdapter.clear();
        folderAdapter.addAll(ListFoldersHolder.getList());
        gridList.setAdapter(folderAdapter);
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListFoldersHolder.setCurrentSelectedPhoto(position);
                Intent intent = new Intent(getActivity(), PhotoViewPagerActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String>list=new ArrayList<>();
                for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                    list.add(ListFoldersHolder.getListForSending().get(i).getPath());
                }
                CustomGallery.getCallBack().sendClicked(list);
                getActivity().finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFoldersHolder.setListForSending(null);
                getActivity().finish();
            }
        });
        if (ListFoldersHolder.getCheckQuantity() > 0) {
            checkToolbar=true;
            toolbar.setTitle(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorSelectedToolbar());
        } else {
            checkToolbar=false;
            toolbar.setTitle(nameHolder);
            toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorToolbar());
        }
        toolbar.setNavigationIcon(CustomGallery.getGalleryOptions().getDrawableNavigationIconToolbar());
        toolbar.setTitleTextColor(CustomGallery.getGalleryOptions().getTextColorInToolbar());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkToolbar) {
                    GalleryFragment galleryFragment;
                    galleryFragment = new GalleryFragment();
                    ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
                    ft.replace(R.id.transparent_content, galleryFragment);
                    ft.commit();
                } else {
                    checkToolbar = false;
                    ListFoldersHolder.setListForSending(null);
                    ListFoldersHolder.setCheckQuantity(0);
                    ListFoldersHolder.setListImages(null);
                    for (int i = 0; i < ListFoldersHolder.getList().size(); i++) {
                        ListFoldersHolder.getList().get(i).setCheck(false);
                    }
                    folderAdapter.notifyDataSetChanged();
                    toolbar.setTitle(nameHolder);
                    toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorToolbar());
                    AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
                    animation1.setDuration(Const.TIME_ANIM_DURATION);
                    toolbar.setAnimation(animation1);
                }
            }
        });

    }

    private void adjustGridViewPortTablet() {
        gridList.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_horizontal_spacing));
        gridList.setVerticalSpacing((int) getResources().getDimension(R.dimen.grid_vertical_spacing));
        gridList.setNumColumns(getResources().getInteger(R.integer.number_grid_list_columns));
    }

    private void adjustGridViewLandTablet() {
        gridList.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_horizontal_spacing));
        gridList.setVerticalSpacing((int) getResources().getDimension(R.dimen.grid_vertical_spacing));
        gridList.setNumColumns(getResources().getInteger(R.integer.number_grid_list_columns_tablet_land));
    }

    private void adjustGridViewLand() {
        gridList.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_horizontal_spacing));
        gridList.setVerticalSpacing((int) getResources().getDimension(R.dimen.grid_vertical_spacing));
        gridList.setNumColumns(getResources().getInteger(R.integer.number_grid_list_columns));
    }

    private void adjustGridViewPort() {
        gridList.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_horizontal_spacing));
        gridList.setVerticalSpacing((int) getResources().getDimension(R.dimen.grid_vertical_spacing));
        gridList.setNumColumns(getResources().getInteger(R.integer.number_grid_list_columns_tel_port));
    }

    private void checkDevice() {
        if (Utils.isTablet(getActivity()) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            adjustGridViewPortTablet();
            folderAdapter.clear();
            folderAdapter.addAll(ListFoldersHolder.getList());
        } else {
            if (Utils.isTablet(getActivity()) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                adjustGridViewLandTablet();
                folderAdapter.clear();
                folderAdapter.addAll(ListFoldersHolder.getList());
            } else {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
                    adjustGridViewLand();
                    folderAdapter.clear();
                    folderAdapter.addAll(ListFoldersHolder.getList());
                } else {
                    adjustGridViewPort();
                    folderAdapter.clear();
                    folderAdapter.addAll(ListFoldersHolder.getList());
                }
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        checkDevice();
    }


}
