package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class Trends {

    /**
     * mid : 1
     * type : 1
     * schoolid : 1
     * classid : 3
     * userid : 686
     * name : 小头
     * content : 哈哈哈
     * write_time : 1472717936
     * photo : weixiaotong.png
     * pic : [{"pictureurl":"hyx7991472717935446.jpg"},{"pictureurl":"hyx6421472717935435.jpg"}]
     * like : [{"userid":"604","name":"王志恒"}]
     * comment : [{"userid":"604","name":"王志恒","content":"哈哈","avatar":"weixiaotong.png","comment_time":"1472718250"},{"userid":"604","name":"王志恒","content":"哈哈","avatar":"weixiaotong.png","comment_time":"1472718278"},{"userid":"604","name":"王志恒","content":"哈哈","avatar":"weixiaotong.png","comment_time":"1472718467"}]
     */

    private String mid;
    private String type;
    private String schoolid;
    private String classid;
    private String userid;
    private String name;
    private String content;
    private String write_time;
    private String photo;
    private String video;
    private String video_phone;
    /**
     * pictureurl : hyx7991472717935446.jpg
     */

    private List<PicBean> pic;
    /**
     * userid : 604
     * name : 王志恒
     */

    private List<LikeBean> like;
    /**
     * userid : 604
     * name : 王志恒
     * content : 哈哈
     * avatar : weixiaotong.png
     * comment_time : 1472718250
     */

    private List<CommentBean> comment;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWrite_time() {
        return write_time;
    }

    public void setWrite_time(String write_time) {
        this.write_time = write_time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public List<LikeBean> getLike() {
        return like;
    }

    public void setLike(List<LikeBean> like) {
        this.like = like;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo_phone() {
        return video_phone;
    }

    public void setVideo_phone(String video_phone) {
        this.video_phone = video_phone;
    }

    public static class PicBean {
        private String pictureurl;

        public String getPictureurl() {
            return pictureurl;
        }

        public void setPictureurl(String pictureurl) {
            this.pictureurl = pictureurl;
        }
    }

    public static class LikeBean {
        private String userid;
        private String name;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class CommentBean {
        private String userid;
        private String name;
        private String content;
        private String avatar;
        private String comment_time;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getComment_time() {
            return comment_time;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }
    }
}
