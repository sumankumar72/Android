package suman.dev.strocks.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suman on 6/8/17.
 */

public class TeacherProfile {
    public String UserToken;
    public String SchoolCode;
    public String Email;
    public String Role;
    public String ParentId;
    public String FirstName;
    public String MiddleName;
    public String LastName;
    public String DOB;
    public String Age;
    public String Address;
    public String Mobile;
    public String AlternateMobile;
    public String City;
    public String State;
    public String Pin;
    public String Image;
    public String LoginAs;
    public UserSessionData SessionData= new UserSessionData();
    public List<UserSubjectData> SubjectData= new ArrayList<>();
    public UserClassData ClassData= new UserClassData();
    public List<UserHomework> HomeworkData= new ArrayList<>();
    public ArrayList<ChildModel> childs = new ArrayList<>();



    public  String getFullName()
    {
        return FirstName+" "+MiddleName+" "+LastName;
    }

}
