package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class ClassNewsReceive {

    /**
     * id : 98
     * homework_id : 4
     * receiverid : 655
     * read_time : 1472627585
     * homework_info : [{"id":"4","userid":"604","content":"班级消息8-31","create_time":"1472626986","name":"王志恒","photo":"weixiaotong.png"}]
     * receive_list : [{"name":"嗯嗯","photo":"weixiaotong.png","phone":"232323","receiverid":"655","read_time":"1472628315"}]
     * picture : [{"picture_url":"hyx1071472626984848.jpg"},{"picture_url":"hyx1031472626984840.jpg"},{"picture_url":"hyx8251472626984826.jpg"},{"picture_url":"hyx1401472626984802.jpg"}]
     */

    private String id;
    private String homework_id;
    private String receiverid;
    private String read_time;
    /**
     * id : 4
     * userid : 604
     * content : 班级消息8-31
     * create_time : 1472626986
     * name : 王志恒
     * photo : weixiaotong.png
     */

    private List<HomeworkInfoBean> homework_info;
    /**
     * name : 嗯嗯
     * photo : weixiaotong.png
     * phone : 232323
     * receiverid : 655
     * read_time : 1472628315
     */

    private List<ReceiveListBean> receive_list;
    /**
     * picture_url : hyx1071472626984848.jpg
     */

    private List<PictureBean> picture;

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

    public String getRead_time() {
        return read_time;
    }

    public void setRead_time(String read_time) {
        this.read_time = read_time;
    }

    public List<HomeworkInfoBean> getHomework_info() {
        return homework_info;
    }

    public void setHomework_info(List<HomeworkInfoBean> homework_info) {
        this.homework_info = homework_info;
    }

    public List<ReceiveListBean> getReceive_list() {
        return receive_list;
    }

    public void setReceive_list(List<ReceiveListBean> receive_list) {
        this.receive_list = receive_list;
    }

    public List<PictureBean> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureBean> picture) {
        this.picture = picture;
    }

    public static class HomeworkInfoBean {
        private String id;
        private String userid;
        private String content;
        private String create_time;
        private String name;
        private String photo;

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

    public static class ReceiveListBean {
        private String name;
        private String photo;
        private String phone;
        private String receiverid;
        private String read_time;

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

        public String getReceiverid() {
            return receiverid;
        }

        public void setReceiverid(String receiverid) {
            this.receiverid = receiverid;
        }

        public String getRead_time() {
            return read_time;
        }

        public void setRead_time(String read_time) {
            this.read_time = read_time;
        }
    }

    public static class PictureBean {
        private String picture_url;

        public String getPicture_url() {
            return picture_url;
        }

        public void setPicture_url(String picture_url) {
            this.picture_url = picture_url;
        }
    }
}
