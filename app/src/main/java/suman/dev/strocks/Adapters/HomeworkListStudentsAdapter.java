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

public class HomeworkListStudentsAdapter extends RecyclerView.Adapter<HomeworkListStudentsAdapter.ViewHolder>  {
    private HomeworkList dataSet;

    public HomeworkListStudentsAdapter(HomeworkList homework){
        this.dataSet=homework;
    }

    @Override
    public HomeworkListStudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homework_list_students, parent, false);
        HomeworkListStudentsAdapter.ViewHolder viewHolder = new HomeworkListStudentsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeworkListStudentsAdapter.ViewHolder holder, int position) {
        holder.lblName.setText(dataSet.Students.get(position).StudentName);
        holder.lblStudentNote.setText(dataSet.Students.get(position).StudentNote);
        holder.lblSubmittedDate.setText(dataSet.Students.get(position).SubmittedDate);
    }

    @Override
    public int getItemCount() {
        if(dataSet!=null)
            return dataSet.Students.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lblStudentNote, lblFileUrl, lblSubmittedDate, lblName;

        public ViewHolder(View itemView) {
            super(itemView);
            lblName =  (TextView)itemView.findViewById(R.id.lblName);
            lblStudentNote = (TextView)itemView.findViewById(R.id.lblStudentNote);
            lblFileUrl = (TextView)itemView.findViewById(R.id.lblFileUrl);
            lblSubmittedDate = (TextView)itemView.findViewById(R.id.lblSubmittedDate);
        }
    }
}
