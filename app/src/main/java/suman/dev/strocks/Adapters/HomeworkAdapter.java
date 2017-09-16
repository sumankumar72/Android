package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import suman.dev.strocks.Model.HomeworkClickListener;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserClassData;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.Model.UserSubjectData;
import suman.dev.strocks.R;


/**
 * Created by Node1 on 7/21/2017.
 */

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder>{
    public boolean isUsedforHomework= true;
    private static HomeworkClickListener clickListener;

    private static List<UserSubjectData> subjects;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView homework_list_submissiondate,homework_list_subjectName;
        public Button homework_list_btnView,homework_list_btnCreate;
        public View view;

        public ViewHolder(View v)
        {
            super(v);
            view  = v;
            homework_list_submissiondate = (TextView) itemView.findViewById(R.id.homework_list_submissiondate);
            homework_list_subjectName = (TextView) itemView.findViewById(R.id.homework_list_subjectName);
            homework_list_btnView = (Button) itemView.findViewById(R.id.homework_list_btnView);
            homework_list_btnCreate = (Button) itemView.findViewById(R.id.homework_list_btnCreate);
            homework_list_btnView.setOnClickListener(this);
            homework_list_btnCreate.setOnClickListener(this);

            if(UserProfile.Role.toLowerCase().equals("teacher")) {
                //homework_list_btnView.setText("Create");
                homework_list_btnCreate.setVisibility(View.VISIBLE);
                homework_list_btnView.setWidth(160);
            }

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.onClick(v, UserProfile.SubjectData.get(getAdapterPosition()));
        }
    }

    public HomeworkAdapter()
    {
        //subjects = new ArrayList<>();
        //for (int i=0;i<UserProfile.SubjectData.size();i++){
          //  if(UserProfile.SubjectData.get(i).HasHomework)
                //subjects.add(UserProfile.SubjectData.get(i));
        //}
    }
    public HomeworkAdapter(boolean SyllabusAdapter)
    {
        subjects = UserProfile.SubjectData;
    }

    @Override
    public HomeworkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homework_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeworkAdapter.ViewHolder holder, int position) {
        //UserSubjectData subjectData = subjects.get(position);
        UserSubjectData subjectData = UserProfile.SubjectData.get(position);
        if(isUsedforHomework) {
            //if (subjectData.HasHomework) {
                holder.homework_list_subjectName.setText(subjectData.Name);
                if (!isUsedforHomework)
                    holder.homework_list_submissiondate.setText("Click below to...");
                //else
                   // holder.homework_list_submissiondate.setText("Submission Date: 22-Jul-2017");
            //}
        }
        else
        {
            holder.homework_list_subjectName.setText(subjectData.Name);
            if (!isUsedforHomework)
                holder.homework_list_submissiondate.setText("Click below to...");
            else
                holder.homework_list_submissiondate.setText("Submission Date: 22-Jul-2017");
        }
    }

    @Override
    public int getItemCount() {
        //return subjects.size();
        return UserProfile.SubjectData.size();
    }

    public void setClickListener(HomeworkClickListener itemClickListener) {
         this.clickListener = itemClickListener;
    }
}