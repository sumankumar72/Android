package suman.dev.strocks.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.EventImage;
import suman.dev.strocks.R;

/**
 * Created by suman on 10/8/17.
 */

public class ImageViewerAdapter extends RecyclerView.Adapter<ImageViewerAdapter.ViewHolder> {
    private ArrayList<EventImage> dataSet;
    private Context mContext;

    public ImageViewerAdapter(ArrayList<EventImage> images, Context context)
    {
        dataSet= images;
        this.mContext = context;
    }


    @Override
    public ImageViewerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list, parent, false);
        ImageViewerAdapter.ViewHolder viewHolder = new ImageViewerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewerAdapter.ViewHolder holder, int position) {
        EventImage img = dataSet.get(position);

        Picasso.with(mContext)
                .load(Const.BASE_URL+"/school/"+img.ImageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public ViewHolder(View itemView){
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
