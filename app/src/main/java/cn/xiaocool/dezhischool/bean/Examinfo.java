package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class Examinfo {

        /**
         * id : 1
         * exam_name : 第一次期末考试
         * subject : ["语文","数学","体育"]
         */

        private String id;
        private String exam_name;
        private List<String> subject;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getExam_name() {
            return exam_name;
        }

        public void setExam_name(String exam_name) {
            this.exam_name = exam_name;
        }

        public List<String> getSubject() {
            return subject;
        }

        public void setSubject(List<String> subject) {
            this.subject = subject;
        }
    }
