package suman.dev.strocks.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import suman.dev.strocks.Model.SyllabusTopic;
import suman.dev.strocks.R;

/**
 * Created by suman on 29/7/17.
 */

public class SyllabusTopicAdapter extends ArrayAdapter<SyllabusTopic> {
    private ArrayList<SyllabusTopic> topics;
    private Context mContext;

    public static class ViewHolder
    {
        TextView txtSyllabusTopic;
    }

    public SyllabusTopicAdapter(ArrayList<SyllabusTopic> topics, Context context)
    {
        super(context, R.layout.syllabus_topic_list, topics);
        mContext =context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SyllabusTopic topic = topics.get(position) ;
        SyllabusTopicAdapter.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.syllabus_topic_list, parent, false);
        if(convertView!=null)
        {
            holder= new ViewHolder();
            holder.txtSyllabusTopic = (TextView)convertView.findViewById(R.id.syllabusTpoic);
            holder.txtSyllabusTopic.setText(topic.TopicName);
            return  convertView;
        }
        else
            return super.getView(position, convertView, parent);
    }
}
