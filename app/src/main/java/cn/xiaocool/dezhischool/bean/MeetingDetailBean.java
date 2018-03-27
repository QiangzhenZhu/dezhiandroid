package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by 10835 on 2018/2/5.
 */

public class MeetingDetailBean {

    /**
     * id : 10
     * school_id : 14
     * chair_id : 94849
     * title : 会议1
     * description : 考试安排
     * begintime : 0
     * endtime : 0
     * userid : 94849
     * meetingroom : 1
     * create_time : 2018-01-16 16:24"47
     * room_name : 一号会议室
     * chair_name : 常玉荣
     * chair_phone : 15931962905
     * chair_photo : weixiaotong.png
     * publisher_name : 常玉荣
     * publisher_phone : 15931962905
     * publisher_photo : weixiaotong.png
     * userlist : [{"id":"22","conferenceid":"10","userid":"94846","readtime":"0","cometime":"0","leavetime":"0","create_time":"1516091087","username":"钟尚梅"},{"id":"23","conferenceid":"10","userid":"94849","readtime":"1517646195","cometime":"0","leavetime":"0","create_time":"1516091087","username":"常玉荣"},{"id":"24","conferenceid":"10","userid":"94869","readtime":"0","cometime":"0","leavetime":"0","create_time":"1516091087","username":"李怀宇"},{"id":"25","conferenceid":"10","userid":"94873","readtime":"0","cometime":"0","leavetime":"0","create_time":"1516091087","username":"倪双双"}]
     */

    private String id;
    private String school_id;
    private String chair_id;
    private String title;
    private String description;
    private String begintime;
    private String endtime;
    private String userid;
    private String meetingroom;
    private String create_time;
    private String room_name;
    private String chair_name;
    private String chair_phone;
    private String chair_photo;
    private String publisher_name;
    private String publisher_phone;
    private String publisher_photo;
    private List<UserlistBean> userlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getChair_id() {
        return chair_id;
    }

    public void setChair_id(String chair_id) {
        this.chair_id = chair_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMeetingroom() {
        return meetingroom;
    }

    public void setMeetingroom(String meetingroom) {
        this.meetingroom = meetingroom;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getChair_name() {
        return chair_name;
    }

    public void setChair_name(String chair_name) {
        this.chair_name = chair_name;
    }

    public String getChair_phone() {
        return chair_phone;
    }

    public void setChair_phone(String chair_phone) {
        this.chair_phone = chair_phone;
    }

    public String getChair_photo() {
        return chair_photo;
    }

    public void setChair_photo(String chair_photo) {
        this.chair_photo = chair_photo;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getPublisher_phone() {
        return publisher_phone;
    }

    public void setPublisher_phone(String publisher_phone) {
        this.publisher_phone = publisher_phone;
    }

    public String getPublisher_photo() {
        return publisher_photo;
    }

    public void setPublisher_photo(String publisher_photo) {
        this.publisher_photo = publisher_photo;
    }

    public List<UserlistBean> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<UserlistBean> userlist) {
        this.userlist = userlist;
    }

    public static class UserlistBean {
        /**
         * id : 22
         * conferenceid : 10
         * userid : 94846
         * readtime : 0
         * cometime : 0
         * leavetime : 0
         * create_time : 1516091087
         * username : 钟尚梅
         */

        private String id;
        private String conferenceid;
        private String userid;
        private String readtime;
        private String cometime;
        private String leavetime;
        private String create_time;
        private String username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getConferenceid() {
            return conferenceid;
        }

        public void setConferenceid(String conferenceid) {
            this.conferenceid = conferenceid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getReadtime() {
            return readtime;
        }

        public void setReadtime(String readtime) {
            this.readtime = readtime;
        }

        public String getCometime() {
            return cometime;
        }

        public void setCometime(String cometime) {
            this.cometime = cometime;
        }

        public String getLeavetime() {
            return leavetime;
        }

        public void setLeavetime(String leavetime) {
            this.leavetime = leavetime;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
