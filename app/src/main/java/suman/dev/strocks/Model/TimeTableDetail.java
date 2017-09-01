package suman.dev.strocks.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suman on 22/8/17.
 */

public class TimeTableDetail{
    private SimpleDateFormat format;

    public String Subject;
    public String TeacherName;
    public String StartTime;
    public String EndTime;
    public Date StartDateTime;


    public TimeTableDetail(String _Subject, String _TeacherName, String _StartTime, String _EndTime) {
        this.Subject = _Subject;
        this.TeacherName=_TeacherName;
        this.StartTime = _StartTime;
        this.EndTime = _EndTime;
        try {
            format = new SimpleDateFormat("HH:mm");
            this.StartDateTime = format.parse(this.StartTime);
        }catch (Exception e){

        }
    }
}