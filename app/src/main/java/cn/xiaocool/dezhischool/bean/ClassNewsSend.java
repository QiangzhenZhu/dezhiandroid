package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class ClassNewsSend {

    /**
     * id : 4
     * userid : 604
     * content : 班级消息8-31
     * create_time : 1472626986
     * receiverlist : [{"id":"98","homework_id":"4","receiverid":"655","read_time":null,"receiver_info":[{"name":"嗯嗯","photo":"weixiaotong.png","phone":"232323"}]}]
     * teacher_info : {"name":"王志恒","photo":"weixiaotong.png"}
     * pic : [{"picture_url":"hyx1071472626984848.jpg"},{"picture_url":"hyx1031472626984840.jpg"},{"picture_url":"hyx8251472626984826.jpg"},{"picture_url":"hyx1401472626984802.jpg"}]
     */

    private String id;
    private String userid;
    private String content;
    private String create_time;
    /**
     * name : 王志恒
     * photo : weixiaotong.png
     */

    private TeacherInfoBean teacher_info;
    /**
     * id : 98
     * homework_id : 4
     * receiverid : 655
     * read_time : null
     * receiver_info : [{"name":"嗯嗯","photo":"weixiaotong.png","phone":"232323"}]
     */

    private List<ReceiverlistBean> receiverlist;
    /**
     * picture_url : hyx1071472626984848.jpg
     */

    private List<PicBean> pic;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public TeacherInfoBean getTeacher_info() {
        return teacher_info;
    }

    public void setTeacher_info(TeacherInfoBean teacher_info) {
        this.teacher_info = teacher_info;
    }

    public List<ReceiverlistBean> getReceiverlist() {
        return receiverlist;
    }

    public void setReceiverlist(List<ReceiverlistBean> receiverlist) {
        this.receiverlist = receiverlist;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public static class TeacherInfoBean {
        private String name;
        private String photo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    public static class ReceiverlistBean implements Serializable {
        private String id;
        private String homework_id;
        private String receiverid;
        private Object read_time;
        /**
         * name : 嗯嗯
         * photo : weixiaotong.png
         * phone : 232323
         */

        private List<ReceiverInfoBean> receiver_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHomework_id() {
            return homework_id;
        }

        public void setHomework_id(String homework_id) {
            this.homework_id = homework_id;
        }

        public String getReceiverid() {
            return receiverid;
        }

        public void setReceiverid(String receiverid) {
            this.receiverid = receiverid;
        }

        public Object getRead_time() {
            return read_time;
        }

        public void setRead_time(Object read_time) {
            this.read_time = read_time;
        }

        public List<ReceiverInfoBean> getReceiver_info() {
            return receiver_info;
        }

        public void setReceiver_info(List<ReceiverInfoBean> receiver_info) {
            this.receiver_info = receiver_info;
        }

        public static class ReceiverInfoBean implements Serializable{
            private String name;
            private String photo;
            private String phone;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }

    public static class PicBean {
        private String picture_url;

        public String getPicture_url() {
            return picture_url;
        }

        public void setPicture_url(String picture_url) {
            this.picture_url = picture_url;
        }
    }
}
