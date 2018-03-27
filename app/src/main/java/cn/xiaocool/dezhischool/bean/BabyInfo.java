package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class BabyInfo {

    /**
     * id : 33
     * studentid : 654
     * userid : 674
     * appellation : 奶奶
     * relation_rank : 0
     * preferred : 0
     * type : 0
     * time : 0
     * school_name :
     * studentname : 未填写
     * studentavatar :
     * sex :
     * classlist : [{"classid":"1","schoolid":"1","classname":"小一班啊"}]
     */

    private String id;
    private String studentid;
    private String userid;
    private String appellation;
    private String relation_rank;
    private String preferred;
    private String type;
    private String time;
    private String school_name;
    private String studentname;
    private String studentavatar;
    private String sex;
    /**
     * classid : 1
     * schoolid : 1
     * classname : 小一班啊
     */

    private List<ClasslistBean> classlist;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getRelation_rank() {
        return relation_rank;
    }

    public void setRelation_rank(String relation_rank) {
        this.relation_rank = relation_rank;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getStudentavatar() {
        return studentavatar;
    }

    public void setStudentavatar(String studentavatar) {
        this.studentavatar = studentavatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<ClasslistBean> getClasslist() {
        return classlist;
    }

    public void setClasslist(List<ClasslistBean> classlist) {
        this.classlist = classlist;
    }

    public static class ClasslistBean {
        private String classid;
        private String schoolid;
        private String classname;

        public String getClassid() {
            return classid;
        }

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public String getSchoolid() {
            return schoolid;
        }

        public void setSchoolid(String schoolid) {
            this.schoolid = schoolid;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }
    }
}
