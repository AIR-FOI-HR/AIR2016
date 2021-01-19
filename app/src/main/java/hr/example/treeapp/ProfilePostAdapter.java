package hr.example.treeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.core.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class ProfilePostAdapter extends BaseAdapter {
    private Context mContext;
    GetPostData getPostData;
    // Constructor
    List<Bitmap> postImages=new ArrayList<>();
    int imageWidth;
    public ProfilePostAdapter(Context c, List<Bitmap> postImages, int imageWidth) {
        mContext = c;
        this.postImages=postImages;
        this.imageWidth=imageWidth;
    }

    public int getCount() {
        return postImages.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(postImages.get(position));
        return imageView;
    }

}
