package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import suman.dev.strocks.Model.TimeTable;
import suman.dev.strocks.Model.TimeTableDetail;
import suman.dev.strocks.Model.TimeTableDetailModel;
import suman.dev.strocks.R;

/**
 * Created by suman on 23/8/17.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {
    ArrayList<TimeTableDetailModel> _dataSet=null;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //public TextView txtTime, txtMon, txtTue, txtWed, txtThu, txtFri, txtSat;
        public TextView txtTime, txtSubject, txtTeacherName;

        public View view;

        public ViewHolder(View v)
        {
            super(v);
//            txtTime = (TextView)itemView.findViewById(R.id.lblTime);
//            txtMon= (TextView)itemView.findViewById(R.id.lblMon);
//            txtTue= (TextView)itemView.findViewById(R.id.lblTue);
//            txtWed = (TextView)itemView.findViewById(R.id.lblWed);
//            txtThu = (TextView)itemView.findViewById(R.id.lblThu);
//            txtFri = (TextView)itemView.findViewById(R.id.lblFri);
//            txtSat= (TextView)itemView.findViewById(R.id.lblSat);

            txtTime = (TextView)itemView.findViewById(R.id.time);
            txtSubject = (TextView)itemView.findViewById(R.id.subject);
            txtTeacherName= (TextView)itemView.findViewById(R.id.teacherName);
            view=v;
        }
    }

    public TimeTableAdapter(ArrayList<TimeTableDetailModel> dataSet){
        this._dataSet=dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timetable_tabview_list, parent, false);
        TimeTableAdapter.ViewHolder viewHolder = new TimeTableAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeTableDetailModel timeTable = _dataSet.get(position);
        holder.txtTime.setText(timeTable.StartTime+" to "+timeTable.EndTime);
        //holder.txtSubject.setText(timeTable.Subject);
        //holder.txtTeacherName.setText(timeTable.TeacherName);

    }

    @Override
    public int getItemCount() {
        if(_dataSet==null)
            return 0;
        else
            return _dataSet.size();
    }
}
