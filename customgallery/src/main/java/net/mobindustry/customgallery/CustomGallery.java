package net.mobindustry.customgallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import net.mobindustry.customgallery.holder.ListFoldersHolder;
import net.mobindustry.customgallery.services.CreateGalleryThumbs;
import net.mobindustry.customgallery.utils.Const;

import java.util.List;

public class CustomGallery {

    private Activity activity;
    private static GalleryClickCallBack callBack;
    private static GalleryOptions galleryOptions;

    public CustomGallery(Activity activity, GalleryOptions galleryOption1, GalleryClickCallBack callBack1) {
        this.activity = activity;
        callBack = callBack1;
        galleryOptions = galleryOption1;
    }

    public static void startCreateThumbsService(Context context) {
        context.startService(new Intent(context, CreateGalleryThumbs.class));
    }

    public void startGallery() {
        Intent intentGallery = new Intent(activity, TransparentActivity.class);
        intentGallery.putExtra("choice", Const.GALLERY_FRAGMENT);
        activity.startActivity(intentGallery);
        ListFoldersHolder.setCheckQuantity(0);
        ListFoldersHolder.setListFolders(null);
        ListFoldersHolder.setList(null);
        ListFoldersHolder.setListForSending(null);
        ListFoldersHolder.setListImages(null);
    }

    public interface GalleryClickCallBack {
        void sendClicked(List<String> list);
    }

    public static GalleryClickCallBack getCallBack() {
        return callBack;
    }

    public static GalleryOptions getGalleryOptions() {
        return galleryOptions;
    }
}
