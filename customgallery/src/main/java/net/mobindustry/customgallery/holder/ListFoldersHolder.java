package net.mobindustry.customgallery.holder;


import net.mobindustry.customgallery.utils.FileWithIndicator;
import net.mobindustry.customgallery.utils.FolderCustomGallery;
import net.mobindustry.customgallery.utils.ImagesObject;

import java.util.ArrayList;
import java.util.List;

public class ListFoldersHolder {
    private static List<FileWithIndicator> list = new ArrayList<>();
    private static int checkQuantity = 0;
    private static List<FolderCustomGallery> listFolders;
    private static List<ImagesObject> listForSending;
    private static String nameHolder;
    private static int currentSelectedPhoto;
    private static List<String> listImages;
    private static String linkToCustomThumbs="";


    public static List<String> getListImages() {
        return listImages;
    }

    public static void setListImages(List<String> listImages) {
        ListFoldersHolder.listImages = listImages;
    }

    public static String getLinkToCustomThumbs() {
        return linkToCustomThumbs;
    }

    public static void setLinkToCustomThumbs(String linkToCustomThumbs) {
        ListFoldersHolder.linkToCustomThumbs = linkToCustomThumbs;
    }

    public static int getCurrentSelectedPhoto() {
        return currentSelectedPhoto;
    }

    public static void setCurrentSelectedPhoto(int currentSelectedPhoto) {
        ListFoldersHolder.currentSelectedPhoto = currentSelectedPhoto;
    }

    public static String getNameHolder() {
        return nameHolder;
    }

    public static void setNameHolder(String nameHolder) {
        ListFoldersHolder.nameHolder = nameHolder;
    }

    public static List<ImagesObject> getListForSending() {
        return listForSending;
    }

    public static void setListForSending(List<ImagesObject> listForSending) {
        ListFoldersHolder.listForSending = listForSending;
    }

    public static List<FolderCustomGallery> getListFolders() {
        return listFolders;
    }

    public static void setListFolders(List<FolderCustomGallery> listFolders) {
        ListFoldersHolder.listFolders = listFolders;
    }

    public static int getCheckQuantity() {
        return checkQuantity;
    }

    public static void setCheckQuantity(int checkQuantity) {
        ListFoldersHolder.checkQuantity = checkQuantity;
    }

    public static List<FileWithIndicator> getList() {
        return list;
    }

    public static void setList(List<FileWithIndicator> list) {
        ListFoldersHolder.list = list;
    }
}
