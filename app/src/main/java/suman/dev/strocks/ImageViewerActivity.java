package suman.dev.strocks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;

import suman.dev.strocks.Adapters.EventAdapter;
import suman.dev.strocks.Adapters.ImageViewerAdapter;
import suman.dev.strocks.Model.EventImage;
import suman.dev.strocks.Model.ItemClickListener;

/**
 * Created by suman on 10/8/17.
 */

public class ImageViewerActivity extends AppCompatActivity {

    private ArrayList<EventImage> images;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageViewerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewer);
        try {
            Object obj = getIntent().getExtras().getStringArrayList("images");
            ArrayList<String> img = (ArrayList<String>) obj;

            images = new ArrayList<>();
            EventImage image;
            for (String i : img) {
                image = new EventImage();
                image.ImageUrl = i;
                images.add(image);
            }

            recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new ImageViewerAdapter(images, ImageViewerActivity.this);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            FirebaseCrash.report(e);
            e.getStackTrace();
        }

    }


}
