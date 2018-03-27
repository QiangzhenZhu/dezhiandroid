package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class SchoolNewsReceiver {

    /**
     * classid : 2
     * receive : [{"id":"2","message_id":"1","receiver_user_id":"2","receiver_user_name":"","message_type":"0","read_time":null,"send_message":{"id":"1","schoolid":"1","send_user_id":"599","send_user_name":"郑敬概","message_content":"小一班啊，小二班","message_time":"1472544630","photo":"weixiaotong.png"},"pic":[{"picture_url":"hyx9031472544628354.jpg"},{"picture_url":"hyx1911472544628333.jpg"},{"picture_url":"hyx1471472544628300.jpg"}]}]
     */

    private String classid;
    /**
     * id : 2
     * message_id : 1
     * receiver_user_id : 2
     * receiver_user_name :
     * message_type : 0
     * read_time : null
     * send_message : {"id":"1","schoolid":"1","send_user_id":"599","send_user_name":"郑敬概","message_content":"小一班啊，小二班","message_time":"1472544630","photo":"weixiaotong.png"}
     * pic : [{"picture_url":"hyx9031472544628354.jpg"},{"picture_url":"hyx1911472544628333.jpg"},{"picture_url":"hyx1471472544628300.jpg"}]
     */

    private List<ReceiveBean> receive;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public List<ReceiveBean> getReceive() {
        return receive;
    }

    public void setReceive(List<ReceiveBean> receive) {
        this.receive = receive;
    }

    public static class ReceiveBean {
        private String id;
        private String message_id;
        private String receiver_user_id;
        private String receiver_user_name;
        private String message_type;
        private Object read_time;
        /**
         * id : 1
         * schoolid : 1
         * send_user_id : 599
         * send_user_name : 郑敬概
         * message_content : 小一班啊，小二班
         * message_time : 1472544630
         * photo : weixiaotong.png
         */

        private SendMessageBean send_message;
        /**
         * picture_url : hyx9031472544628354.jpg
         */

        private List<PicBean> pic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

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

        public String getMessage_type() {
            return message_type;
        }

        public void setMessage_type(String message_type) {
            this.message_type = message_type;
        }

        public Object getRead_time() {
            return read_time;
        }

        public void setRead_time(Object read_time) {
            this.read_time = read_time;
        }

        public SendMessageBean getSend_message() {
            return send_message;
        }

        public void setSend_message(SendMessageBean send_message) {
            this.send_message = send_message;
        }

        public List<PicBean> getPic() {
            return pic;
        }

        public void setPic(List<PicBean> pic) {
            this.pic = pic;
        }

        public static class SendMessageBean {
            private String id;
            private String schoolid;
            private String send_user_id;
            private String send_user_name;
            private String message_content;
            private String message_time;
            private String photo;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSchoolid() {
                return schoolid;
            }

            public void setSchoolid(String schoolid) {
                this.schoolid = schoolid;
            }

            public String getSend_user_id() {
                return send_user_id;
            }

            public void setSend_user_id(String send_user_id) {
                this.send_user_id = send_user_id;
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

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
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
}
