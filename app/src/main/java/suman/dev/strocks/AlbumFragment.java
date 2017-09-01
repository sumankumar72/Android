package suman.dev.strocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import suman.dev.strocks.Adapters.EventAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.EventImage;
import suman.dev.strocks.Model.Events;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 8/8/17.
 */

public class AlbumFragment extends Fragment {

    private ArrayList<Events> events = new ArrayList<>();
    private Events event;
    private EventImage image;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EventAdapter adapter;

    private VolleyService service= new VolleyService();

    private View view;




    public AlbumFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_album, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view  = view;
        loadEvent();
    }
    private void loadEvent()
    {
        service.MakeGetRequest(Const.GET_ALBUM+ UserProfile.SessionData.Id, getContext() , new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result!=null)
                {
                    try
                    {
                        if(result.getJSONArray("data")!=null)
                        {
                            JSONArray array = result.getJSONArray("data");
                            for (int i=0;i<array.length();i++)
                            {
                                JSONObject json = array.getJSONObject(i);
                                event = new Events();
                                event.Id = json.getInt("id");
                                event.SessionId = json.getInt("session_id");
                                event.AuthorId = json.getInt("author_id");
                                event.Title = json.getString("title");
                                event.Article = json.getString("article");
                                if(json.getJSONArray("album_image")!=null)
                                {
                                    event.Images = new ArrayList<EventImage>();
                                    JSONArray images =json.getJSONArray("album_image");
                                    for (int j=0;j<images.length();j++){
                                        image = new EventImage();
                                        JSONObject img = images.getJSONObject(j);
                                        image.Id =img.getInt("id");
                                        image.Id =img.getInt("album_id");
                                        image.Id =img.getInt("author_id");
                                        image.Name = img.getString("name");
                                        image.ImageUrl = img.getString("image_url");
                                        image.CoverImage = img.getString("cover_image");
                                        event.Images.add(image);
                                    }
                                }
                                events.add(event);
                            }
                            setAdapter();
                        }
                    }
                    catch (Exception e)
                    {
                        FirebaseCrash.report(e);
                        e.getStackTrace();
                        Toast.makeText(getContext(), R.string.serviceError, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                FirebaseCrash.report(error);
                Toast.makeText(getContext(), R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter()
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.eventsRecycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter= new EventAdapter(events, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                ArrayList<String> images = new ArrayList<String>();
                for (EventImage img : events.get(position).Images) {
                    images.add(img.ImageUrl);
                }
                Intent intent= new Intent(getContext(), ImageViewerActivity.class);
                intent.putStringArrayListExtra("images", images);
                startActivity(intent);
            }
        });
    }

}
