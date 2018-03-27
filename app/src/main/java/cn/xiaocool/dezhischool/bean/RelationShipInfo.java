package cn.xiaocool.dezhischool.bean;

/**
 * Created by 10835 on 2018/3/16.
 */

public class RelationShipInfo  {


    /**
     * appellation : 妈妈
     * userid : 93706
     * type : 1
     * parent_info : {"name":"王艳辉家长","phone":"15031910774","photo":"weixiaotong.png"}
     */

    private String appellation;
    private String userid;
    private String type;
    private ParentInfoBean parent_info;

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ParentInfoBean getParent_info() {
        return parent_info;
    }

    public void setParent_info(ParentInfoBean parent_info) {
        this.parent_info = parent_info;
    }

    public static class ParentInfoBean {
        /**
         * name : 王艳辉家长
         * phone : 15031910774
         * photo : weixiaotong.png
         */

        private String name;
        private String phone;
        private String photo;

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
