package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class SystemNews implements Serializable{

    /**
     * id : 16
     * term_id : 3
     * term_name : 系统消息
     * post_title : 教师风采 薛凌燕
     * post_excerpt : 教师风采 薛凌燕
     * post_date : 2016-06-17 07:53:56
     * post_source : 教师风采 薛凌燕
     * post_like : 0
     * post_hits : 11
     * recommended : 0
     * smeta : {"thumb":"57633c0a1f947.jpg"}
     * thumb : /data/upload/57633c0a1f947.jpg
     */

    private String id;
    private String term_id;
    private String term_name;
    private String post_title;
    private String post_excerpt;
    private String post_date;
    private String post_source;
    private String post_like;
    private String post_hits;
    private String recommended;
    private String smeta;
    private String thumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
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

    public String getPost_source() {
        return post_source;
    }

    public void setPost_source(String post_source) {
        this.post_source = post_source;
    }

    public String getPost_like() {
        return post_like;
    }

    public void setPost_like(String post_like) {
        this.post_like = post_like;
    }

    public String getPost_hits() {
        return post_hits;
    }

    public void setPost_hits(String post_hits) {
        this.post_hits = post_hits;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
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
