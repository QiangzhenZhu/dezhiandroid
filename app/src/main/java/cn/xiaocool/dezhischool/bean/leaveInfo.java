package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 10835 on 2018/2/27.
 */

public class leaveInfo  implements Serializable{

    /**
     * id : 223
     * studentid : 93712
     * classid : 206
     * parentid : 93706
     * teacherid : 94873
     * create_time : 1519701367
     * begintime : 1519701303
     * endtime : 1519787707
     * reason : 感冒
     * leavetype : 病假
     * status : 10
     * feedback : 好的
     * deal_time : 1519710921
     * classname : 高一3班
     * teachername : 倪双双
     * teacheravatar : weixiaotong.png
     * teacherphone : 18233013569
     * parentname : 王艳辉家长
     * parentavatar : weixiaotong.png
     * parentphone : 15031910774
     * studentname : 王保凯
     * studentavatar : weixiaotong.png
     * pic : [{"picture_url":""}]
     * reportback : [{"id":"3","userid":"93706","leaveid":"223","reason":"Test destroy leave 022714071","applytype":"0","teacherid":"94873","create_time":"1519711682","backtime":"1519711652","status":"1","feedback":"同意","deal_time":"1519712803"}]
     */

    private String id;
    private String studentid;
    private String classid;
    private String parentid;
    private String teacherid;
    private String create_time;
    private String begintime;
    private String endtime;
    private String reason;
    private String leavetype;
    private String status;
    private String feedback;
    private String deal_time;
    private String classname;
    private String teachername;
    private String teacheravatar;
    private String teacherphone;
    private String parentname;
    private String parentavatar;
    private String parentphone;
    private String studentname;
    private String studentavatar;
    private List<PicBean> pic;
    private List<ReportbackBean> reportback;

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

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeacheravatar() {
        return teacheravatar;
    }

    public void setTeacheravatar(String teacheravatar) {
        this.teacheravatar = teacheravatar;
    }

    public String getTeacherphone() {
        return teacherphone;
    }

    public void setTeacherphone(String teacherphone) {
        this.teacherphone = teacherphone;
    }

    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public String getParentavatar() {
        return parentavatar;
    }

    public void setParentavatar(String parentavatar) {
        this.parentavatar = parentavatar;
    }

    public String getParentphone() {
        return parentphone;
    }

    public void setParentphone(String parentphone) {
        this.parentphone = parentphone;
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

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public List<ReportbackBean> getReportback() {
        return reportback;
    }

    public void setReportback(List<ReportbackBean> reportback) {
        this.reportback = reportback;
    }

    public static class PicBean implements Serializable {
        /**
         * picture_url :
         */

        private String picture_url;

        public String getPicture_url() {
            return picture_url;
        }

        public void setPicture_url(String picture_url) {
            this.picture_url = picture_url;
        }
    }

    public static class ReportbackBean implements Serializable{
        /**
         * id : 3
         * userid : 93706
         * leaveid : 223
         * reason : Test destroy leave 022714071
         * applytype : 0
         * teacherid : 94873
         * create_time : 1519711682
         * backtime : 1519711652
         * status : 1
         * feedback : 同意
         * deal_time : 1519712803
         */

        private String id;
        private String userid;
        private String leaveid;
        private String reason;
        private String applytype;
        private String teacherid;
        private String create_time;
        private String backtime;
        private String status;
        private String feedback;
        private String deal_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getLeaveid() {
            return leaveid;
        }

        public void setLeaveid(String leaveid) {
            this.leaveid = leaveid;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getApplytype() {
            return applytype;
        }

        public void setApplytype(String applytype) {
            this.applytype = applytype;
        }

        public String getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(String teacherid) {
            this.teacherid = teacherid;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getBacktime() {
            return backtime;
        }

        public void setBacktime(String backtime) {
            this.backtime = backtime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getDeal_time() {
            return deal_time;
        }

        public void setDeal_time(String deal_time) {
            this.deal_time = deal_time;
        }
    }
}
