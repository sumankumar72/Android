package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import suman.dev.strocks.Model.ChildModel;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.PTAForumComment;
import suman.dev.strocks.Model.UserSubjectData;
import suman.dev.strocks.R;

/**
 * Created by suman on 27/8/17.
 */

public class AttandanceAdapter extends RecyclerView.Adapter<AttandanceAdapter.ViewHolder>  {
    private ArrayList<ChildModel> dataSet;
    private static ItemClickListener clickListener;

    //This adapter used to submit result. If adapter used by result set this property false
    private boolean IsAttendance = true;


    public AttandanceAdapter(ArrayList<ChildModel> students){
        dataSet = students;
    }
    public AttandanceAdapter(ArrayList<ChildModel> students, boolean forResult){
        dataSet = students;
        IsAttendance=false;
    }

    @Override
    public AttandanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list, parent, false);
        AttandanceAdapter.ViewHolder viewHolder = new AttandanceAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AttandanceAdapter.ViewHolder holder, int position) {
        ChildModel model = dataSet.get(position);
        holder.txtName.setText(model.GetFullName());
        if (model.IsPresent) {
            holder.rdbPresent.setChecked(true);
            holder.rdbAbsent.setChecked(false);
        } else if (!model.IsPresent) {
            holder.rdbPresent.setChecked(false);
            holder.rdbAbsent.setChecked(true);
        }
        if(model.ResultCompleted)
            holder.txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_result_completed, 0);

        if(!IsAttendance) {
            holder.rdbPresentAbsent.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtName;
        public RadioGroup rdbPresentAbsent;
        public RadioButton rdbPresent,rdbAbsent;
        public LinearLayout layoutLinear;

        public View view;

        public ViewHolder(View v){
            super(v);
            view = v;
            txtName = (TextView)view.findViewById(R.id.txtName);
            rdbPresentAbsent = (RadioGroup)view.findViewById(R.id.rdbPresentAbsent);
            rdbPresent = (RadioButton)view.findViewById(R.id.rdbPresent);
            rdbAbsent = (RadioButton)view.findViewById(R.id.rdbAbsent);

            rdbPresentAbsent.setVisibility(View.VISIBLE);


            rdbPresent.setOnClickListener(this);
            rdbAbsent.setOnClickListener(this);
            txtName.setOnClickListener(this);
            v.setOnClickListener(this);


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
