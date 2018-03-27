package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class TeacherInfo {

    /**
     * id : 4
     * name : 财务室
     * teacherinfo : [{"id":"675","name":"柳林","phone":"","photo":"weixiaotong.png"},{"id":"600","name":"朱2啊","phone":"18653503663","photo":"weixiaotong.png"},{"id":"684","name":"测试二","phone":"11111","photo":"weixiaotong.png"}]
     */

    private String id;
    private String name;
    /**
     * id : 675
     * name : 柳林
     * phone :
     * photo : weixiaotong.png
     */

    private List<TeacherinfoBean> teacherinfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeacherinfoBean> getTeacherinfo() {
        return teacherinfo;
    }

    public void setTeacherinfo(List<TeacherinfoBean> teacherinfo) {
        this.teacherinfo = teacherinfo;
    }

    public static class TeacherinfoBean {
        private String id;
        private String name;
        private String phone;
        private String photo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
