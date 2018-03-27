package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/6/19.
 */


public class TeacherReview implements Serializable {
    private String id;
    private String teacherName;
    private String teacherAvator;
    private String time;
    private String learn;
    private String work;
    private String sing;
    private String labour;
    private String strain;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherAvator() {
        return teacherAvator;
    }

    public void setTeacherAvator(String teacherAvator) {
        this.teacherAvator = teacherAvator;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLearn() {
        return learn;
    }

    public void setLearn(String learn) {
        this.learn = learn;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }

    public String getLabour() {
        return labour;
    }

    public void setLabour(String labour) {
        this.labour = labour;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
