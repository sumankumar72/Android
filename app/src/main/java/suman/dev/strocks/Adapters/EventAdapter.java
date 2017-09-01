package suman.dev.strocks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.Events;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.R;

/**
 * Created by suman on 6/8/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Events> dataSet;
    private static ItemClickListener clickListener;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);
        EventAdapter.ViewHolder viewHolder = new EventAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Events event = dataSet.get(position);
        holder.txtEventTitle.setText(event.Title);
        holder.txtViewDescription.setText(event.Article);

        if(event.Images!=null && event.Images.size()>0) {
            Picasso.with(mContext)
                    .load(Const.BASE_URL+"/school/"+event.Images.get(0).ImageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.imgEventImage);
        }
    }

    private void showAllImages()
    {

    }

    @Override
    public int getItemCount() {
        if(dataSet==null)
            return 0;
        else
            return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtViewDescription,txtViewAll,txtEventTitle;
        public ImageView imgEventImage;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            txtViewAll = (TextView)itemView.findViewById(R.id.txtViewAll);
            txtViewDescription =(TextView)itemView.findViewById(R.id.txtViewDescription);
            txtEventTitle = (TextView)itemView.findViewById(R.id.txtEventTitle);
            imgEventImage = (ImageView) itemView.findViewById(R.id.imgEventImage);
            view = itemView;

            txtViewAll.setOnClickListener(this);
            imgEventImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public EventAdapter(ArrayList<Events> events, Context context){
        dataSet = events;
        mContext = context;
    }
}
