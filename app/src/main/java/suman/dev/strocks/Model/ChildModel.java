package suman.dev.strocks.Model;

import java.util.ArrayList;

/**
 * Created by suman on 5/8/17.
 */

public class ChildModel {
    public String UserToken;
    public String FirstName;
    public String MiddleName;
    public String LastName;
    public boolean Selected;

    //This property used by teacher module to make attendance
    public boolean IsPresent;

    public ArrayList<UserSubjectData> Subjects = new ArrayList<>();


    public String GetFullName()
    {
        return this.FirstName+" "+this.MiddleName+" "+this.LastName;
    }

    public ChildModel(String UserToken, String FirstName, String MiddleName, String LastName)
    {
        this.UserToken = UserToken;
        this.FirstName = FirstName;
        this.MiddleName = MiddleName;
        this.LastName = LastName;
    }

    @Override
    public String toString() {
        return GetFullName();
    }
}
