package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class Classevents implements Serializable{


    /**
     * id : 85
     * userid : 605
     * classid : 0
     * title : 还好
     * content : 8.1.2
     * contactman : 真后悔
     * contactphone : 18363585629
     * begintime : 1470022510
     * endtime : 1471491312
     * starttime : 1470022527
     * finishtime : 1472182529
     * isapply : 1
     * create_time : 1470022533
     * username : 潘宁
     * readcount : 1
     * allreader : 5
     * readtag : 1
     * comment : []
     * like : []
     * receiverlist : [{"id":"203","activity_id":"85","receiverid":"661","read_time":null,"receiver_info":[{"name":"啊强小衰","photo":"newsgroup31470105950927.jpg","phone":"15653579769"}]}]
     * teacher_info : {"name":"再见十八岁","photo":"newsgroup1271470103737546.jpg"}
     * pic : [{"picture_url":"newsgroup6351470022508991.jpg"},{"picture_url":"newsgroup2211470022508977.jpg"},{"picture_url":"newsgroup1141470022508967.jpg"},{"picture_url":"newsgroup6441470022508944.jpg"}]
     * applylist : [{"userid":"661","avatar":"newsgroup31470105950927.jpg","name":"啊强小衰","applyid":"31","fathername":"","contactphone":"","age":"0","sex":"0","create_time":"1470022560"}]
     */

    private String id;
    private String userid;
    private String classid;
    private String title;
    private String content;
    private String contactman;
    private String contactphone;
    private String begintime;
    private String endtime;
    private String starttime;
    private String finishtime;
    private String isapply;
    private String create_time;
    private String username;
    private int readcount;
    private int allreader;
    private int readtag;
    /**
     * name : 再见十八岁
     * photo : newsgroup1271470103737546.jpg
     */

    private TeacherInfoBean teacher_info;
    private List<?> comment;
    private List<?> like;
    /**
     * id : 203
     * activity_id : 85
     * receiverid : 661
     * read_time : null
     * receiver_info : [{"name":"啊强小衰","photo":"newsgroup31470105950927.jpg","phone":"15653579769"}]
     */

    private List<ReceiverlistBean> receiverlist;
    /**
     * picture_url : newsgroup6351470022508991.jpg
     */

    private List<PicBean> pic;
    /**
     * userid : 661
     * avatar : newsgroup31470105950927.jpg
     * name : 啊强小衰
     * applyid : 31
     * fathername :
     * contactphone :
     * age : 0
     * sex : 0
     * create_time : 1470022560
     */

    private List<ApplylistBean> applylist;

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

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContactman() {
        return contactman;
    }

    public void setContactman(String contactman) {
        this.contactman = contactman;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getIsapply() {
        return isapply;
    }

    public void setIsapply(String isapply) {
        this.isapply = isapply;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getReadcount() {
        return readcount;
    }

    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }

    public int getAllreader() {
        return allreader;
    }

    public void setAllreader(int allreader) {
        this.allreader = allreader;
    }

    public int getReadtag() {
        return readtag;
    }

    public void setReadtag(int readtag) {
        this.readtag = readtag;
    }

    public TeacherInfoBean getTeacher_info() {
        return teacher_info;
    }

    public void setTeacher_info(TeacherInfoBean teacher_info) {
        this.teacher_info = teacher_info;
    }

    public List<?> getComment() {
        return comment;
    }

    public void setComment(List<?> comment) {
        this.comment = comment;
    }

    public List<?> getLike() {
        return like;
    }

    public void setLike(List<?> like) {
        this.like = like;
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

    public List<ApplylistBean> getApplylist() {
        return applylist;
    }

    public void setApplylist(List<ApplylistBean> applylist) {
        this.applylist = applylist;
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

    public static class ReceiverlistBean {
        private String id;
        private String activity_id;
        private String receiverid;
        private Object read_time;
        /**
         * name : 啊强小衰
         * photo : newsgroup31470105950927.jpg
         * phone : 15653579769
         */

        private List<ReceiverInfoBean> receiver_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
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

        public static class ReceiverInfoBean {
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
        private String description;

        public String getPicture_url() {
            return picture_url;
        }

        public void setPicture_url(String picture_url) {
            this.picture_url = picture_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class ApplylistBean {
        private String userid;
        private String avatar;
        private String name;
        private String applyid;
        private String fathername;
        private String contactphone;
        private String age;
        private String sex;
        private String create_time;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplyid() {
            return applyid;
        }

        public void setApplyid(String applyid) {
            this.applyid = applyid;
        }

        public String getFathername() {
            return fathername;
        }

        public void setFathername(String fathername) {
            this.fathername = fathername;
        }

        public String getContactphone() {
            return contactphone;
        }

        public void setContactphone(String contactphone) {
            this.contactphone = contactphone;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
