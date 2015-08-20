package net.mobindustry.customgallery.services;


import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import net.mobindustry.customgallery.holder.ListFoldersHolder;
import net.mobindustry.customgallery.utils.Const;
import net.mobindustry.customgallery.utils.ImagesFromMediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateGalleryThumbs extends Service {

    private List<ImagesFromMediaStore> listImagesMediaStore = new ArrayList<>();
    private ExecutorService executorService;
    private Map<Long, String> map = new HashMap<>();


    public void onCreate() {
        super.onCreate();
        Log.e("Log", "Service start");
        executorService = Executors.newFixedThreadPool(1);
        ListFoldersHolder.setListForSending(null);
        ListFoldersHolder.setListImages(null);
    }

    public void onDestroy() {
        Log.e("Log", "Service destroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CompleteFolder myRun = new CompleteFolder();
        executorService.execute(myRun);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class CompleteFolder implements Runnable {
        private void getAllImages() {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.DISPLAY_NAME,
                    MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.IS_PRIVATE, MediaStore.Images.Media._ID};
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
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
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(images.getData(), options);
                        int height = options.outHeight;
                        int width = options.outWidth;
                        File file = new File(images.getData());
                        long size = checkSizeInKB(file);
                        if (height > 0 && width > 0 && size > Const.MIN_SIZE_KB) {
                            listImagesMediaStore.add(images);
                        }
                    } else {
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }

        }


        private File createThumb(String path, String name) {
            //create a file to write bitmap data
            File file = new File(Const.PATH_TO_THUMBS_GALLERY, name);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Convert bitmap to byte array

            Bitmap bitmap;
            try {
                Log.e("Log", "Patch " + path);
                bitmap = BitmapFactory.decodeFile(path);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, Const.THUMBSIZE, Const.THUMBSIZE);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                //write the bytes in file
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bitMapData);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap.recycle();
                thumbImage.recycle();
                return file;
            } catch (Exception e) {
                Log.e("Log", "Error create thumb");
            }
            return null;
        }

        private List<File> getListFiles(File parentDir) {
            ArrayList<File> inFiles = new ArrayList<File>();
            File[] files = parentDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(Const.FORMAT_JPG)) {
                        inFiles.add(file);
                    }
                }
                Log.e("Log", "inFiles " + inFiles.size());
                return inFiles;
            }
            Log.e("Log", "inFiles null" + inFiles.size());
            return inFiles;
        }

        private String separateName(String path) {
            String filename = path.substring(path.lastIndexOf("/") + 1);
            int pos = filename.lastIndexOf(".");
            String name = filename.substring(0, pos);
            return name;
        }

        private void checkThumbsInFolder() {
            List<File> files = getListFiles(new File(Const.PATH_TO_THUMBS_GALLERY));
            Log.e("Log", "checkThumbsInFolder() " + files.size());
            if (files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    Long idForMap = Long.valueOf(separateName(files.get(i).getAbsolutePath()));
                    String pathForMap = files.get(i).getAbsolutePath();
                    map.put(idForMap, pathForMap);
                }
            }

        }

        private void fillFolder() {
            for (int i = 0; i < listImagesMediaStore.size(); i++) {
                if (map.size() == 0) {
                    createThumb(listImagesMediaStore.get(i).getData(), String.valueOf(listImagesMediaStore.get(i).getId()) + Const.FORMAT_JPG);
                } else {
                    if (map.get(listImagesMediaStore.get(i).getId()) == null) {
                        createThumb(listImagesMediaStore.get(i).getData(), String.valueOf(listImagesMediaStore.get(i).getId()) + Const.FORMAT_JPG);
                    } else {
                        continue;
                    }
                }

            }
        }

        private void createFolderForGalleryThumbs() {
            File f = new File(Const.PATH_TO_THUMBS_GALLERY);
            Log.e("Log", "link to folder " + f.getAbsolutePath());
            if (!f.exists()) {
                Log.e("Log", "folder");
                f.mkdirs();
                ListFoldersHolder.setLinkToCustomThumbs(f.getAbsolutePath());
            }
            ListFoldersHolder.setLinkToCustomThumbs(f.getAbsolutePath());
        }

        private long checkSizeInKB(File file) {
            long fileSizeInBytes = file.length();
            long fileSizeInKB = fileSizeInBytes / 1024;
            return fileSizeInKB;
        }


        public void run() {
            createFolderForGalleryThumbs();
            Log.e("Log", "createFolderForGalleryThumbs()");
            getAllImages();
            Log.e("Log", "ALL IMAGES");
            checkThumbsInFolder();
            Log.e("Log", "CHECK THUMBS");
            fillFolder();
            stopSelf();
        }
    }
}
