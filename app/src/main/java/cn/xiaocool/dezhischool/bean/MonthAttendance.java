package cn.xiaocool.dezhischool.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class MonthAttendance {


        /**
         * userid : 15875
         * name : 测试-8
         * photo : weixiaotong.png
         * isleave : 0
         * leave_count : 0
         * leave_arr :
         * noarrive : 30
         * noarrive_arr : [1498838400,1498924800,1499011200,1499097600,1499270400,1499356800,1499443200,1499529600,1499616000,1499702400,1499788800,1499875200,1499961600,1500048000,1500134400,1500220800,1500307200,1500393600,1500480000,1500566400,1500652800,1500739200,1500825600,1500912000,1500998400,1501084800,1501171200,1501257600,1501344000,1501430400]
         */

        private String userid;
        private String name;
        private String photo;
        private int isleave;
        private int leave_count;
        private List<Integer> leave_arr;
        private int noarrive;
        private List<Integer> noarrive_arr;

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

        public int getIsleave() {
            return isleave;
        }

        public void setIsleave(int isleave) {
            this.isleave = isleave;
        }

        public int getLeave_count() {
            return leave_count;
        }

        public void setLeave_count(int leave_count) {
            this.leave_count = leave_count;
        }

        public List<Integer> getLeave_arr() {
            return leave_arr;
        }

        public void setLeave_arr(List<Integer> leave_arr) {
            this.leave_arr = leave_arr;
        }

        public int getNoarrive() {
            return noarrive;
        }

        public void setNoarrive(int noarrive) {
            this.noarrive = noarrive;
        }

        public List<Integer> getNoarrive_arr() {
            return noarrive_arr;
        }

        public void setNoarrive_arr(List<Integer> noarrive_arr) {
            this.noarrive_arr = noarrive_arr;
        }
    }

