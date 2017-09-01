package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import suman.dev.strocks.Model.Holiday;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.PTAForum;
import suman.dev.strocks.R;

/**
 * Created by suman on 30/7/17.
 */

public class PTAForumAdapter extends RecyclerView.Adapter<PTAForumAdapter.ViewHolder> {

    private static ItemClickListener clickListener;
    private List<PTAForum> dataSet=null;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtViewComment,txtViewLikes,txtViewCommentCount,txtViewPostedTime;
        public View view;

        public ViewHolder(View v)
        {
            super(v);
            txtViewComment = (TextView)itemView.findViewById(R.id.txtViewComment);
            txtViewLikes= (TextView)itemView.findViewById(R.id.txtViewLikes);
            txtViewCommentCount= (TextView)itemView.findViewById(R.id.txtViewCommentCount);
            txtViewPostedTime = (TextView)itemView.findViewById(R.id.txtViewPostedTime);
            txtViewLikes.setOnClickListener(this);
            txtViewCommentCount.setOnClickListener(this);
            view=v;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }

    public PTAForumAdapter(ArrayList<PTAForum> ptaForums)
    {
        dataSet = ptaForums;
    }

    @Override
    public PTAForumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pta_forum_list, parent, false);
        PTAForumAdapter.ViewHolder viewHolder = new PTAForumAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PTAForumAdapter.ViewHolder holder, int position) {
        PTAForum forum = dataSet.get(position);
        holder.txtViewComment .setText(forum.Title);

        if(forum.Comments==null)
            holder.txtViewCommentCount.setText("Comments(0)");
        else
            holder.txtViewCommentCount.setText("Comments("+forum.Comments.size()+")");

        if(forum.Likes==null)
            holder.txtViewLikes.setText("Likes(0)");
        else
            holder.txtViewLikes.setText("Likes("+forum.Likes.size()+")");

        String s = forum.GetTimeAgo();
        holder.txtViewPostedTime.setText(s);

    }

    @Override
    public int getItemCount() {
        if(dataSet==null)
            return 0;
        else
            return dataSet.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


}
