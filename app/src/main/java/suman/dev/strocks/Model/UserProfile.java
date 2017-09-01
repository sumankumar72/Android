package suman.dev.strocks.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Node1 on 7/20/2017.
 */

public class UserProfile {
    public static String UserToken;
    public static String SchoolCode;
    public static String Email;
    public static String Role;
    public static String ParentId;
    public static String FirstName;
    public static String MiddleName;
    public static String LastName;
    public static String DOB;
    public static String Age;
    public static String Address;
    public static String Mobile;
    public static String AlternateMobile;
    public static String City;
    public static String State;
    public static String Pin;
    public static String Image;
    public static String LoginAs;
    public static UserSessionData SessionData= new UserSessionData();
    public static List<UserSubjectData> SubjectData= new ArrayList<>();
    public static UserClassData ClassData= new UserClassData();
    public static List<UserSubjectData> TeacherSubjects= new ArrayList<>();
    public static List<UserClassData> Classes= new ArrayList<>();
    public static List<UserHomework> HomeworkData= new ArrayList<>();
    public static ArrayList<ChildModel> childs = new ArrayList<>();



    public static String getFullName()
    {
        return FirstName+" "+MiddleName+" "+LastName;
    }

    public static String CurrentUserToken;

    public static void setToken(String token)
    {
        UserToken = token;
    }

    public static boolean IsCompleted= false;
    public static boolean Impersonated= false;

}







