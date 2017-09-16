package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import suman.dev.strocks.Model.HomeworkList;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.R;

/**
 * Created by sshan on 9/16/2017.
 */

public class HomeworkListAdapter extends RecyclerView.Adapter<HomeworkListAdapter.ViewHolder>  {
    private static ItemClickListener clickListener;
    private ArrayList<HomeworkList> dataSet;

    public HomeworkListAdapter(ArrayList<HomeworkList> homework){
        this.dataSet=homework;
    }

    @Override
    public HomeworkListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homework_list_teacher, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeworkListAdapter.ViewHolder holder, int position) {
        HomeworkList m = dataSet.get(position);
        holder.lblFileUrl.setText(m.FileUrl);
        holder.lblEndDate.setText("End Date: "+m.EndDate);
        holder.lblTeacherNote.setText(m.TeacherNote);
    }

    @Override
    public int getItemCount() {
        if(dataSet!=null)
            return dataSet.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView lblSubjectName, lblFileUrl, lblTeacherNote,lblEndDate;

        public ViewHolder(View itemView) {
            super(itemView);
            lblEndDate= (TextView)itemView.findViewById(R.id.lblEndDate1);
            lblSubjectName = (TextView)itemView.findViewById(R.id.lblSubjectName);
            lblFileUrl = (TextView)itemView.findViewById(R.id.lblFileUrl);
            lblTeacherNote = (TextView)itemView.findViewById(R.id.lblTeacherNote);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
