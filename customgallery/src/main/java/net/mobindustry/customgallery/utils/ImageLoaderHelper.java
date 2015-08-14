package net.mobindustry.customgallery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import net.mobindustry.customgallery.CustomGallery;
import net.mobindustry.customgallery.holder.DataHolder;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.io.InputStream;

public class ImageLoaderHelper {

    private static class CustomImageDownloader extends BaseImageDownloader {

        public CustomImageDownloader(Context context) {
            super(context);
        }

        @Override
        public InputStream getStream(String imageUri, Object extra) throws IOException {
            return super.getStream(imageUri, extra);
        }
    }

    private static ImageLoader imageLoader = initImageLoader();

    private static DisplayImageOptions defaultOptionsFadeIn = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(500))
            .build();


    private static ImageLoader initImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DataHolder.getContext())
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(defaultOptionsFadeIn)
                .imageDownloader(new CustomImageDownloader(DataHolder.getContext())) // setup custom loader
                .build();

        imageLoader.init(config);
        return imageLoader;
    }

    public static void displayImage(final String url, final ImageView imageView) {
        imageLoader.displayImage(url, imageView, defaultOptionsFadeIn);
    }

    public static void displayImageList(final String url, final ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .showImageOnLoading(CustomGallery.getGalleryOptions().getDrawablePlaceHolder())
                .build();
        imageLoader.displayImage(url, imageView, options);
    }

}
