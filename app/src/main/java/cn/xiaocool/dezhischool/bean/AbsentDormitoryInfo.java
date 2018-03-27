package cn.xiaocool.dezhischool.bean;

/**
 * Created by 10835 on 2018/3/6.
 */

public class AbsentDormitoryInfo {


    /**
     * id : 44
     * studentid : 90355
     * schoolid : 14
     * name : 倪娟
     * photo : weixiaotong.png
     * classname : 高三2班
     * dorm_name : 女生宿舍
     * room_name : 104
     */

    private String id;
    private String studentid;
    private String schoolid;
    private String name;
    private String photo;
    private String classname;
    private String dorm_name;
    private String room_name;
    private boolean isReply;

    public boolean getIsReply() {
        return isReply;
    }

    public void setIsReply(boolean isReply) {
        this.isReply = isReply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDorm_name() {
        return dorm_name;
    }

    public void setDorm_name(String dorm_name) {
        this.dorm_name = dorm_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }
}
