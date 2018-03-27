package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ChooseReciveBean implements Serializable {

    /**
     * classid : 1
     * classname : 小一班
     * studentlist : [{"id":"647","name":"随便1","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"646","name":"随变2","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"648","name":"随便3","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"643","name":"随便4","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"599","name":"随便5","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"645","name":"随便6","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"644","name":"随便7","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"649","name":"随便8","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"657","name":"随便9","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"658","name":"10","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"665","name":"朱晟超","sex":"1","phone":"","photo":"weixiaotong.png"},{"id":"666","name":"秦安雅","sex":"0","phone":"","photo":"20160825145626666.png"},{"id":"667","name":"林程跃","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"668","name":"林欣辰","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"669","name":"曾熙","sex":"0","phone":"","photo":"weixiaotong.png"},{"id":"670","name":"黄显江","sex":"1","phone":"","photo":"weixiaotong.png"},{"id":"671","name":"薛直得","sex":"1","phone":"","photo":"weixiaotong.png"},{"id":"682","name":"蒋庆学生","sex":"0","phone":"","photo":"20160817151532682.png"},{"id":"661","name":"啊强小衰","sex":"1","phone":"15653579769","photo":"20160824170255661.png"}]
     */

    private String classid;
    private String classname;
    /**
     * id : 647
     * name : 随便1
     * sex : 0
     * phone :
     * photo : weixiaotong.png
     */

    private List<StudentlistBean> studentlist;

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

    public List<StudentlistBean> getStudentlist() {
        return studentlist;
    }

    public void setStudentlist(List<StudentlistBean> studentlist) {
        this.studentlist = studentlist;
    }

    public static class StudentlistBean implements Serializable{
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
