package suman.dev.strocks.Adapters;

import android.app.ActionBar;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.Syllabus;
import suman.dev.strocks.Model.SyllabusTopic;
import suman.dev.strocks.R;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 29/7/17.
 */

public class SyllabusAdapter extends ArrayAdapter<Syllabus> {
    private Context mContext;
    private ArrayList<Syllabus> syllabuses;
    private ArrayList<SyllabusTopic> syllabusTopics;
    private final VolleyService service = new VolleyService();
    private SyllabusTopicAdapter adapter;

    public static class ViewHolder {
        TextView txtSyllabus;
        ListView listView;
        LinearLayout layout;
    }

    public SyllabusAdapter(ArrayList<Syllabus> syllabuses, Context context) {
        super(context, R.layout.syllabus_detail_list, syllabuses);
        mContext = context;
        this.syllabuses = syllabuses;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Syllabus syllabus = syllabuses.get(position);


        final SyllabusAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.syllabus_detail_list, parent, false);
        if (convertView != null) {
            holder = new ViewHolder();
            holder.txtSyllabus = (TextView) convertView.findViewById(R.id.header);
            //holder.listView = (ListView)convertView.findViewById(R.id.listSyllabusTopic);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layoutTopic);
            holder.txtSyllabus.setText(syllabus.SubjectName + "/" + syllabus.SyllabusName);
            convertView.setTag(holder);

            holder.txtSyllabus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    service.MakeGetRequest(String.format(Const.GET_STUDENT_SYLLABUS_TOPIC, syllabus.Id + ""), getContext(), new VolleyJsonObjectCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {

                            if (syllabus.SyllabusTopic != null)
                                setAdapter(syllabus, holder.listView, holder.layout);
                            else {
                                try {
                                    if (result.getJSONArray("data") != null && result.getJSONArray("data").length() > 0) {
                                        JSONArray topics = result.getJSONArray("data").getJSONObject(0).getJSONArray("syllabus_topic");
                                        syllabus.SyllabusTopic = new ArrayList<SyllabusTopic>();
                                        for (int i = 0; i < topics.length(); i++) {
                                            JSONObject json = topics.getJSONObject(i);
                                            syllabus.SyllabusTopic.add(new SyllabusTopic(
                                                    json.getInt("id"),
                                                    json.getString("name")
                                            ));
                                        }
                                        setAdapter(syllabus, holder.listView, holder.layout);
                                    }
                                } catch (Exception e) {
                                    FirebaseCrash.report(e);
                                    e.getStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
                }
            });


            return convertView;
        } else
            return super.getView(position, convertView, parent);
    }

    private void setAdapter(Syllabus syllabus, ListView list, LinearLayout layout) {
        //adapter = new SyllabusTopicAdapter(syllabus.SyllabusTopic, mContext);
        //list.setAdapter(adapter);

        for (SyllabusTopic t : syllabus.SyllabusTopic) {
            if(layout.findViewById(t.TopicId)==null) {
                TextView v = new TextView(mContext);
                v.setId(t.TopicId);
                v.setText(t.TopicName);
                v.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                v.setPadding(10, 10, 10, 10);
                v.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
                layout.addView(v);
            }
        }
    }
}