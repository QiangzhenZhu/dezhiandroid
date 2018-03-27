package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/26.
 */
public class WebListInfo implements Serializable{

    /**
     * id : 1
     * schoolid : 1
     * post_title : dasd
     * post_excerpt : dawdsa
     * post_date : 2016-06-23 11:21:30
     * smeta : {"thumb":""}
     * thumb :
     */

    private String id;
    private String schoolid;
    private String post_title;
    private String post_excerpt;
    private String post_date;
    private String smeta;
    private String thumb;

    private String where;

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

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}
