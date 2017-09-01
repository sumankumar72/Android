package suman.dev.strocks.Model;

/**
 * Created by suman on 28/7/17.
 */

public class StudentResult {
    public int Id;
    public int SubjectId;
    public int FullMarks;
    public int MarksObtained;
    public String Grade;
    public String Review;
    public String SubjectName;

    public StudentResult(int Id, int SubjectId, int FullMarks, int MarksObtained,
                         String Grade, String Review, String SubjectName)
    {
        this.Id = Id;
        this.SubjectId = SubjectId;
        this.FullMarks = FullMarks;
        this.MarksObtained = MarksObtained;
        this.Grade = Grade;
        this.Review = Review;
        this.SubjectName = SubjectName;
    }
}
