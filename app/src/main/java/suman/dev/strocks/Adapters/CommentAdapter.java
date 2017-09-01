package suman.dev.strocks.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.PTAForumComment;
import suman.dev.strocks.R;

/**
 * Created by suman on 8/8/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<PTAForumComment> dataSet;
    private static ItemClickListener clickListener;

    public CommentAdapter(ArrayList<PTAForumComment> comments){
        dataSet =comments;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pta_comment_list, parent, false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PTAForumComment comment  = dataSet.get(position);
        holder.txtCommentDetail.setText(comment.Comment);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtCommentDetail;
        public View view;

        public ViewHolder(View v){
            super(v);
            view = v;
            txtCommentDetail = (TextView)view.findViewById(R.id.txtCommentDetail);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
