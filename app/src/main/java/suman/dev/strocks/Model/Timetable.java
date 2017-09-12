package suman.dev.strocks.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by suman on 22/8/17.
 */

public class TimeTable{
    private SimpleDateFormat format;
    public int index;
    public String Day;
    public String StartTime;
    public String EndTime;

    public Date StartDateTime;
    public String Subject;
    public String TeacherName;

    public ArrayList<TimeTableDetail> Details = new ArrayList<>();
    public ArrayList<TimeTableDetailModel> timetable = new ArrayList<>();

    public TimeTable(String Subject, String TeacherName, String Day, String StartTime, String EndTime) {
        this.Day = Day;
        this.Subject = Subject;
        this.TeacherName = TeacherName;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        try {
            format = new SimpleDateFormat("HH:mm");
            this.StartDateTime = format.parse(this.StartTime);
        }catch (Exception e){

        }
        //this.Details = Details;
    }
}
