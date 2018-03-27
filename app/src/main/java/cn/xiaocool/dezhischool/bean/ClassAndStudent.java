package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class ClassAndStudent {

    /**
     * classname : 小一班啊
     * lists : [{"id":"654","name":"呵呵哈哈","sex":"1","photo":"weixiaotong.png"},{"id":"685","name":"大眼","sex":"1","photo":"weixiaotong.png"}]
     */

    private String classname;
    /**
     * id : 654
     * name : 呵呵哈哈
     * sex : 1
     * photo : weixiaotong.png
     */

    private List<ListsBean> lists;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        private String id;
        private String name;
        private String sex;
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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
