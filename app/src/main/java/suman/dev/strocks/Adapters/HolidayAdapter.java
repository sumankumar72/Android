package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import suman.dev.strocks.Model.Holiday;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.R;

/**
 * Created by suman on 22/7/17.
 */

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.ViewHolder>{
    private static ItemClickListener clickListener;
    private List<Holiday> _holidays=null;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView holiday_list_date,holiday_list_name;
        public View view;

        public ViewHolder(View v)
        {
            super(v);
            holiday_list_name = (TextView)itemView.findViewById(R.id.holiday_list_name);
            holiday_list_date = (TextView)itemView.findViewById(R.id.holiday_list_date);
            view=v;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public HolidayAdapter(List<Holiday> holidays)
    {
        _holidays=holidays;
    }

    @Override
    public HolidayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holiday_list, parent, false);
        HolidayAdapter.ViewHolder viewHolder = new HolidayAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HolidayAdapter.ViewHolder holder, int position) {
        Holiday holiday = _holidays.get(position);
        holder.holiday_list_date.setText("From: "+holiday.Start_Date+" To: "+holiday.End_Date);
        holder.holiday_list_name.setText(holiday.Name);
    }

    @Override
    public int getItemCount() {
        if(_holidays==null)
            return 0;
        else
            return _holidays.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
