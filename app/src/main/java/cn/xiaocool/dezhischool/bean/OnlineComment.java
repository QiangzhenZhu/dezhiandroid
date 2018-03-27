package cn.xiaocool.dezhischool.bean;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class OnlineComment {

    /**
     * id : 5
     * schoolid : 1
     * userid : 597
     * content : 留言
     * send_time : 1473214992
     * feedback :
     * feed_time : 0
     */

    private String id;
    private String schoolid;
    private String userid;
    private String content;
    private String send_time;
    private String feedback;
    private String feed_time;

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

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeed_time() {
        return feed_time;
    }

    public void setFeed_time(String feed_time) {
        this.feed_time = feed_time;
    }
}
