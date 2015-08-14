package net.mobindustry.customgallery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import net.mobindustry.customgallery.CustomGallery;
import net.mobindustry.customgallery.R;
import net.mobindustry.customgallery.holder.ListFoldersHolder;
import net.mobindustry.customgallery.utils.Const;
import net.mobindustry.customgallery.utils.FileWithIndicator;
import net.mobindustry.customgallery.utils.ImagesObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FolderAdapter extends ArrayAdapter<FileWithIndicator>  {
    //TODO create a simple animation for button check
    private LayoutInflater inflater;
    private ImageView photo;
    private View.OnClickListener clickListener;


    public FolderAdapter(Context context, final LoadPhotos loadPhotos) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileWithIndicator galleryFolder = (FileWithIndicator) v.getTag();
                if (galleryFolder.isCheck()) {
                    galleryFolder.setCheck(false);
                    for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                            if ((ListFoldersHolder.getListForSending().get(i)).getPath().equals(galleryFolder.getFile().getAbsolutePath())){
                                ListFoldersHolder.getListForSending().remove(ListFoldersHolder.getListForSending().get(i));
                            }

                    }
                    ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getCheckQuantity() - 1);
                    loadPhotos.load();
                } else {
                    if (ListFoldersHolder.getCheckQuantity()<10){
                        galleryFolder.setCheck(true);
                        ImagesObject imagesObject=new ImagesObject();
                        imagesObject.setPath(galleryFolder.getFile().getAbsolutePath());
                        if (ListFoldersHolder.getListForSending()==null){
                            ListFoldersHolder.setListForSending(new ArrayList<ImagesObject>());
                        }
                        ListFoldersHolder.getListForSending().add(imagesObject);
                        ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getCheckQuantity() + 1);
                        loadPhotos.load();
                    }
                }
                notifyDataSetChanged();

            }
        };

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileWithIndicator galleryFolder = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_folder_item, parent, false);

        }
        ImageView image = (ImageView) convertView.findViewById(R.id.photoFolder);
        image.setOnClickListener(clickListener);
        image.setTag(galleryFolder);

        photo = (ImageView) convertView.findViewById(R.id.imagePhoto);
        if (galleryFolder.isCheck()) {
            image.setImageResource(R.drawable.ic_attach_check);
        } else {
            image.setImageResource(R.drawable.circle);
        }

        if (galleryFolder != null) {
            if (!galleryFolder.getThumbPhoto().equals("")){
                Picasso.with(getContext()).load(Const.IMAGE_LOADER_PATH_PREFIX + galleryFolder.getThumbPhoto())
                        .placeholder(CustomGallery.getGalleryOptions().getDrawablePlaceHolder()).into(photo);
            } else {
                Picasso.with(getContext()).load(Const.IMAGE_LOADER_PATH_PREFIX + galleryFolder.getFile().getAbsolutePath())
                        .placeholder(CustomGallery.getGalleryOptions().getDrawablePlaceHolder()).into(photo);
            }

        }
        return convertView;
    }
    public interface LoadPhotos {
        void load();
    }

}

