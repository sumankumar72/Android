package suman.dev.strocks.Adapters;

import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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


    public void onBindViewHolder(final ResultPostAdapter.ViewHolder holder, int position) {
        final UserSubjectData model = dataSet.get(position);
        holder.lblSubject.setText(model.Name);
        holder.txtGrdde.setText(model.Grade);
        holder.txtMarksObtained.setText(model.MarksObtained);
        holder.txtReview.setText(model.Review);

        holder.txtGrdde.setEnabled((model.Grade.trim().length()<=0));
        holder.txtMarksObtained.setEnabled((model.MarksObtained.trim().length()<=0));
        holder.txtReview.setEnabled((model.Review.trim().length()<=0));

        holder.txtReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.Review = holder.txtReview.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        holder.txtGrdde.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.Grade = holder.txtGrdde.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.txtMarksObtained.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.MarksObtained = holder.txtMarksObtained.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lblTotalMarks, lblSubject;
        public EditText txtMarksObtained, txtGrdde, txtReview;
        public View view;

        public ViewHolder(View v){
            super(v);
            view = v;

            lblSubject = (TextView)view.findViewById(R.id.lblSubject);
            lblTotalMarks = (TextView)view.findViewById(R.id.lblTotalMarks);
            txtGrdde = (EditText)view.findViewById(R.id.txtGrade);
            txtMarksObtained = (EditText)view.findViewById(R.id.txtMarksObtained);
            txtReview = (EditText)view.findViewById(R.id.txtReview);
        }
    }
}
