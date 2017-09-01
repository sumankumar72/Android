package suman.dev.strocks.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import suman.dev.strocks.Model.StudentResult;
import suman.dev.strocks.R;

/**
 * Created by suman on 29/7/17.
 */

public class ResultAdapter extends ArrayAdapter<StudentResult> {
    private ArrayList<StudentResult> dataset;
    private Context context;
    private Animation animShow, animHide;

    public static class ViewHolder{
        TextView header,totalMarks,marksObtained,percentage;
        View section;
    }

    public ResultAdapter(ArrayList<StudentResult> results, Context context) {
        super(context, R.layout.result_list, results);
        this.dataset = results;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StudentResult result = dataset.get(position);
        final ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.result_list, parent, false);

        if (convertView != null) {
            holder = new ViewHolder();

            holder.header = (TextView) convertView.findViewById(R.id.header);
            holder.totalMarks = (TextView) convertView.findViewById(R.id.totalMarks);
            holder.marksObtained = (TextView) convertView.findViewById(R.id.marksObtained);
            holder.percentage = (TextView) convertView.findViewById(R.id.percentage);
            holder.section = convertView.findViewById(R.id.section);

            holder.header.setText(result.SubjectName);
            holder.totalMarks.setText(result.FullMarks + "");
            holder.marksObtained.setText(result.MarksObtained + "");
            holder.percentage.setText(result.MarksObtained + "%");
            holder.section.setVisibility(View.GONE);

            animShow = AnimationUtils.loadAnimation(context , R.anim.in_animation);
            animHide = AnimationUtils.loadAnimation(context, R.anim.out_animation);

            if (position == 0) {
                //holder.header.startAnimation(animShow);
                holder.section.setVisibility(View.VISIBLE);
                holder.header.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);

            }


            holder.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.section.getVisibility() == View.GONE) {
                        //holder.header.startAnimation(animShow);
                        holder.section.setVisibility(View.VISIBLE);
                        holder.header.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    } else {
                        //holder.header.startAnimation(animHide);
                        holder.section.setVisibility(View.GONE);
                        holder.header.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);
                    }
                }
            });
            convertView.setTag(holder);
            return convertView;
        } else
            return super.getView(position, convertView, parent);

    }
}
