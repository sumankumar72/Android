package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import suman.dev.strocks.Model.UserSubjectData;
import suman.dev.strocks.R;

/**
 * Created by suman on 3/9/17.
 */

public class ResultPostAdapter extends RecyclerView.Adapter<ResultPostAdapter.ViewHolder>  {

    private ArrayList<UserSubjectData> dataSet;

    public ResultPostAdapter(ArrayList<UserSubjectData> subjects){
        this.dataSet = subjects;
    }

    @Override
    public ResultPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.create_result_list, parent, false);
        ResultPostAdapter.ViewHolder viewHolder = new ResultPostAdapter.ViewHolder(v);
        return viewHolder;
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void onBindViewHolder(ResultPostAdapter.ViewHolder holder, int position) {
        UserSubjectData model = dataSet.get(position);
        holder.lblSubject.setText(model.Name);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lblTotalMarks, lblSubject;
        public EditText txtMarksObtained, txtGrdde;
        public View view;

        public ViewHolder(View v){
            super(v);
            view = v;

            lblSubject = (TextView)view.findViewById(R.id.lblSubject);
            lblTotalMarks = (TextView)view.findViewById(R.id.lblTotalMarks);
            txtGrdde = (EditText)view.findViewById(R.id.txtGrade);
            txtMarksObtained = (EditText)view.findViewById(R.id.txtMarksObtained);
        }
    }
}
