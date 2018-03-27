package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class SchoolNewsSend {

    /**
     * id : 476
     * send_user_id : 605
     * schoolid : 1
     * send_user_name : 再见十八岁
     * message_content : 扣扣空间
     * message_time : 1471957859
     * message_type : 0
     * receiver : [{"receiver_user_id":"647","receiver_user_name":"随便1","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"646","receiver_user_name":"随变2","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"648","receiver_user_name":"随便3","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"643","receiver_user_name":"随便4","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"599","receiver_user_name":"随便5","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"645","receiver_user_name":"随便6","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"644","receiver_user_name":"随便7","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"649","receiver_user_name":"随便8","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"657","receiver_user_name":"随便9","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"658","receiver_user_name":"10","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"665","receiver_user_name":"朱晟超","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"666","receiver_user_name":"秦安雅","read_time":null,"photo":"newsgroup9721471506703347.jpg","phone":""},{"receiver_user_id":"667","receiver_user_name":"林程跃","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"668","receiver_user_name":"林欣辰","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"669","receiver_user_name":"曾熙","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"670","receiver_user_name":"黄显江","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"671","receiver_user_name":"薛直得","read_time":null,"photo":"weixiaotong.png","phone":""},{"receiver_user_id":"682","receiver_user_name":"蒋庆学生","read_time":null,"photo":"20160817151532682.png","phone":""},{"receiver_user_id":"661","receiver_user_name":"啊强小衰","read_time":null,"photo":"20160820103038661.png","phone":"15653579769"}]
     * receiver_num : 19
     * picture : [{"picture_url":"newsgroup491471957856425.jpg"},{"picture_url":"newsgroup5091471957856328.jpg"}]
     */

    private String id;
    private String send_user_id;
    private String schoolid;
    private String send_user_name;
    private String message_content;
    private String message_time;
    private String message_type;
    private String userphoto;
    private int receiver_num;

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    /**
     * receiver_user_id : 647
     * receiver_user_name : 随便1
     * read_time : null
     * photo : weixiaotong.png
     * phone :
     */


    private List<ReceiverBean> receiver;
    /**
     * picture_url : newsgroup491471957856425.jpg
     */

    private List<PictureBean> picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(String send_user_id) {
        this.send_user_id = send_user_id;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getSend_user_name() {
        return send_user_name;
    }

    public void setSend_user_name(String send_user_name) {
        this.send_user_name = send_user_name;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public int getReceiver_num() {
        return receiver_num;
    }

    public void setReceiver_num(int receiver_num) {
        this.receiver_num = receiver_num;
    }

    public List<ReceiverBean> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<ReceiverBean> receiver) {
        this.receiver = receiver;
    }

    public List<PictureBean> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureBean> picture) {
        this.picture = picture;
    }

    public static class ReceiverBean implements Serializable{
        private String receiver_user_id;
        private String receiver_user_name;
        private Object read_time;
        private String photo;
        private String phone;

        public String getReceiver_user_id() {
            return receiver_user_id;
        }

        public void setReceiver_user_id(String receiver_user_id) {
            this.receiver_user_id = receiver_user_id;
        }

        public String getReceiver_user_name() {
            return receiver_user_name;
        }

        public void setReceiver_user_name(String receiver_user_name) {
            this.receiver_user_name = receiver_user_name;
        }

        public Object getRead_time() {
            return read_time;
        }

        public void setRead_time(Object read_time) {
            this.read_time = read_time;
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
