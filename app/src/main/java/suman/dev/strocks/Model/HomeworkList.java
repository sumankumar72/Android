package suman.dev.strocks.Model;

import java.util.ArrayList;

/**
 * Created by sshan on 9/16/2017.
 */

public class HomeworkList {
    public int Id;
    public String SubjectName;
    public String IssuedDate;
    public String EndDate;
    public String TeacherNote;
    public String FileUrl;
    public String ClassName;
    public ArrayList<Student> Students;

    public void AddStudent(int StudentId, String SubmittedDate, String StudentNote, String FileUrl, String StudentName) {
        if (Students == null)
            Students = new ArrayList<>();

        Students.add(new Student(StudentId, SubmittedDate, StudentNote, FileUrl, StudentName));
    }

    public class Student {
        public int StudentId;
        public String SubmittedDate;
        public String StudentNote;
        public String FileUrl;
        public String StudentName;

        public Student(int StudentId, String SubmittedDate, String StudentNote, String FileUrl, String StudentName) {
            this.StudentId = StudentId;
            this.StudentName = StudentName;
            this.SubmittedDate = SubmittedDate;
            this.StudentNote = StudentNote;
            this.FileUrl = FileUrl;
        }
    }
}
