package suman.dev.strocks;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import suman.dev.strocks.Adapters.CommentAdapter;
import suman.dev.strocks.Adapters.PTAForumAdapter;
import suman.dev.strocks.Adapters.SyllabusTopicAdapter;
import suman.dev.strocks.Constant.Const;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.PTAForum;
import suman.dev.strocks.Model.PTAForumComment;
import suman.dev.strocks.Model.PTALike;
import suman.dev.strocks.Model.UserProfile;
import suman.dev.strocks.WebService.VolleyJsonObjectCallback;
import suman.dev.strocks.WebService.VolleyService;

/**
 * Created by suman on 29/7/17.
 */

public class PTAForumActivity extends AppCompatActivity{

    private ArrayList<PTAForum> forums;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PTAForumAdapter adapter;
    private VolleyService service;

    private RecyclerView commentRecyclerView;
    private RecyclerView.LayoutManager commentLayoutManager;
    private CommentAdapter commentAdapter;
    private Menu _menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptaforum);
        UserProfileHeader profile = new UserProfileHeader(this, findViewById(R.id.userProfileHeader));
        profile.loadProfile();

        TextView content_title = (TextView)findViewById(R.id.content_toolbar_title);
        content_title.setText("PTA/Forum");
        content_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chat_black, 0, 0, 0);
        loadPTAForum();
    }

    private void loadPTAForum()
    {
        forums = new ArrayList<>();

        service= new VolleyService();

        service.MakeGetRequest(Const.GET_PTAFORUM, this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result!=null)
                {
                    try
                    {
                        PTAForum forum;
                        PTAForumComment PTAComment;
                        JSONArray array = result.getJSONArray("data");
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject json = array.getJSONObject(i);
                            forum = new PTAForum();
                            forum.Id = json.getInt("id");
                            forum.Author_id = json.getInt("author_id");
                            forum.Title  = json.getString("comments");
                            forum.CreatedAt = json.getString("created_at");

                            forum.Comments=new ArrayList<PTAForumComment>();
                            JSONArray comments = json.getJSONArray("pta_forum_comment");

                            for (int j=0;j<comments.length();j++)
                            {
                                JSONObject comment = comments.getJSONObject(j);
                                PTAComment = new PTAForumComment();
                                PTAComment.Id = comment.getInt("id");
                                PTAComment.AuthorId = comment.getInt("author_id");
                                PTAComment.Comment = comment.getString("comments");
                                PTAComment.CreatedAt = comment.getString("created_at");
                                PTAComment.pta_forum_id = comment.getInt("pta_forum_id");
                                forum.Comments.add(PTAComment);
                            }
                            JSONArray likes = json.getJSONArray("pta_forum_likes");
                            forum.Likes = new ArrayList<PTALike>();
                            for (int k=0;k<likes.length();k++)
                            {
                                JSONObject like = likes.getJSONObject(k);
                                forum.Likes.add(new PTALike(like.getInt("id"),like.getInt("author_id"),like.getInt("pta_forum_id")));
                            }
                            forums.add(forum);
                        }
                        setAdapter();
                    }
                    catch (Exception e)
                    {
                        e.getStackTrace();
                        FirebaseCrash.report(e);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(PTAForumActivity.this,R.string.serviceError, Toast.LENGTH_SHORT).show();
                FirebaseCrash.report(error);
            }
        });
        setAdapter();
    }
    private void setAdapter()
    {
        recyclerView = (RecyclerView) findViewById(R.id.ptaforumRecycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PTAForumAdapter(forums);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (view.getId())
                {
                    case R.id.txtViewLikes:
                        doLike(forums.get(position));
                        break;
                    case R.id.txtViewCommentCount:
                        doComment(forums.get(position));
                        break;
                }
            }
        });
    }

    //riteshkumarsingh9911@gmail.com

    private void createPTA(){
        try{
            final Dialog dialog = new Dialog(PTAForumActivity.this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.create_pta_dialog);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setAttributes(lp);

            Button btnSave = (Button)dialog.findViewById(R.id.btnSave);
            final EditText txtPta =(EditText)dialog.findViewById(R.id.txtPta);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("session_id", UserProfile.SessionData.Id+"");
                    params.put("comments", txtPta.getText().toString() );

                    service.MakePostRequest(Const.POST_PTAFORUM_CREATE, params, PTAForumActivity.this, new VolleyJsonObjectCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try{
                                if(result.getString("err_code").equals("0")) {
                                    Toast.makeText(PTAForumActivity.this, "PTA Created Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(PTAForumActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(PTAForumActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(PTAForumActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            });

            dialog.show();
        }
        catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }

    private void doComment(final PTAForum ptaForum)
    {
        try {
            Button send;
            final EditText commenttext;

            final Dialog dialog = new Dialog(PTAForumActivity.this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.activity_pta_comment);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setAttributes(lp);

            commentRecyclerView = (RecyclerView) dialog.findViewById(R.id.comment_recycler_view);
            commentRecyclerView.setHasFixedSize(true);
            commentLayoutManager = new LinearLayoutManager(getApplicationContext());
            commentRecyclerView.setLayoutManager(commentLayoutManager);

            commentAdapter = new CommentAdapter(ptaForum.Comments);
            commentRecyclerView.setAdapter(commentAdapter);
            commentAdapter.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {

                }
            });

            send = (Button)dialog.findViewById(R.id.send);
            commenttext =(EditText)dialog.findViewById(R.id.commenttext);
            if(send!=null)
            {
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (commenttext.getText().toString().trim().length() <= 0) {
                                Toast.makeText(PTAForumActivity.this, "Please enter comment text!!!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            service.DoComment(String.format(Const.POST_PTAFORUM_COMMENT, ptaForum.Id), commenttext.getText().toString().trim(), ptaForum.Id + "", PTAForumActivity.this, new VolleyJsonObjectCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    PTAForumComment comment = new PTAForumComment();
                                    comment.Comment = commenttext.getText().toString();
                                    comment.pta_forum_id = ptaForum.Id;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
                                    String currentDateandTime = sdf.format(new Date());
                                    comment.CreatedAt = currentDateandTime;
                                    ptaForum.Comments.add(comment);
                                    commentAdapter.notifyDataSetChanged();
                                    adapter.notifyDataSetChanged();
                                    commenttext.setText("");
                                    //dialog.dismiss();
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Toast.makeText(PTAForumActivity.this, R.string.serviceError, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (Exception e)
                        {
                            FirebaseCrash.report(e);
                        }
                    }
                });
            }


            dialog.show();
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
            e.getStackTrace();
        }
    }


    private void doLike(final PTAForum ptaForum)
    {
        service.DoLikes(String.format(Const.POST_PTAFORUM_LIKE, ptaForum.Id), this, new VolleyJsonObjectCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                if(result!=null)
                {
                    try{
                        //Need to check user liked this comment or not.
                        JSONObject json = result.getJSONObject("data");
                        ptaForum.Likes.add(new PTALike(ptaForum.Id,ptaForum.Author_id,ptaForum.Id));
                        adapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(PTAForumActivity.this,R.string.serviceError, Toast.LENGTH_SHORT).show();
                        e.getStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(PTAForumActivity.this,R.string.serviceError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.switch_user_menu, menu);
        menu.findItem(R.id.nav_switch_add).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.nav_switch_add){
            //Toast.makeText(this, "Add new PTA", Toast.LENGTH_SHORT).show();
            createPTA();
        }
        return super.onOptionsItemSelected(item);
    }
}
