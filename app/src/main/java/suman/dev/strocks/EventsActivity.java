package suman.dev.strocks;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import suman.dev.strocks.Adapters.EventAdapter;
import suman.dev.strocks.Adapters.TabFragmentAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.EventImage;
import suman.dev.strocks.Model.Events;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 26/7/17.
 */

public class EventsActivity extends AppCompatActivity implements View.OnClickListener  {
    private ArrayList<Events> events = new ArrayList<>();
    private Events event;
    private EventImage image;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EventAdapter adapter;

    private TextView content_title;
    private String title;

    private VolleyService service= new VolleyService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        UserProfileHeader  profile =  new UserProfileHeader(this,findViewById(R.id.userProfileHeader));
        profile.loadProfile();

        content_title = (TextView)findViewById(R.id.content_toolbar_title);
        content_title.setText("Events");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tent_black, 0, 0, 0);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        final ViewPager pager= (ViewPager)findViewById(R.id.pager);
        TabFragmentAdapter pagerAdapter = new TabFragmentAdapter(getSupportFragmentManager());

        pagerAdapter.addFrag(new AlbumFragment(),"Album");
        pagerAdapter.addFrag(new EventFragment(), "Event");
        pager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(pager);

        TextView tab1= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab1.setText("Album");
        tabLayout.getTabAt(0).setCustomView(tab1);

        TextView tab2= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab2.setText("Event");
        tabLayout.getTabAt(1).setCustomView(tab2);



//        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        //loadEvent();

    }

    @Override
    public void onClick(View v) {

    }
}
