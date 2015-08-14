package net.mobindustry.customgallery.fragments;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.mobindustry.customgallery.CustomGallery;
import net.mobindustry.customgallery.R;
import net.mobindustry.customgallery.TransparentActivity;
import net.mobindustry.customgallery.adapters.GalleryAdapter;
import net.mobindustry.customgallery.holder.ListFoldersHolder;
import net.mobindustry.customgallery.utils.Const;
import net.mobindustry.customgallery.utils.FileWithIndicator;
import net.mobindustry.customgallery.utils.FolderCustomGallery;
import net.mobindustry.customgallery.utils.ImagesFromMediaStore;
import net.mobindustry.customgallery.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GalleryFragment extends Fragment {

    private GridView gridList;
    private GalleryAdapter galleryAdapter;
    private List<ImagesFromMediaStore> listImagesMediaStore = new ArrayList<>();
    private Set<String> listDirLink = new HashSet<>();
    private List<FolderCustomGallery> listFolders = new ArrayList<>();
    private List<String> dirLinkFolders;
    private FrameLayout buttonCancel;
    private FrameLayout buttonSend;
    private TextView textSend;
    private TextView textCancel;
    private Map<Long, String> map;
    private Map<Long, String> mapForCustomThumbs;
    private Toolbar toolbar;
    private TransparentActivity activity;
    private boolean checkToolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);
        gridList = (GridView) view.findViewById(R.id.gridList);
        gridList.setBackgroundColor(CustomGallery.getGalleryOptions().getColorBackgroundGridView());
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_gallery);
        buttonCancel = (FrameLayout) view.findViewById(R.id.buttonCancelGallery);
        buttonSend = (FrameLayout) view.findViewById(R.id.buttonSendGallery);
        textSend=(TextView)view.findViewById(R.id.galleryTextButtonSend);
        textSend.setTextColor(CustomGallery.getGalleryOptions().getTextColorInButtons());
        textCancel=(TextView)view.findViewById(R.id.galleryTextButtonCancel);
        textCancel.setTextColor(CustomGallery.getGalleryOptions().getTextColorInButtons());
        buttonSend.setBackground(CustomGallery.getGalleryOptions().getButtonBackground());
        buttonCancel.setBackground(CustomGallery.getGalleryOptions().getButtonBackground());
        activity = (TransparentActivity) getActivity();
        return view;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        checkDevice();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        galleryAdapter = new GalleryAdapter(getActivity());
        gridList.setAdapter(galleryAdapter);
        if (ListFoldersHolder.getListFolders() == null) {
            AsyncMediaStore asyncMediaStore = new AsyncMediaStore();
            asyncMediaStore.execute();
        } else {
            checkDevice();
        }
        if (ListFoldersHolder.getCheckQuantity() > 0) {
            checkToolbar = true;
            toolbar.setTitle(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorSelectedToolbar());
        } else {
            checkToolbar = false;
            toolbar.setTitle(R.string.photos);
            toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorToolbar());
        }
        toolbar.setNavigationIcon(CustomGallery.getGalleryOptions().getDrawableNavigationIconToolbar());
        toolbar.setTitleTextColor(CustomGallery.getGalleryOptions().getTextColorInToolbar());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkToolbar) {
                    getActivity().finish();
                } else {
                    checkToolbar=false;
                    ListFoldersHolder.setListForSending(null);
                    ListFoldersHolder.setCheckQuantity(0);
                    ListFoldersHolder.setListImages(null);
                    for (int i = 0; i < ListFoldersHolder.getList().size(); i++) {
                        ListFoldersHolder.getList().get(i).setCheck(false);
                    }
                    galleryAdapter.notifyDataSetChanged();
                    toolbar.setTitle(R.string.photos);
                    toolbar.setBackgroundColor(CustomGallery.getGalleryOptions().getColorToolbar());
                    AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
                    animation1.setDuration(200);
                    toolbar.setAnimation(animation1);
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                ListFoldersHolder.setListForSending(null);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String>list=new ArrayList<String>();
                for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                    list.add(ListFoldersHolder.getListForSending().get(i).getPath());
                }
                CustomGallery.getCallBack().sendClicked(list);
                getActivity().finish();
            }
        });


        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListFoldersHolder.setList(ListFoldersHolder.getListFolders().get(position).getPhotosInFolder());
                ListFoldersHolder.setNameHolder(ListFoldersHolder.getListFolders().get(position).getName());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FolderFragment folderFragment = new FolderFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
                fragmentTransaction.replace(R.id.transparent_content, folderFragment);
                fragmentTransaction.commit();
            }
        });

    }

    private void checkThumbsInFolder() {
        mapForCustomThumbs = new HashMap<Long, String>();
        List<File> files = getListFiles(new File(Const.PATH_TO_THUMBS_GALLERY));
        Log.e("Log", "ListFoldersHolder.getLinkToCustomThumbs() " + files.size());
        if (files.size() > 0) {
            Log.e("Log", "SIZE " + files.size());
            for (int i = 0; i < files.size(); i++) {
                Long idForMap = Long.valueOf(separateName(files.get(i).getAbsolutePath()));
                String pathForMap = files.get(i).getAbsolutePath();
                mapForCustomThumbs.put(idForMap, pathForMap);
            }
        }

    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg")) {
                    inFiles.add(file);
                }
            }
            return inFiles;
        }
        return new ArrayList<File>();

    }

    private String separateName(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);
        int pos = filename.lastIndexOf(".");
        String name = filename.substring(0, pos);
        return name;
    }

    private void adjustGridViewPortTablet() {
        gridList.setHorizontalSpacing(4);
        gridList.setVerticalSpacing(4);
        gridList.setNumColumns(3);
    }

    private void adjustGridViewLandTablet() {
        gridList.setHorizontalSpacing(4);
        gridList.setVerticalSpacing(4);
        gridList.setNumColumns(4);
    }

    private void adjustGridViewLand() {
        gridList.setHorizontalSpacing(4);
        gridList.setVerticalSpacing(4);
        gridList.setNumColumns(3);
    }

    private void adjustGridViewPort() {
        gridList.setHorizontalSpacing(4);
        gridList.setVerticalSpacing(4);
        gridList.setNumColumns(GridView.AUTO_FIT);
    }

    public Map<Long, String> getThumbAll() {
        Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        map = new HashMap<Long, String>();
        String[] projection = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(getActivity().getContentResolver(), uri,
                MediaStore.Images.Thumbnails.MINI_KIND, projection);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                File file = new File(path);
                if (file.canRead()) {
                    int idxId = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
                    long id = cursor.getLong(idxId);
                    map.put(id, path);
                }
            }
            cursor.close();
        }
        return map;
    }

    private void getAllImages() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.IS_PRIVATE, MediaStore.Images.Media._ID};
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ImagesFromMediaStore images = new ImagesFromMediaStore();
                int idxData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                images.setData(cursor.getString(idxData));
                int idxId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                images.setId(cursor.getLong(idxId));
                int idxSize = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
                images.setSize(cursor.getString(idxSize));
                int idxDisplayName = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                images.setDisplayName(cursor.getString(idxDisplayName));
                int idxTitle = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);
                images.setTitle(cursor.getString(idxTitle));
                int idxMime = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);
                images.setMimType(cursor.getString(idxMime));
                int idxBucketName = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);
                images.setBucketDisplayName(cursor.getString(idxBucketName));
                int idxIsPrivate = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.IS_PRIVATE);
                images.setIsPrivate(cursor.getString(idxIsPrivate));
                if (images.getData() != null) {
                    File file = new File(images.getData());
                    long size = checkSizeInKB(file);
                    if (size > 50) {
                        listImagesMediaStore.add(images);
                    }
                } else {
                    cursor.moveToNext();
                }

            }
            cursor.close();
        }

    }

    private long checkSizeInKB(File file) {
        long fileSizeInBytes = file.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        return fileSizeInKB;
    }


    private List<FileWithIndicator> getPhotosFromFolder(List<ImagesFromMediaStore> list) {
        List<FileWithIndicator> listPhotos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getData().contains((".jpg")) || list.get(i).getData().contains(".jpeg") || list.get(i).getData().contains(".png")) {
                FileWithIndicator fileWithIndicator = new FileWithIndicator();
                File file = new File(list.get(i).getData());
                if (mapForCustomThumbs.get(list.get(i).getId()) != null) {
                    String thumb = mapForCustomThumbs.get(list.get(i).getId());
                    fileWithIndicator.setThumbPhoto(thumb);
                } else {
                    if (map.get(list.get(i).getId()) != null) {
                        if (file.canRead()) {
                            String thumb = map.get(list.get(i).getId());
                            fileWithIndicator.setThumbPhoto(thumb);
                        } else {
                            fileWithIndicator.setThumbPhoto("");
                        }
                    }

                }
                fileWithIndicator.setFile(file);
                fileWithIndicator.setCheck(false);
                listPhotos.add(fileWithIndicator);
            }
        }
        return listPhotos;
    }


    private void completeListFolders() {
        for (int i = 0; i < dirLinkFolders.size(); i++) {
            FolderCustomGallery folderCustomGallery = new FolderCustomGallery();
            folderCustomGallery.setName(getDirNames(dirLinkFolders.get(i)));
            folderCustomGallery.setPath(dirLinkFolders.get(i));
            List<ImagesFromMediaStore> fromMediaStoreList = getListImagesMediaStoreInFolder(dirLinkFolders.get(i));
            folderCustomGallery.setPhotosInFolder(getPhotosFromFolder(fromMediaStoreList));
            folderCustomGallery.setPhotosQuantity(String.valueOf(getPhotosFromFolder(fromMediaStoreList).size()));
            if (folderCustomGallery.getPhotosInFolder().size() == 0) {
                continue;
            } else {
                folderCustomGallery.setFirstPhoto(folderCustomGallery.getPhotosInFolder().get(0).getFile().getAbsolutePath());
                folderCustomGallery.setFirstThumb(folderCustomGallery.getPhotosInFolder().get(0).getThumbPhoto());
                listFolders.add(folderCustomGallery);
            }

        }
    }


    private void getFoldersPath() {
        for (int i = 0; i < listImagesMediaStore.size(); i++) {
            String dirLink = "";
            String uri = listImagesMediaStore.get(i).getData();
            if (uri != null) {
                String[] segments = uri.split("/");
                for (int j = 0; j < segments.length - 1; j++) {
                    dirLink = dirLink + segments[j] + "/";
                }
                listDirLink.add(dirLink);
            }
        }
        String[] dirLink = new String[listDirLink.size()];
        listDirLink.toArray(dirLink);
        dirLinkFolders = new ArrayList<String>(Arrays.asList(dirLink));
    }


    private List<ImagesFromMediaStore> getListImagesMediaStoreInFolder(String folderPath) {
        List<ImagesFromMediaStore> listPhotosInFolder = new ArrayList<>();
        for (int i = 0; i < listImagesMediaStore.size(); i++) {
            if (listImagesMediaStore.get(i).getData().contains(folderPath)) {
                listPhotosInFolder.add(listImagesMediaStore.get(i));
            }
        }
        return listPhotosInFolder;
    }

    private String getDirNames(String path) {
        String[] segments = path.split("/");
        String nameDir = segments[segments.length - 1];
        return nameDir;
    }

    private void checkDevice() {
        if (Utils.isTablet(getActivity()) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.progressBarGone();
            adjustGridViewPortTablet();
            galleryAdapter.clear();
            galleryAdapter.addAll(ListFoldersHolder.getListFolders());
        } else {
            if (Utils.isTablet(getActivity()) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.progressBarGone();
                adjustGridViewLandTablet();
                galleryAdapter.clear();
                galleryAdapter.addAll(ListFoldersHolder.getListFolders());
            } else {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
                    activity.progressBarGone();
                    adjustGridViewLand();
                    galleryAdapter.clear();
                    galleryAdapter.addAll(ListFoldersHolder.getListFolders());
                } else {
                    activity.progressBarGone();
                    adjustGridViewPort();
                    galleryAdapter.clear();
                    galleryAdapter.addAll(ListFoldersHolder.getListFolders());
                }
            }
        }
    }

    private class AsyncMediaStore extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            getAllImages();
            Log.e("Log", "getAllImages() " + listImagesMediaStore.size());
            getThumbAll();
            Log.e("Log", "getThumbAll() " + map.size());
            checkThumbsInFolder();
            Log.e("Log", "checkThumbsInFolder() " + mapForCustomThumbs.size());
            getFoldersPath();
            completeListFolders();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListFoldersHolder.setListFolders(listFolders);
            if (getActivity() != null) {
                checkDevice();
            }
        }
    }

}



