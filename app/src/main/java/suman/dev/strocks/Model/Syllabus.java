package suman.dev.strocks.Model;

import java.util.ArrayList;

/**
 * Created by suman on 22/7/17.
 */

public class Syllabus {
    public int Id;
    public int SubjectId;
    public String SubjectName;
    public String SyllabusName;
    public ArrayList<SyllabusTopic> SyllabusTopic;

    public Syllabus(int Id, int SubjectId, String SubjectName, String SyllabusName, ArrayList<SyllabusTopic> Topic)
    {
        this.Id = Id;
        this.SubjectId = SubjectId;
        this.SubjectName = SubjectName;
        this.SyllabusName=SyllabusName;
        this.SyllabusTopic = Topic;
    }

}
