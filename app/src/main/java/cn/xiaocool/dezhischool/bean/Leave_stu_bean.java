package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by mac on 2017/6/15.
 */

public class Leave_stu_bean {
    /**
     * data : [{"studentid":"599","appellation_id":"1","studentavatar":"weixiaotong.png","studentname":"李小东","id":"2","relation_rank":"1","time":"1453282437","userid":"597","preferred":"1"},{"studentid":"599","appellation_id":"2","studentavatar":"weixiaotong.png","studentname":"李小东","id":"1","relation_rank":"2","time":"1453282437","userid":"597","preferred":"0"}]
     * status : success
     */
    private List<DataEntity> data;
    private String status;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public class DataEntity {
        /**
         * studentid : 599
         * appellation_id : 1
         * studentavatar : weixiaotong.png
         * studentname : 李小东
         * id : 2
         * relation_rank : 1
         * time : 1453282437
         * userid : 597
         * preferred : 1
         */
        private String studentid;
        private String appellation_id;
        private String studentavatar;
        private String studentname;
        private String id;
        private String relation_rank;
        private String time;
        private String userid;
        private String preferred;

        public void setStudentid(String studentid) {
            this.studentid = studentid;
        }

        public void setAppellation_id(String appellation_id) {
            this.appellation_id = appellation_id;
        }

        public void setStudentavatar(String studentavatar) {
            this.studentavatar = studentavatar;
        }

        public void setStudentname(String studentname) {
            this.studentname = studentname;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setRelation_rank(String relation_rank) {
            this.relation_rank = relation_rank;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setPreferred(String preferred) {
            this.preferred = preferred;
        }

        public String getStudentid() {
            return studentid;
        }

        public String getAppellation_id() {
            return appellation_id;
        }

        public String getStudentavatar() {
            return studentavatar;
        }

        public String getStudentname() {
            return studentname;
        }

        public String getId() {
            return id;
        }

        public String getRelation_rank() {
            return relation_rank;
        }

        public String getTime() {
            return time;
        }

        public String getUserid() {
            return userid;
        }

        public String getPreferred() {
            return preferred;
        }
    }
}
