package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class WebOther implements Serializable{

    /**
     * id : 16
     * schoolid : 1
     * post_title : 教师风采   薛凌燕
     * post_excerpt : 教师风采   薛凌燕
     * post_keywords : 教师风采   薛凌燕1
     * post_date : 2000-01-01 00:00:00
     * smeta : {"thumb":"57633c0a1f947.jpg"}
     * thumb : /data/upload/57633c0a1f947.jpg
     */

    private String id;
    private String schoolid;
    private String post_title;
    private String post_excerpt;
    private String post_keywords;
    private String post_date;
    private String smeta;
    private String thumb;

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

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }

    public String getPost_keywords() {
        return post_keywords;
    }

    public void setPost_keywords(String post_keywords) {
        this.post_keywords = post_keywords;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getSmeta() {
        return smeta;
    }

    public void setSmeta(String smeta) {
        this.smeta = smeta;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
