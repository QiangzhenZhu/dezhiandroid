package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 2017/6/14.
 */
public class NewClassAttendance implements Serializable {

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeave() {
        return isleave;
    }

    public void setLeave(String leave) {
        this.isleave = leave;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<attendance_info> getAttendance_infolist() {
        return attendance_info;
    }

    public void setAttendance_infolist(List<attendance_info> attendance_infolist) {
        this.attendance_info = attendance_infolist;
    }

    public List<attendance_info> getInfo() {
        return info;
    }

    public void setInfo(List<attendance_info> info) {
        this.info = info;
    }

    private String studentid;
    private String name;
    private String photo;
    private String isleave;
    private List<attendance_info> info;
    private List<attendance_info> attendance_info;

    public static class attendance_info implements Serializable {
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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getNew_photo() {
            return new_photo;
        }

        public void setNew_photo(String new_photo) {
            this.new_photo = new_photo;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        private String new_photo;
        private String mid;
        private String fid;
        private String location;
        private String time;
        private String type;

    }

}
