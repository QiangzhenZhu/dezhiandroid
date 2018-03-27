package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class ClassParent {

    /**
     * classid : 1
     * classname : 小一班啊
     * student_list : [{"id":"654","name":"呵呵哈哈","phone":"22222222","photo":"weixiaotong.png","parent_list":[{"id":"674","name":"嗯嗯嗯嗯","phone":"15142389265","photo":"weixiaotong.png","appellation":"奶奶"}]}]
     */

    private String classid;
    private String classname;
    /**
     * id : 654
     * name : 呵呵哈哈
     * phone : 22222222
     * photo : weixiaotong.png
     * parent_list : [{"id":"674","name":"嗯嗯嗯嗯","phone":"15142389265","photo":"weixiaotong.png","appellation":"奶奶"}]
     */

    private List<StudentListBean> student_list;

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

    public List<StudentListBean> getStudent_list() {
        return student_list;
    }

    public void setStudent_list(List<StudentListBean> student_list) {
        this.student_list = student_list;
    }

    public static class StudentListBean {
        private String id;
        private String name;
        private String phone;
        private String photo;
        /**
         * id : 674
         * name : 嗯嗯嗯嗯
         * phone : 15142389265
         * photo : weixiaotong.png
         * appellation : 奶奶
         */

        private List<ParentListBean> parent_list;

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

        public List<ParentListBean> getParent_list() {
            return parent_list;
        }

        public void setParent_list(List<ParentListBean> parent_list) {
            this.parent_list = parent_list;
        }

        public static class ParentListBean {
            private String id;
            private String name;
            private String phone;
            private String photo;
            private String appellation;

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

            public String getAppellation() {
                return appellation;
            }

            public void setAppellation(String appellation) {
                this.appellation = appellation;
            }
        }
    }
}
