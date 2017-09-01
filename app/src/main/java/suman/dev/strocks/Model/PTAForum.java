package suman.dev.strocks.Model;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by suman on 30/7/17.
 */

public class PTAForum {
    public int Id;
    public int Author_id;
    public String Title;
    public String CreatedAt;
    public String AutherToken;
    public String AutherRole;
    public String AuthorFirstName;
    public String AuthorMiddleName;
    public String AuthorLastName;
    public String AuthorMobileNo;
    public ArrayList<PTAForumComment> Comments;
    public ArrayList<PTALike> Likes;

    public String GetTimeAgo()
    {
        String result = "";
        try {

            DateFormat readFormat = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss aa");
            Date past= readFormat.parse(this.CreatedAt);
            Date now = new Date();

            long second = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());

            if(second<60)
                result = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime())+ " seconds ago";
            else if(second>60 && second<3600)
                result = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago";
            else if(second>3600 && second<86400)
                result = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago";
            else if(second>86400)
                result = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago";

            return result;
        }
        catch (Exception j){
         return  "";
        }
    }
}