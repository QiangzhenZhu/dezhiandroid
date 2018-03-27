package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class ClassTeacher {

    /**
     * classid : 1
     * classname : 小一班啊
     * teacherlist : [{"id":"600","name":"朱2啊","sex":"1","phone":"18653503663","photo":"weixiaotong.png"},{"id":"609","name":"老王","sex":"1","phone":"18865511108","photo":"weixiaotong.png"},{"id":"605","name":"潘宁","sex":"1","phone":"15589521956","photo":"weixiaotong.png"},{"id":"684","name":"测试二","sex":"0","phone":"11111","photo":"weixiaotong.png"},{"id":"683","name":"测试","sex":"0","phone":"1635123","photo":"weixiaotong.png"}]
     */

    private String classid;
    private String classname;
    /**
     * id : 600
     * name : 朱2啊
     * sex : 1
     * phone : 18653503663
     * photo : weixiaotong.png
     */

    private List<TeacherlistBean> teacherlist;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<TeacherlistBean> getTeacherlist() {
        return teacherlist;
    }

    public void setTeacherlist(List<TeacherlistBean> teacherlist) {
        this.teacherlist = teacherlist;
    }

    public static class TeacherlistBean {
        private String id;
        private String name;
        private String sex;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
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
