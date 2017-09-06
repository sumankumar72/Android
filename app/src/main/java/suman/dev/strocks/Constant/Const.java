package suman.dev.strocks.Constant;

/**
 * Created by Node1 on 7/20/2017.
 */

public class Const {
    public static final String BASE_URL="http://52.39.175.55";
    public static final String TOKEN= "/school/public/oauth/token";
    public static final String GET_LOGIN_DATA="/school/public/school/api/user/log";

    public static final String GET_HOMEWORK ="/school/public/school/api/homework/0/edit?subject_id=%s";
    public static final String COMPLETE_HOMEWORK = "/school/public/school/api/studenthomework/%s/update";
    public static final String CREATE_HOMEWORK = "/school/public/school/api/homework/create";


    public static final String GET_HOLIDAY_LIST ="/school/public/school/api/holiday/0/edit?s=";
    public static final String GET_FEE_DETAILS="/school/public/school/api/studentfees/%s/edit?m=%s&session_id=%s";
    public static final String GET_USER_PROFILE="/school/public/school/api/user/%s/edit";
    public static final String GET_STUDENT_ATTENDANCE="/school/public/school/api/studentattendance/%s/graph?m=%s&y=%s&student_id=%sn&session_id=%s&class_id=%s";
    public static final String CREATE_STUDENT_ATTENDANCE="/school/public/school/api/studentattendance/create";
    public static final String GET_STUDENT_RESULT="/school/public/school/api/studentresult/0/edit?student_id=%s&semester_id=%s";
    public static final String GET_STUDENT_SYLLABUS="/school/public/school/api/subject/%s/edit";
    public static final String GET_STUDENT_SYLLABUS_TOPIC="/school/public/school/api/syllabus/%s/edit";
    public static final String GET_PTAFORUM="/school/public/school/api/ptaforum";
    public static final String POST_PTAFORUM_LIKE="/school/public/school/api/ptaforum/%s";
    public static final String POST_PTAFORUM_COMMENT="/school/public/school/api/ptaforum/%s/comment";
    public static final String POST_PTAFORUM_CREATE="/school/public/school/api/ptaforum/create";

    public static final String GET_ALBUM="/school/public/school/api/album?session_id=";
    public static final String GET_EVENTS = "/school/public/school/api/event?session_id=";
    public static final String GET_SEMESTER="/school/public/school/api/semester";
    public static final String GET_PAYMENT_DETAIL="/school/public/school/api/studentpayment/0/edit?student_id=%s&month=%s";
    public static final String GET_TIMETABLE="/school/public/school/api/timetable/0/edit?session_id=%s&class_id=%s";


    public static final String ALBUM_IMAGE_BASE=BASE_URL+"/school/public/uploaded_images/album-images/";
    public static final String EVENT_IMAGE_BASE=BASE_URL+"/school/public/uploaded_images/event-images/";
    public static final String HOMEWORK_IMAGE_BASE=BASE_URL+"/school/public/uploaded_images/student-homeWork/";
    public static final String PROFILE_IMAGE_BASE=BASE_URL+"/school/public/uploaded_images/users/";
    public static final String TEACHER_SUBJECT_DATA="/school/public/school/api/teachersubject/0/edit?session_id=%s&class_id=%s&teacher_id=%s";
    public static final String GET_STUDENT_BY_CLASS="/school/public/school/api/studentsession/0/edit?session_id=%s&class_id=%s&session2=0";
    public static final String CREATE_RESULT="/school/public/school/api/studentresult/create";



    public static final String CLIENT_ID="2";
    public static final String CLIENT_SECRET= "Ez9PTmPQkcTLUAUN5gfqEIEyNaag6bWuZbpEqy1m";


    //****************************Activity Results Code***********************************//
    public static final int LOGIN_OPTION_ACTIVITY=1001;
    public static final int LOGIN_ACTIVITY=1002;

}