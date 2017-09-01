package suman.dev.strocks.Model;

/**
 * Created by suman on 22/7/17.
 */

public class HomeworkDetails {
    public int Id;
    public int SubjectTopicId;
    public int SubjectId;
    public int TeacherId;
    public int ClassId;
    public String IssueDate;
    public String EndDate;
    public String TeacherNote;
    public TeacherProfile TeacherProfile;
    public String FileUrl;

    public String getSubjectName()
    {
        String SubjectName="";
        for(UserSubjectData subjectData:UserProfile.SubjectData)
        {
            if(subjectData.Id==this.SubjectId)
            {
                SubjectName = subjectData.Name;
                break;
            }
        }
        return SubjectName;
    }

}
