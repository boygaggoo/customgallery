package net.mobindustry.customgallery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.customgallery.CustomGallery;
import net.mobindustry.customgallery.R;
import net.mobindustry.customgallery.utils.Const;
import net.mobindustry.customgallery.utils.FolderCustomGallery;
import com.squareup.picasso.Picasso;


public class GalleryAdapter extends ArrayAdapter<FolderCustomGallery> {
    private LayoutInflater inflater;

    public GalleryAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }


        ImageView firstPhoto = (ImageView) convertView.findViewById(R.id.imagePhoto);
        TextView nameFolder = (TextView) convertView.findViewById(R.id.nameFolder);
        TextView photosFolder = (TextView) convertView.findViewById(R.id.quantityPhotos);

        FolderCustomGallery galleryFolder = getItem(position);

        if (galleryFolder != null) {
            if (galleryFolder.getFirstThumb().equals("")) {
                Picasso.with(getContext()).load(Const.IMAGE_LOADER_PATH_PREFIX + galleryFolder.getFirstPhoto())
                        .placeholder(CustomGallery.getGalleryOptions().getDrawablePlaceHolder()).into(firstPhoto);
            } else {
                Picasso.with(getContext()).load(Const.IMAGE_LOADER_PATH_PREFIX + galleryFolder.getFirstThumb())
                        .placeholder(CustomGallery.getGalleryOptions().getDrawablePlaceHolder()).into(firstPhoto);
            }
            nameFolder.setText(galleryFolder.getName());
            if (Integer.valueOf(galleryFolder.getPhotosQuantity()) > 1000) {
                photosFolder.setText("> 1k");
            } else {
                photosFolder.setText(galleryFolder.getPhotosQuantity());
            }

        }
        return convertView;
    }

}

