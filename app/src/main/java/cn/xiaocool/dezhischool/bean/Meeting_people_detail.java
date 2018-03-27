package cn.xiaocool.dezhischool.bean;

import java.io.Serializable;

/**
 * Created by 10835 on 2018/2/5.
 */

public class Meeting_people_detail implements Serializable {
    private String name;
    private String id;
    private String comeTime;
    private String levelTime;
    private String levelReason;

    public String getLevelReason() {
        return levelReason;
    }

    public void setLevelReason(String levelReason) {
        this.levelReason = levelReason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComeTime() {
        return comeTime;
    }

    public void setComeTime(String comeTime) {
        this.comeTime = comeTime;
    }

    public String getLevelTime() {
        return levelTime;
    }

    public void setLevelTime(String levelTime) {
        this.levelTime = levelTime;
    }
}
