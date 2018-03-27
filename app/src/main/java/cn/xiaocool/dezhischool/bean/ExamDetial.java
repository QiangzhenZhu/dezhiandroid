package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class ExamDetial {


    /**
     * userid : 15856
     * name : 学生4
     * photo : weixiaotong.png
     * score_count : 300
     * rank : 2
     * subject : [{"subject":"语文","score":"100"},{"subject":"数学","score":"85"},{"subject":"体育","score":"100"}]
     */

    private String userid;
    private String name;
    private String photo;
    private String score_count;
    private String rank;
    private List<SubjectBean> subject;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getScore_count() {
        return score_count;
    }

    public void setScore_count(String score_count) {
        this.score_count = score_count;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public List<SubjectBean> getSubject() {
        return subject;
    }

    public void setSubject(List<SubjectBean> subject) {
        this.subject = subject;
    }

    public static class SubjectBean {
        /**
         * subject : 语文
         * score : 100
         */

        private String subject;
        private String score;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }
}
