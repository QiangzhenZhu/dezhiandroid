package cn.xiaocool.dezhischool.bean;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class ClassInfo {

    /**
     * t_id : 20
     * teacherid : 604
     * schoolid : 1
     * classid : 2
     * type : 1
     * status : 0
     * classname : 小二班
     */

    private String id;
    private String t_id;
    private String teacherid;
    private String schoolid;
    private String classid;
    private String type;
    private String status;
    private String classname;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  ClassInfo)){
            return false;
        }
        ClassInfo info = (ClassInfo) obj;

        return (this.classname.equals(info.getClassname())&&this.id.equals(info.getId()));

    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
