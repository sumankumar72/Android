package suman.dev.strocks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import suman.dev.strocks.Adapters.EventAdapter;
import suman.dev.strocks.Adapters.TimeTableAdapter;
import suman.dev.strocks.Model.TimeTable;
import suman.dev.strocks.Model.TimeTableDetail;
import suman.dev.strocks.Model.TimeTableDetailModel;
import suman.dev.strocks.Model.Timetable;

/**
 * Created by suman on 23/8/17.
 */

public class TimeTableFragment extends Fragment {
    private View view;
    private TimeTable timeTable;
    private TextView message;
    private Gson gson= new Gson();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TimeTableAdapter adapter;
    public TimeTableFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            String json = getArguments().getString("timeTableDetail");
            timeTable = new TimeTable("","",getArguments().getString("day"),"","");
            timeTable.timetable = gson.fromJson(json, new TypeToken<ArrayList<TimeTableDetailModel>>(){}.getType());

            //message = (TextView)view.findViewById(R.id.message);
            //message.setText(timeTable.Day);


            recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            adapter= new TimeTableAdapter(timeTable.timetable);
            recyclerView.setAdapter(adapter);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        this.view = view;
    }
}
